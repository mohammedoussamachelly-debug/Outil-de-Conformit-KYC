package kyc.selection;

import kyc.model.Couple;
import kyc.model.Nom;
import java.util.List;

public interface Selectionneur {
    List<Couple> selectionner(Nom nom, List<Couple> candidats, int max);
}
