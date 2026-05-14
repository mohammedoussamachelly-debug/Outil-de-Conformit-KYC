package kyc.pretraitement;

import kyc.model.Nom;
import java.util.List;

public abstract class Pretraiteur {
    public abstract List<String> pretraiter(Nom a);
}
