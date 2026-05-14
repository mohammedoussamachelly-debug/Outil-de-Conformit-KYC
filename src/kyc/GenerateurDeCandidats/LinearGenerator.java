package kyc.GenerateurDeCandidats;

import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.List;

public class LinearGenerator implements GenerateurDeCandidat {

    @Override
    public List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats) {
        List<Couple> couples = new ArrayList<>();
        for (Nom requete : requetes)
            for (Nom candidat : candidats)
                couples.add(new Couple(requete, candidat, 0.0));
        return couples;
    }
}
