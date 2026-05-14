package kyc.GenerateurDeCandidats;

import kyc.indexation.Index;
import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateurLettresCommunes implements GenerateurDeCandidat {

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
        List<String> indexNomsFinal = indexNoms;

        List<Couple> resultats = Collections.synchronizedList(new ArrayList<>());

        requetes.parallelStream().forEach(requete -> {
            String reqStr = joinPretraite(requete);
            if (reqStr.isEmpty()) return;

            boolean[] lettresRequete = new boolean[128];
            for (int i = 0; i < reqStr.length(); i++) {
                char ch = reqStr.charAt(i);
                if (ch < 128) lettresRequete[ch] = true;
            }

            for (int j = 0; j < indexNomsFinal.size(); j++) {
                String candStr = indexNomsFinal.get(j);
                if (candStr.isEmpty()) continue;
                boolean partage = false;
                for (int k = 0; k < candStr.length(); k++) {
                    char ch = candStr.charAt(k);
                    if (ch < 128 && lettresRequete[ch]) {
                        partage = true;
                        break;
                    }
                }
                if (partage) {
                    Nom candidat = mapCandidats.get(indexNomsFinal.get(j));
                    if (candidat != null) {
                        resultats.add(new Couple(requete, candidat, 0.0));
                    }
                }
            }
        });

        return resultats;
    }

    private String joinPretraite(Nom nom) {
        return String.join(" ", nom.getNomPretraite());
    }
}
