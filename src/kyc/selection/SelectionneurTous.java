package kyc.selection;

import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.List;

public class SelectionneurTous implements Selectionneur {

    @Override
    public List<Couple> selectionner(Nom nom, List<Couple> candidats, int max) {
        if (candidats == null || candidats.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(candidats);
    }
}
