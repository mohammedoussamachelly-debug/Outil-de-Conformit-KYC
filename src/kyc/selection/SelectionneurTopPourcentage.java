package kyc.selection;

import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopPourcentage implements Selectionneur {

    private final int pourcentage;

    public SelectionneurTopPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public List<Couple> selectionner(Nom nom, List<Couple> candidats, int max) {
        if (candidats == null || candidats.isEmpty()) return new ArrayList<>();
        List<Couple> copies = new ArrayList<>(candidats);
        copies.sort(Comparator.comparingDouble(Couple::getScore).reversed());
        int limite = (int) Math.ceil(copies.size() * (Math.min(100, pourcentage) / 100.0));
        return new ArrayList<>(copies.subList(0, Math.min(limite, copies.size())));
    }
}
