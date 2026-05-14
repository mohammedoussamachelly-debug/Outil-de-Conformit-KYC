package kyc.selection;

import kyc.model.Couple;
import kyc.model.Groupe;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopN implements Selectionneur {

    private int n;

    public SelectionneurTopN(int n) {
        this.n = n;
    }

    @Override
    public Groupe selectionner(List<Couple> candidats, double score) {
        Groupe groupe = new Groupe(score);
        if (candidats == null || candidats.isEmpty()) return groupe;

        Collections.sort(candidats, Comparator.comparingDouble(Couple::getScore).reversed());

        for (int i = 0; i < n && i < candidats.size(); i++) {
            groupe.ajouter(candidats.get(i));
        }
        return groupe;
    }
}

