package kyc.affichage;

import kyc.model.Couple;
import kyc.model.Groupe;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LivreurDeResultat {

    private static final String RESET  = "[0m";
    private static final String CYAN   = "[36m";
    private static final String GREEN  = "[32m";
    private static final String YELLOW = "[33m";
    private static final String RED    = "[31m";
    private static final String BOLD   = "[1m";

    public void fermer() {}

    public List<Nom> livrer(Nom nom, Groupe groupe) {
        List<Couple> tries = new ArrayList<>(groupe.getCouples());
        tries.sort(Comparator.comparingDouble(Couple::getScore).reversed());

        System.out.println(CYAN + "┌──────┬────────────────────────────────────┬──────────┐" + RESET);
        System.out.printf( CYAN + "│" + BOLD + " %-4s " + CYAN + "│" + BOLD + " %-34s " + CYAN + "│" + BOLD + " %-8s " + CYAN + "│\n" + RESET,
                "#", "Nom (source CSV)", "Score");
        System.out.println(CYAN + "├──────┼────────────────────────────────────┼──────────┤" + RESET);

        List<Nom> resultat = new ArrayList<>();
        if (tries.isEmpty()) {
            System.out.println(CYAN + "│" + RED + "  Aucun résultat trouvé.                                   " + CYAN + "│" + RESET);
        } else {
            int rang = 1;
            for (Couple c : tries) {
                Nom nomCSV = c.getNom2();
                resultat.add(nomCSV);
                String couleur = c.getScore() >= 0.9 ? GREEN : c.getScore() >= 0.7 ? YELLOW : RED;
                System.out.printf(CYAN + "│" + couleur + " %-4d " + CYAN + "│" + RESET + " %-34s " + CYAN + "│" + couleur + " %-8.4f " + CYAN + "│\n" + RESET,
                        rang++, nomCSV.getNomOriginal(), c.getScore());
            }
        }
        System.out.println(CYAN + "└──────┴────────────────────────────────────┴──────────┘" + RESET);
        System.out.printf(BOLD + "  %d résultat(s)  —  seuil : %.2f\n" + RESET,
                resultat.size(), groupe.getScore());
        return resultat;
    }
}

