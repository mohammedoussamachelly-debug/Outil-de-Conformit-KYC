package kyc.app;

import kyc.GenerateurDeCandidats.*;
import kyc.comparateur.*;
import kyc.indexation.*;
import kyc.model.Couple;
import kyc.model.Groupe;
import kyc.model.Nom;
import kyc.pretraitement.*;
import kyc.selection.*;
import kyc.SourcesDeNoms.LecteurListeCSV;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.IdentityHashMap;

public class Benchmark {

    static final String DATA_DIR = "data/names_matching_peps-20260513T183405Z-3-001/names_matching_peps/";
    static final String DB_FILE  = DATA_DIR + "peps_names_658k.csv";
    static final String OUT_CSV  = "benchmark_results.csv";
    static final double SEUIL    = 0.70;
    static final long   TIMEOUT_SEC      = 60;
    static final int    MAX_DB_FOR_G1    = 50_000;   // G1 skipped si db > cette taille
    static final long   MAX_CANDIDATS    = 5_000_000L; // abort si trop de candidats generés

    // Query files : 800, 2k, 4k recherches dans la base 658k
    static final String[] QUERY_FILES = {
        DATA_DIR + "peps_names_800.csv",
        DATA_DIR + "peps_names_2k.csv",
        DATA_DIR + "peps_names_4k.csv",
    };

    static final String[][] PRETRAITEURS = {{"P1"}, {"P2"}, {"P1","P2"}};
    static final String[]   COMPARATEURS = {"C1","C2","C3","C4"};
    static final String[]   GENERATEURS  = {"G1","G2","G3","G4","G5"};
    static final String[]   INDEXES      = {"I1","I2","I3"};
    static final String[]   SELECTEURS   = {"S1","S2","S3"};

    static final String[] PRET_LABELS = {
        "SupprimerAccents", "ConvertirMinuscule", "SupprimerAccents+ConvertirMinuscule"
    };
    static final String[] COMP_LABELS = {"Levenshtein","JaroWinkler","EgaliteExacte","Global"};
    static final String[] GEN_LABELS  = {"Lineaire","LongueurEgale","Prefixe","Phonetique","LettresCommunes"};
    static final String[] IDX_LABELS  = {"Dictionnaire","Tri","Arbre"};
    static final String[] SEL_LABELS  = {"TopN(10)","TopPourcentage(50)","Tous"};

