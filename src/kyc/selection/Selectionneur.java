
package kyc.selection;

import kyc.model.Couple;
import kyc.model.Groupe;
import java.util.List;

public interface Selectionneur {
    Groupe selectionner(List<Couple> candidats, double score);
}
