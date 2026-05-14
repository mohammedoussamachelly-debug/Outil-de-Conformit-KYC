package kyc.affichage;

import kyc.model.Couple;
import kyc.model.Groupe;
import kyc.model.Nom;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LivreurDeResultat {

    private static final String RESET  = "\033[0m";
    private static final String CYAN   = "\033[36m";
    private static final String GREEN  = "\033[32m";
    private static final String YELLOW = "\033[33m";
    private static final String RED    = "\033[31m";
    private static final String BOLD   = "\033[1m";

    private final String fichierCSV;
    private final FileWriter fw;

    public LivreurDeResultat() {
        this.fichierCSV = null;
        this.fw = null;
    }

    public LivreurDeResultat(String fichierCSV) {
        this.fichierCSV = fichierCSV;
        FileWriter temp = null;
        try {
            temp = new FileWriter(fichierCSV);
            temp.write("requete,resultat,score\n");
        } catch (IOException e) {
            System.out.println(RED + "  Erreur ouverture fichier : " + e.getMessage() + RESET);
        }
        this.fw = temp;
    }

    public List<Nom> livrer(Nom requete, Groupe groupe) {
        List<Couple> tries = new ArrayList<>(groupe.getCouples());
        tries.sort(Comparator.comparingDouble(Couple::getScore).reversed());

        List<Nom> resultat = new ArrayList<>();
        for (int i = 0; i < tries.size(); i++) {
            resultat.add(tries.get(i).getNom2());
        }

        if (fichierCSV != null) {
            ecrireDansCSV(requete, tries);
        } else {
            afficherDansTerminal(tries, groupe.getSeuil());
        }

        return resultat;
    }

    private void afficherDansTerminal(List<Couple> tries, double seuil) {
        System.out.println(CYAN + "┌──────┬────────────────────────────────────┬──────────┐" + RESET);
        System.out.printf(CYAN + "│" + BOLD + " %-4s " + CYAN + "│" + BOLD + " %-34s " + CYAN + "│" + BOLD + " %-8s " + CYAN + "│\n" + RESET,
                "#", "Nom (source CSV)", "Score");
        System.out.println(CYAN + "├──────┼────────────────────────────────────┼──────────┤" + RESET);

        if (tries.isEmpty()) {
            System.out.println(CYAN + "│" + RED + "  Aucun résultat trouvé.                                   " + CYAN + "│" + RESET);
        } else {
            int rang = 1;
            for (int i = 0; i < tries.size(); i++) {
                Couple c = tries.get(i);
                String couleur = c.getScore() >= 0.9 ? GREEN : c.getScore() >= 0.7 ? YELLOW : RED;
                System.out.printf(CYAN + "│" + couleur + " %-4d " + CYAN + "│" + RESET + " %-34s " + CYAN + "│" + couleur + " %-8.4f " + CYAN + "│\n" + RESET,
                        rang++, c.getNom2().getNomOriginal(), c.getScore());
            }
        }
        System.out.println(CYAN + "└──────┴────────────────────────────────────┴──────────┘" + RESET);
        System.out.printf(BOLD + "  %d résultat(s)  —  seuil : %.2f\n" + RESET, tries.size(), seuil);
    }

    private void ecrireDansCSV(Nom requete, List<Couple> tries) {
        if (fw == null) return;
        try {
            for (int i = 0; i < tries.size(); i++) {
                Couple c = tries.get(i);
                fw.write(requete.getNomOriginal() + "," + c.getNom2().getNomOriginal() + "," + String.format("%.4f", c.getScore()) + "\n");
            }
        } catch (IOException e) {
            System.out.println(RED + "  Erreur ecriture CSV : " + e.getMessage() + RESET);
        }
    }

    public void fermer() {
        if (fw != null) {
            try {
                fw.close();
                System.out.println(GREEN + "\n  ✔ Résultats sauvegardés dans : " + fichierCSV + RESET);
            } catch (IOException e) {
                System.out.println(RED + "  Erreur fermeture fichier : " + e.getMessage() + RESET);
            }
        }
    }
}