    public static void main(String[] args) throws Exception {

        PrintStream out     = System.out;
        PrintStream nullOut = new PrintStream(OutputStream.nullOutputStream());

        // ── Chargement base 658k ─────────────────────────────────────
        out.println("Chargement de la base de donnees 658k...");
        List<Nom> database = new LecteurListeCSV(DB_FILE).lire();
        for (Nom n : database) {
            List<String> mots = n.getNomPretraite();
            for (int j = 0; j < mots.size(); j++)
                mots.set(j, mots.get(j).toLowerCase());
        }
        out.println(database.size() + " noms charges dans la base.\n");

        int combosParFichier = PRETRAITEURS.length * COMPARATEURS.length
                             * GENERATEURS.length * INDEXES.length * SELECTEURS.length;
        int totalRuns = QUERY_FILES.length * combosParFichier;
        out.printf("Benchmark : %d fichiers x %d combinaisons = %d runs (timeout=%ds par combo)%n%n",
                   QUERY_FILES.length, combosParFichier, totalRuns, TIMEOUT_SEC);

        // ── En-tete CSV ───────────────────────────────────────────────
        FileWriter fw = new FileWriter(OUT_CSV);
        fw.write(
            "query_file,nb_requetes,pretraiteur,comparateur,generateur,index,selectionneur," +
            "statut,temps_total_ms,temps_par_requete_ms," +
            "nb_candidats_moyen,nb_matches_avant_selection,nb_matches_apres_selection," +
            "nb_requetes_avec_match,taux_couverture_pct," +
            "score_moyen,score_max,score_min\n"
        );

        int done = 0;
        ExecutorService exec = Executors.newSingleThreadExecutor();

        for (String queryFile : QUERY_FILES) {
            String qName = new File(queryFile).getName();

            // Chargement des requetes pour ce fichier
            out.printf("%n=== Fichier requetes : %s ===%n", qName);
            List<Nom> requetesRef = new LecteurListeCSV(queryFile).lire();
            out.printf("    %d requetes chargees%n%n", requetesRef.size());

            for (int pi = 0; pi < PRETRAITEURS.length; pi++) {
                String[] cPret   = PRETRAITEURS[pi];
                String pretLabel = PRET_LABELS[pi];

                for (int ci = 0; ci < COMPARATEURS.length; ci++) {
                    String cComp     = COMPARATEURS[ci];
                    String compLabel = COMP_LABELS[ci];

                    for (int gi = 0; gi < GENERATEURS.length; gi++) {
                        String cGen    = GENERATEURS[gi];
                        String genLabel = GEN_LABELS[gi];

                        for (int ii = 0; ii < INDEXES.length; ii++) {
                            String cIdx    = INDEXES[ii];
                            String idxLabel = IDX_LABELS[ii];

                            for (int si = 0; si < SELECTEURS.length; si++) {
                                String cSel    = SELECTEURS[si];
                                String selLabel = SEL_LABELS[si];

                                // G1, G2, G4, G5 incompatibles avec grandes bases (O(N*db) / OOM / timeout)
                                if (("G1".equals(cGen) || "G2".equals(cGen) || "G4".equals(cGen) || "G5".equals(cGen)) && database.size() > MAX_DB_FOR_G1) {
                                    fw.write(String.format(Locale.US,
                                        "%s,%d,%s,%s,%s,%s,%s,INCOMPATIBLE,,,,,,,,,,\n",
                                        qName, requetesRef.size(),
                                        pretLabel, compLabel, genLabel, idxLabel, selLabel));
                                    fw.flush();
                                    done++;
                                    out.printf("  [%4d/%d]  %-25s | %-14s | %-14s | %-12s | %-20s  -> INCOMPATIBLE (%s sur %dk)%n",
                                        done, totalRuns, qName, compLabel, genLabel, idxLabel, selLabel,
                                        cGen, database.size()/1000);
                                    continue;
                                }

                                // Copie fraiche des requetes
                                final List<Nom> requetes    = deepCopy(requetesRef);
                                final List<Nom> db          = database;
                                final List<Pretraiteur> pret = buildPretraiteur(cPret);
                                final ComparateurDeNom comp = buildComparateur(cComp);
                                final GenerateurDeCandidat gen = buildGenerateur(cGen);
                                final Index index           = buildIndex(cIdx);
                                final Selectionneur sel     = buildSelectionneur(cSel);

                                // Lancement avec timeout
                                System.setOut(nullOut);
                                Future<long[]> future = exec.submit(() ->
                                    runCombo(requetes, db, pret, comp, gen, index, sel)
                                );

                                long[]  stats  = null;
                                String  statut = "OK";
                                try {
                                    stats = future.get(TIMEOUT_SEC, TimeUnit.SECONDS);
                                } catch (TimeoutException e) {
                                    future.cancel(true);
                                    statut = "TIMEOUT";
                                } catch (Exception e) {
                                    statut = "ERREUR";
                                }
                                System.setOut(out);

                                done++;
                                // Trop de candidats → marquer sans attendre le timeout
                                if ("OK".equals(statut) && stats != null && stats[0] == -2) {
                                    statut = "TROP_CANDIDATS(" + stats[1] + ")";
                                    stats  = null;
                                }

                                if ("OK".equals(statut) && stats != null) {
                                    int    nbReq   = requetes.size();
                                    double tMs     = stats[0] / 1_000_000.0;
                                    double tMsReq  = tMs / nbReq;
                                    double cMoy    = (double) stats[1] / nbReq;
                                    int    avSel   = (int) stats[2];
                                    int    apSel   = (int) stats[3];
                                    int    avMatch = (int) stats[4];
                                    double couv    = (double) avMatch / nbReq * 100.0;
                                    int    nSc     = (int) stats[7];
                                    double sMoy    = nSc > 0 ? stats[5] / 1e6 / nSc : 0;
                                    double sMax    = nSc > 0 ? stats[6] / 1e6        : 0;
                                    double sMin    = nSc > 0 ? stats[8] / 1e6        : 0;

                                    fw.write(String.format(Locale.US,
                                        "%s,%d,%s,%s,%s,%s,%s," +
                                        "OK,%.2f,%.3f," +
                                        "%.1f,%d,%d," +
                                        "%d,%.1f," +
                                        "%.4f,%.4f,%.4f\n",
                                        qName, nbReq, pretLabel, compLabel, genLabel, idxLabel, selLabel,
                                        tMs, tMsReq,
                                        cMoy, avSel, apSel,
                                        avMatch, couv,
                                        sMoy, sMax, sMin));

                                    out.printf("  [%4d/%d]  %-25s | %-14s | %-14s | %-12s | %-20s  -> %8.1f ms  %d/%d match%n",
                                        done, totalRuns, qName, compLabel, genLabel, idxLabel, selLabel,
                                        tMs, avMatch, nbReq);
                                } else {
                                    fw.write(String.format(Locale.US,
                                        "%s,%d,%s,%s,%s,%s,%s,%s,,,,,,,,,,\n",
                                        qName, requetesRef.size(),
                                        pretLabel, compLabel, genLabel, idxLabel, selLabel, statut));
                                    out.printf("  [%4d/%d]  %-25s | %-14s | %-14s | %-12s | %-20s  -> %s%n",
                                        done, totalRuns, qName, compLabel, genLabel, idxLabel, selLabel, statut);
                                }
                                fw.flush();
                            }
                        }
                    }
                }
            }
        }

        exec.shutdown();
        fw.close();

        out.println("\n============================================");
        out.println("  Benchmark termine ! " + done + " runs.");
        out.println("  Resultats dans : " + OUT_CSV);
        out.println("============================================");
    }

