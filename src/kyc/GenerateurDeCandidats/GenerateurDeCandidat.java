package kyc.GenerateurDeCandidats;

import kyc.model.Couple;
import kyc.model.Nom;
import java.util.List;

public interface GenerateurDeCandidat {
    List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats);
}
