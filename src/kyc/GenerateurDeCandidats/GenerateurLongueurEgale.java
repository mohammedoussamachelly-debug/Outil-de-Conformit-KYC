package kyc.GenerateurDeCandidats;

import kyc.indexation.Index;
import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateurLongueurEgale implements GenerateurDeCandidat {

    private int tolerance;

    public GenerateurLongueurEgale(int tolerance) {
        this.tolerance = tolerance;
    }

    public GenerateurLongueurEgale() {
        this.tolerance = 2;
    }

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
            Nom requete = requetes.get(i);
            int longueurRequete = joinPretraite(requete).length();
            for (int j = 0; j < indexNoms.size(); j++) {
                String s = indexNoms.get(j);
                Nom candidat = mapCandidats.get(s);
                if (candidat == null) continue;
                int difference = longueurRequete - s.length();
                if (difference < 0) {
                    difference = -difference;
                }
                if (difference <= tolerance) {
                    couples.add(new Couple(requete, candidat, 0.0));
                }
            }
        }
        return couples;
    }

    private String joinPretraite(Nom nom) {
        return String.join(" ", nom.getNomPretraite());
    }
}
