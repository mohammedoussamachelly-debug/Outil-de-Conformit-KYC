package kyc.selection;

import kyc.model.Couple;
import kyc.model.Groupe;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopPourcentage implements Selectionneur {

    private int pourcentage;

    public SelectionneurTopPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public Groupe selectionner(List<Couple> candidats, double score) {
        Groupe groupe = new Groupe(score);
        if (candidats == null || candidats.isEmpty()) return groupe;

        Collections.sort(candidats, Comparator.comparingDouble(Couple::getScore).reversed());

        int limite = (int) Math.ceil(candidats.size() * pourcentage / 100.0);
        for (int i = 0; i < limite && i < candidats.size(); i++) {
            groupe.ajouter(candidats.get(i));
        }
        return groupe;
    }
}


