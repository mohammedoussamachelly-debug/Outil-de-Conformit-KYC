package kyc.pretraitement;

import kyc.model.Nom;
import java.util.List;

public class SupprimerAccents extends Pretraiteur {

    @Override
    public List<String> pretraiter(Nom a) {
        List<String> parts = a.getNomPretraite();
        parts.replaceAll(s -> s
            .replaceAll("[àâä]", "a")
            .replaceAll("[éêëè]", "e")
            .replaceAll("[ç]", "c")
            .replaceAll("[ùûü]", "u")
            .replaceAll("[îï]", "i")
            .replaceAll("[ôö]", "o"));
        return parts;
    }
}
