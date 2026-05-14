package kyc.app;

import kyc.MoteurDeRecherche.MoteurDeRecherche;
import kyc.comparateur.*;
import kyc.GenerateurDeCandidats.*;
import kyc.affichage.LivreurDeResultat;
import kyc.indexation.IndexTri;
import kyc.model.Nom;
import kyc.pretraitement.*;
import kyc.selection.*;
import kyc.SourcesDeNoms.LecteurListeCSV;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    // ── ANSI colours ──────────────────────────────────────────────
    static final String R  = "[0m";
    static final String B  = "[1m";
    static final String CY = "[36m";
    static final String GR = "[32m";
    static final String YL = "[33m";
    static final String MG = "[35m";
    static final String RD = "[31m";
    static final String WH = "[37m";

    static final Scanner sc = new Scanner(System.in);
    static List<Nom> database = null;
    static String cheminCSV = "data/noms.csv";

    // ── helpers ───────────────────────────────────────────────────
    static void clear()  { System.out.print("\033[H\033[2J"); System.out.flush(); }
    static void pause()  { System.out.print(WH + "\n  Appuyez sur Entrée pour continuer..." + R); sc.nextLine(); }

    static void banner() {
        System.out.println(CY + B);
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║         OUTIL DE CONFORMITÉ KYC  v1.0           ║");
        System.out.println("  ║     Know Your Customer — Vérification de Noms   ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝" + R);
    }

    static void ligne(String titre) {
        System.out.println(CY + "\n  ┌─────────────────────────────────────────────────┐");
        System.out.printf( "  │  " + B + YL + "%-47s" + R + CY + "│\n", titre);
        System.out.println("  └─────────────────────────────────────────────────┘" + R);
    }

    static String lire(String invite, String defaut) {
        System.out.print(GR + "  ➜ " + WH + invite);
        if (!defaut.isEmpty()) System.out.print(WH + " [" + defaut + "]");
        System.out.print(" : " + R);
        String val = sc.nextLine().trim();
        return val.isEmpty() ? defaut : val;
    }

    static int menu(String titre, String... options) {
        ligne(titre);
        for (int i = 0; i < options.length; i++)
            System.out.printf("    " + MG + "%d" + R + ". %s\n", i + 1, options[i]);
        System.out.println();
        while (true) {
            String s = lire("Votre choix", "1");
            try {
                int n = Integer.parseInt(s);
                if (n >= 1 && n <= options.length) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println(RD + "  Choix invalide." + R);
        }
    }

    // ── screens ───────────────────────────────────────────────────
    static void chargerCSV() {
        ligne("Chargement du fichier CSV");
        cheminCSV = lire("Chemin du fichier CSV", cheminCSV);
        try {
            database = new LecteurListeCSV(cheminCSV).lire();
            System.out.println(GR + "  OK  " + database.size() + " noms charges depuis \"" + cheminCSV + "\"" + R);
        } catch (Exception e) {
            System.out.println(RD + "  ✘  Erreur : " + e.getMessage() + R);
            database = null;
        }
        pause();
    }

    static void lanceRecherche() {
        if (database == null || database.isEmpty()) {
            System.out.println(RD + "\n  ✘  Aucune liste chargée. Chargez d'abord un fichier CSV." + R);
            pause(); return;
        }

        ligne("Paramètres de recherche");
        String nomStr = lire("Nom à rechercher", "");
        if (nomStr.isEmpty()) return;
        Nom requete = new Nom(nomStr);

        // Comparateur
        int choixComp = menu("Algorithme de comparaison",
                "Levenshtein  (distance d'édition)",
                "Jaro-Winkler (similarité phonétique)",
                "Egalité exacte");
        ComparateurDeChaine chaine;
        switch (choixComp) {
            case 2: chaine = new ComparateurJaroWinkler(); break;
            case 3: chaine = new ComparateurEgalitExact();  break;
            default: chaine = new ComparateurLevenshtein(); break;
        }
        ComparateurDeNom comparateur = new ComparateurDeNomParChamp(chaine);

        // Seuil
        double seuil = 0.7;
        try { seuil = Double.parseDouble(lire("Seuil de similarité (0.0 – 1.0)", "0.70")); }
        catch (NumberFormatException ignored) {}

        // Sélectionneur
        int choixSel = menu("Mode de sélection des résultats",
                "Top N  (les N meilleurs)",
                "Top Pourcentage  (% des meilleurs)",
                "Tous   (tous les résultats au-dessus du seuil)");
        Selectionneur selectionneur;
        int maxResultats = 10;
        switch (choixSel) {
            case 2:
                int pct = 50;
                try { pct = Integer.parseInt(lire("Pourcentage (1-100)", "50")); } catch (Exception ignored) {}
                selectionneur = new SelectionneurTopPourcentage(pct);
                break;
            case 3:
                selectionneur = new SelectionneurTous();
                break;
            default:
                try { maxResultats = Integer.parseInt(lire("Nombre de résultats N", "10")); } catch (Exception ignored) {}
                selectionneur = new SelectionneurTopN(maxResultats);
                break;
        }

        // Générateur
        int choixGen = menu("Générateur de candidats",
                "Linéaire   (compare avec toute la liste)",
                "Phonétique (filtre par Soundex d'abord)");
        String typeGen = choixGen == 2 ? "Phonetique" : "Linear";

        // Prétraitement
        int choixPre = menu("Prétraitement",
                "Convertir en minuscules",
                "Supprimer les accents");
        Pretraiteur pretraiteur = choixPre == 2 ? new SupprimerAccents() : new ConvertirMinuscule();

        // Lancement
        GenerateurDeCandidat generateur = typeGen.equals("Phonetique")
                ? new GenerateurPhonetique()
                : new LinearGenerator();
        MoteurDeRecherche moteur = new MoteurDeRecherche(
                Arrays.asList(pretraiteur), comparateur, generateur,
                selectionneur, new LivreurDeResultat(), new IndexTri(), seuil);

        System.out.println();
        moteur.chercher(requete, database);
        pause();
    }

    static void afficherListe() {
        if (database == null || database.isEmpty()) {
            System.out.println(RD + "\n  ✘  Aucune liste chargée." + R);
            pause(); return;
        }
        ligne("Liste chargée (" + database.size() + " noms)");
        int lim = Math.min(20, database.size());
        for (int i = 0; i < lim; i++) {
            Nom n = database.get(i);
            System.out.printf("    " + MG + "%4d" + R + "  %-35s  " + WH + "(%s)" + R + "\n",
                    n.getId(), n.getNomOriginal(), n.getSource());
        }
        if (database.size() > 20)
            System.out.println(YL + "    ... et " + (database.size()-20) + " autres." + R);
        pause();
    }

    // ── main ─────────────────────────────────────────────────────
    public static void main(String[] args) {
        while (true) {
            clear();
            banner();

            String statut = database == null
                    ? RD + "  Aucune liste chargée" + R
                    : GR + "  Liste : \"" + cheminCSV + "\"  (" + database.size() + " noms)" + R;
            System.out.println("  " + statut + "\n");

            int choix = menu("MENU PRINCIPAL",
                    "Charger une liste CSV",
                    "Lancer une recherche",
                    "Afficher la liste chargée",
                    "Quitter");

            switch (choix) {
                case 1: clear(); banner(); chargerCSV();     break;
                case 2: clear(); banner(); lanceRecherche(); break;
                case 3: clear(); banner(); afficherListe();  break;
                case 4:
                    System.out.println(CY + "\n  Au revoir !\n" + R);
                    sc.close();
                    return;
            }
        }
    }
}