    // ── Execution d'une combinaison, retourne tableau de stats ────────
    // Passe TOUTES les requetes en une seule fois au generateur pour
    // eviter de reconstruire les strings 658k × nb_requetes fois.
    // [0]=duree_ns, [1]=totalCandidats, [2]=avantSel, [3]=apresSel,
    // [4]=reqAvecMatch, [5]=sommeScores*1e6, [6]=scoreMax*1e6,
    // [7]=nbScores, [8]=scoreMin*1e6
    static long[] runCombo(List<Nom> requetes, List<Nom> database,
                           List<Pretraiteur> pret, ComparateurDeNom comp,
                           GenerateurDeCandidat gen, Index index,
                           Selectionneur sel) {

        // 1. Pretraitement de toutes les requetes
        for (Nom req : requetes)
            for (Pretraiteur p : pret) p.pretraiter(req);

        long debut = System.nanoTime();

        // 2. Generation en une seule passe (strings 658k construit une seule fois)
        List<Couple> allCandidats = gen.generercandidat(requetes, database, index);

        // Abort si trop de candidats (evite OOM et timeouts inutiles)
        if (allCandidats.size() > MAX_CANDIDATS) {
            return new long[]{-2, allCandidats.size(), -1, -1, -1, -1, -1, -1, -1};
        }

        // 3. Grouper les couples par requete (par identite d'objet)
        IdentityHashMap<Nom, List<Couple>> byReq = new IdentityHashMap<>();
        for (Nom req : requetes) byReq.put(req, new ArrayList<>());
        for (int k = 0; k < allCandidats.size(); k++) {
            Couple c = allCandidats.get(k);
            List<Couple> list = byReq.get(c.getNom1());
            if (list != null) list.add(c);
        }

        // 4. Scoring + selection par requete
        long totalCandidats = allCandidats.size();
        long avantSel = 0, apresSel = 0, reqAvecMatch = 0;
        long sommeScores = 0, scoreMax = 0, scoreMin = Long.MAX_VALUE, nbScores = 0;

        for (Nom req : requetes) {
            if (Thread.currentThread().isInterrupted()) break;

            List<Couple> candidates = byReq.get(req);
            List<Couple> scores = new ArrayList<>();
            for (int k = 0; k < candidates.size(); k++) {
                if (Thread.currentThread().isInterrupted()) break;
                Couple c = candidates.get(k);
                double score = comp.comparer(c.getNom1(), c.getNom2());
                if (score >= SEUIL)
                    scores.add(new Couple(c.getNom1(), c.getNom2(), score));
            }
            avantSel += scores.size();

            Groupe selected = sel.selectionner(scores, SEUIL);
            apresSel += selected.getCouples().size();
            if (!selected.getCouples().isEmpty()) reqAvecMatch++;

            for (int k = 0; k < selected.getCouples().size(); k++) {
                long s = (long)(selected.getCouples().get(k).getScore() * 1_000_000);
                sommeScores += s;
                if (s > scoreMax) scoreMax = s;
                if (s < scoreMin) scoreMin = s;
                nbScores++;
            }
        }

        long fin = System.nanoTime();
        return new long[]{
            fin - debut,
            totalCandidats, avantSel, apresSel,
            reqAvecMatch, sommeScores, scoreMax, nbScores,
            nbScores > 0 ? scoreMin : 0
        };
    }

    // ── Deep copy des requetes ────────────────────────────────────────
    static List<Nom> deepCopy(List<Nom> src) {
        List<Nom> copy = new ArrayList<>(src.size());
        for (Nom n : src)
            copy.add(new Nom(n.getId(), n.getNomOriginal(), n.getSource()));
        return copy;
    }

    // ── Factories ─────────────────────────────────────────────────────
    static List<Pretraiteur> buildPretraiteur(String[] codes) {
        List<Pretraiteur> liste = new ArrayList<>();
        for (String c : codes)
            liste.add("P1".equals(c) ? new SupprimerAccents() : new ConvertirMinuscule());
        return liste;
    }

    static ComparateurDeNom buildComparateur(String code) {
        switch (code) {
            case "C2": return new ComparateurDeNomParChamp(new ComparateurJaroWinkler());
            case "C3": return new ComparateurDeNomParChamp(new ComparateurEgalitExact());
            case "C4": return new ComparateurDeNomGlobal();
            default:   return new ComparateurDeNomParChamp(new ComparateurLevenshtein());
        }
    }

    static GenerateurDeCandidat buildGenerateur(String code) {
        switch (code) {
            case "G2": return new GenerateurLongueurEgale();
            case "G3": return new GenerateurPrefixe();
            case "G4": return new GenerateurPhonetique();
            case "G5": return new GenerateurLettresCommunes();
            default:   return new LinearGenerator();
        }
    }

    static Selectionneur buildSelectionneur(String code) {
        switch (code) {
            case "S2": return new SelectionneurTopPourcentage(50);
            case "S3": return new SelectionneurTous();
            default:   return new SelectionneurTopN(10);
        }
    }

    static Index buildIndex(String code) {
        switch (code) {
            case "I2": return new IndexTri();
            case "I3": return new IndexArbre();
            default:   return new IndexDictionnaire();
        }
    }
}

