package kyc.pretraitement;

import kyc.model.Nom;
import java.util.List;

public class ConvertirMinuscule extends Pretraiteur {

    @Override
    public List<String> pretraiter(Nom a) {
        List<String> parts = a.getNomPretraite();
        parts.replaceAll(String::toLowerCase);
        return parts;
    }
}
