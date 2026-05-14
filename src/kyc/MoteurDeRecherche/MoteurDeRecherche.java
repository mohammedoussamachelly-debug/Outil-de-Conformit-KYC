package kyc.MoteurDeRecherche;

import kyc.affichage.LivreurDeResultat;
import kyc.Configuration.Configuration;
import kyc.GenerateurDeCandidats.GenerateurDeCandidat;
import kyc.model.Couple;
import kyc.model.Groupe;
import kyc.model.Nom;
import kyc.selection.Selectionneur;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MoteurDeRecherche {

    private final Configuration     config;
    private final Selectionneur     selectionneur;
    private final LivreurDeResultat livreur;

    public MoteurDeRecherche(Configuration config, Selectionneur selectionneur, String fichierCSV) {
        this.config        = config;
        this.selectionneur = selectionneur;
        this.livreur       = fichierCSV != null ? new LivreurDeResultat(fichierCSV) : new LivreurDeResultat();
    }

    public List<Nom> chercher(Nom nom, List<Nom> database) {
        config.getPretraiteur().pretraiter(nom);
        for (int i = 0; i < database.size(); i++) {
            config.getPretraiteur().pretraiter(database.get(i));
        }

        List<Nom> requetes = new ArrayList<>();
        requetes.add(nom);

        GenerateurDeCandidat generateur = config.getGenerateur();
        List<Couple> candidats = generateur.generercandidat(requetes, database);

        List<Couple> scores = new ArrayList<>();
        for (int i = 0; i < candidats.size(); i++) {
            Couple c = candidats.get(i);
            double score = config.getComparateur().comparer(c.getNom1(), c.getNom2());
            if (score >= config.getSeuil()) {
                scores.add(new Couple(c.getNom1(), c.getNom2(), score));
            }
        }

        List<Couple> selectionnes = selectionneur.selectionner(nom, scores, config.getMaxResultats());
        Groupe groupe = new Groupe(config.getSeuil());
        for (int i = 0; i < selectionnes.size(); i++) {
            groupe.ajouter(selectionnes.get(i));
        }

        List<Nom> resultat = livreur.livrer(nom, groupe);
        livreur.fermer();
        return resultat;
    }

    public Map<Nom, List<Nom>> chercherListe(List<Nom> requetes, List<Nom> database) {
        for (int i = 0; i < database.size(); i++) {
            config.getPretraiteur().pretraiter(database.get(i));
        }

        GenerateurDeCandidat generateur = config.getGenerateur();
        Map<Nom, List<Nom>> tousResultats = new LinkedHashMap<>();
        int avecMatch = 0;
        int total = requetes.size();

        System.out.println("\033[36m\n  Traitement de " + total + " requete(s)...\033[0m");

        for (int i = 0; i < total; i++) {
            Nom req = requetes.get(i);
            config.getPretraiteur().pretraiter(req);

            System.out.printf("\033[35m\n  [%d/%d]\033[0m  %s\n", i + 1, total, req.getNomOriginal());

            List<Nom> reqListe = new ArrayList<>();
            reqListe.add(req);
            List<Couple> candidats = generateur.generercandidat(reqListe, database);

            List<Couple> scores = new ArrayList<>();
            for (int j = 0; j < candidats.size(); j++) {
                Couple c = candidats.get(j);
                double score = config.getComparateur().comparer(c.getNom1(), c.getNom2());
                if (score >= config.getSeuil()) {
                    scores.add(new Couple(c.getNom1(), c.getNom2(), score));
                }
            }

            List<Couple> selectionnes = selectionneur.selectionner(req, scores, config.getMaxResultats());
            Groupe groupe = new Groupe(config.getSeuil());
            for (int j = 0; j < selectionnes.size(); j++) {
                groupe.ajouter(selectionnes.get(j));
            }

            List<Nom> res = livreur.livrer(req, groupe);
            tousResultats.put(req, res);
            if (!res.isEmpty()) avecMatch++;
        }

        livreur.fermer();

        System.out.println("\033[1m\033[36m");
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.printf( "  ║  BILAN : %3d / %3d noms avec match   ║\n", avecMatch, total);
        System.out.println("  ╚══════════════════════════════════════╝\033[0m");

        return tousResultats;
    }
}
