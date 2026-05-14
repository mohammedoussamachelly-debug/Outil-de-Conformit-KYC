package kyc.MoteurDeRecherche;

import kyc.affichage.LivreurDeResultat;
import kyc.comparateur.ComparateurDeNom;
import kyc.GenerateurDeCandidats.GenerateurDeCandidat;
import kyc.indexation.Index;
import kyc.model.Couple;
import kyc.model.Groupe;
import kyc.model.Nom;
import kyc.pretraitement.Pretraiteur;
import kyc.selection.Selectionneur;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MoteurDeRecherche {

    private List<Pretraiteur>    pretraiteurs;
    private ComparateurDeNom     comparateur;
    private GenerateurDeCandidat generateur;
    private Selectionneur        selectionneur;
    private LivreurDeResultat    livreur;
    private Index                index;
    private double               seuil;

    public MoteurDeRecherche(List<Pretraiteur> pretraiteurs,
                             ComparateurDeNom comparateur,
                             GenerateurDeCandidat generateur,
                             Selectionneur selectionneur,
                             LivreurDeResultat livreur,
                             Index index,
                             double seuil) {
        this.pretraiteurs  = pretraiteurs;
        this.comparateur   = comparateur;
        this.generateur    = generateur;
        this.selectionneur = selectionneur;
        this.livreur       = livreur;
        this.index         = index;
        this.seuil         = seuil;
    }

    private void pretraiter(Nom nom) {
        for (int i = 0; i < pretraiteurs.size(); i++) {
            pretraiteurs.get(i).pretraiter(nom);
        }
    }

    public List<Nom> chercher(Nom nom, List<Nom> database) {
        long debut = System.nanoTime();

        pretraiter(nom);

        List<Nom> requetes = new ArrayList<>();
        requetes.add(nom);

        List<Couple> candidats = generateur.generercandidat(requetes, database, index);

        List<Couple> scores = new ArrayList<>();
        for (int i = 0; i < candidats.size(); i++) {
            Couple c = candidats.get(i);
            double score = comparateur.comparer(c.getNom1(), c.getNom2());
            if (score >= seuil) {
                scores.add(new Couple(c.getNom1(), c.getNom2(), score));
            }
        }

        Groupe groupe = selectionneur.selectionner(scores, seuil);

        List<Nom> resultat = livreur.livrer(nom, groupe);
        livreur.fermer();

        long fin = System.nanoTime();
        double ms = (fin - debut) / 1_000_000.0;
        System.out.printf("\033[33m\n  Temps de recherche : %.2f ms\033[0m\n", ms);

        return resultat;
    }

    public Map<Nom, List<Nom>> chercherListe(List<Nom> requetes, List<Nom> database) {
        long debutTotal = System.nanoTime();

        Map<Nom, List<Nom>> tousResultats = new LinkedHashMap<>();
        int avecMatch = 0;
        int total = requetes.size();

        System.out.println("\033[36m\n  Traitement de " + total + " requete(s)...\033[0m");

        for (int i = 0; i < total; i++) {
            long debutReq = System.nanoTime();

            Nom req = requetes.get(i);
            pretraiter(req);

            System.out.printf("\033[35m\n  [%d/%d]\033[0m  %s\n", i + 1, total, req.getNomOriginal());

            List<Nom> reqListe = new ArrayList<>();
            reqListe.add(req);

            List<Couple> candidats = generateur.generercandidat(reqListe, database, index);

            List<Couple> scores = new ArrayList<>();
            for (int j = 0; j < candidats.size(); j++) {
                Couple c = candidats.get(j);
                double score = comparateur.comparer(c.getNom1(), c.getNom2());
                if (score >= seuil) {
                    scores.add(new Couple(c.getNom1(), c.getNom2(), score));
                }
            }

            Groupe groupe = selectionneur.selectionner(scores, seuil);

            List<Nom> res = livreur.livrer(req, groupe);
            tousResultats.put(req, res);
            if (!res.isEmpty()) {
                avecMatch++;
            }

            long finReq = System.nanoTime();
            double msReq = (finReq - debutReq) / 1_000_000.0;
            System.out.printf("\033[33m  Temps : %.2f ms\033[0m\n", msReq);
        }

        livreur.fermer();

        long finTotal = System.nanoTime();
        double msTotal = (finTotal - debutTotal) / 1_000_000.0;

        System.out.println("\033[1m\033[36m");
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.printf( "  ║  BILAN : %3d / %3d noms avec match           ║\n", avecMatch, total);
        System.out.printf( "  ║  Temps total : %8.2f ms  (moy : %6.2f ms)  ║\n", msTotal, msTotal / total);
        System.out.println("  ╚══════════════════════════════════════════════╝\033[0m");

        return tousResultats;
    }
}
