package kyc.selection;

import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopN implements Selectionneur {

    @Override
    public List<Couple> selectionner(Nom nom, List<Couple> candidats, int n) {
        if (candidats == null || candidats.isEmpty() || n <= 0) return new ArrayList<>();
        List<Couple> copies = new ArrayList<>(candidats);
        copies.sort(Comparator.comparingDouble(Couple::getScore).reversed());
        return new ArrayList<>(copies.subList(0, Math.min(n, copies.size())));
    }
}
