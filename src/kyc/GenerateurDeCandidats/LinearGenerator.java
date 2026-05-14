package kyc.GenerateurDeCandidats;

import kyc.indexation.Index;
import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinearGenerator implements GenerateurDeCandidat {

    @Override
    public List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats, Index index) {
        int capacity = (int) (candidats.size() * 1.34) + 1;
        Map<String, Nom> mapCandidats = new HashMap<>(capacity);
        List<String> strings = new ArrayList<>(candidats.size());
        for (int i = 0; i < candidats.size(); i++) {
            Nom c = candidats.get(i);
            String s = joinPretraite(c);
            strings.add(s);
            mapCandidats.put(s, c);
        }

        List<String> indexNoms = index.getNoms();
        if (indexNoms.isEmpty()) {
            index.indexer(strings);
            indexNoms = index.getNoms();
        }

        List<Couple> couples = new ArrayList<>();
        for (int i = 0; i < requetes.size(); i++) {
            for (int j = 0; j < indexNoms.size(); j++) {
                Nom candidat = mapCandidats.get(indexNoms.get(j));
                if (candidat != null) {
                    couples.add(new Couple(requetes.get(i), candidat, 0.0));
                }
            }
        }
        return couples;
    }

    private String joinPretraite(Nom nom) {
        return String.join(" ", nom.getNomPretraite());
    }
}
