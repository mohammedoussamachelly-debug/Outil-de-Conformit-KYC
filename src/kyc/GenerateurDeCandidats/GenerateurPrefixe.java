package kyc.GenerateurDeCandidats;

import kyc.indexation.Index;
import kyc.model.Couple;
import kyc.model.Nom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateurPrefixe implements GenerateurDeCandidat {

    private int taillePrefixe;

    public GenerateurPrefixe(int taillePrefixe) {
        this.taillePrefixe = taillePrefixe;
    }

    public GenerateurPrefixe() {
        this.taillePrefixe = 3;
    }
    public List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats, Index index) {
        Map<String, List<Nom>> prefixMap = new HashMap<>(candidats.size() * 2);
        for (int i = 0; i < candidats.size(); i++) {
            Nom c = candidats.get(i);
            String s = joinPretraite(c);
            String key = s.length() >= taillePrefixe ? s.substring(0, taillePrefixe) : s;
            if (!key.isEmpty()) {
                prefixMap.computeIfAbsent(key, k -> new ArrayList<>()).add(c);
            }
        }

        List<Couple> couples = new ArrayList<>();
        for (int i = 0; i < requetes.size(); i++) {
            Nom requete = requetes.get(i);
            String nomRequete = joinPretraite(requete);
            int longueur = Math.min(taillePrefixe, nomRequete.length());
            if (longueur == 0) continue;
            String prefixe = nomRequete.substring(0, longueur);
            List<Nom> bucket = prefixMap.getOrDefault(prefixe, Collections.emptyList());
            for (int j = 0; j < bucket.size(); j++) {
                couples.add(new Couple(requete, bucket.get(j), 0.0));
            }
        }
        return couples;
    }

    private String joinPretraite(Nom nom) {
        return String.join(" ", nom.getNomPretraite());
    }
}
