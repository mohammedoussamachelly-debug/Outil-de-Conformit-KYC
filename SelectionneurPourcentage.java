import java.util.ArrayList;
import java.util.List;

public class SelectionneurTopScore implements Selectionneur {

    public List<Couple> selectionner(Nom nom, List<Couple> candidats, int max) {
        List<Couple> resultat = new ArrayList<>();

        for (int i = 0; i < max && i < candidats.size(); i++) {
            Couple meilleur = candidats.get(i);
            for (int j = i + 1; j < candidats.size(); j++) {
                if (candidats.get(j).getScore() > meilleur.getScore()) {
                    meilleur = candidats.get(j);
                }
            }
            candidats.remove(meilleur);
            resultat.add(meilleur);
        }

        return resultat;
    }
}
