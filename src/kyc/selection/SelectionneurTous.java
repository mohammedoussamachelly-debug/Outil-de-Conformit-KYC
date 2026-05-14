package kyc.selection;

import kyc.model.Couple;
import kyc.model.Groupe;
import java.util.List;

public class SelectionneurTous implements Selectionneur {

    @Override
    public Groupe selectionner(List<Couple> candidats, double score) {
        Groupe groupe = new Groupe(score);
        if (candidats == null || candidats.isEmpty()) return groupe;
        for (int i = 0; i < candidats.size(); i++) {
            groupe.ajouter(candidats.get(i));
        }
        return groupe;
    }
}

