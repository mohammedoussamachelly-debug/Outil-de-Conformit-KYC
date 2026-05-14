package kyc.GenerateurDeCandidats;

import kyc.indexation.Index;
import kyc.model.Couple;
import kyc.model.Nom;
import java.util.*;

public class GenerateurPhonetique implements GenerateurDeCandidat {

    private Map<String, List<String>> codeCache = null;
    private Map<String, Nom> mapCandidats = null;
    private int lastIndexSize = 0;
    public List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats, Index index) {
        int capacity = (int) (candidats.size() * 1.34) + 1;
        if (mapCandidats == null || mapCandidats.size() != candidats.size()) {
            mapCandidats = new HashMap<>(capacity);
            List<String> strings = new ArrayList<>(candidats.size());
            for (int i = 0; i < candidats.size(); i++) {
                Nom c = candidats.get(i);
                String s = joinPretraite(c);
                strings.add(s);
                mapCandidats.put(s, c);
            }
            index.indexer(strings);
        }

        List<String> indexNoms = index.getNoms();
        if (indexNoms.isEmpty()) {
            List<String> strings = new ArrayList<>(mapCandidats.keySet());
            index.indexer(strings);
            indexNoms = index.getNoms();
        }

        if (codeCache == null || lastIndexSize != indexNoms.size()) {
            codeCache = new HashMap<>(capacity);
            for (int i = 0; i < indexNoms.size(); i++) {
                codeCache.put(indexNoms.get(i), soundexTokens(indexNoms.get(i)));
            }
            lastIndexSize = indexNoms.size();
        }

        List<Couple> resultats = new ArrayList<>();
        for (int i = 0; i < requetes.size(); i++) {
            Nom requete = requetes.get(i);
            if (requete == null || joinPretraite(requete).isEmpty()) continue;
            List<String> codesRequete = soundexTokens(joinPretraite(requete));
            for (int j = 0; j < indexNoms.size(); j++) {
                String nomStr = indexNoms.get(j);
                List<String> codesCandidat = codeCache.get(nomStr);
                if (codesCandidat != null && ontCodeCommun(codesRequete, codesCandidat)) {
                    Nom candidat = mapCandidats.get(nomStr);
                    if (candidat != null) {
                        resultats.add(new Couple(requete, candidat, 0.0));
                    }
                }
            }
        }
        return resultats;
    }

    private List<String> soundexTokens(String input) {
        List<String> codes = new ArrayList<>();
        String[] mots = input.split(" ");
        for (int i = 0; i < mots.length; i++) {
            if (!mots[i].isEmpty()) {
                codes.add(soundex(mots[i]));
            }
        }
        return codes;
    }

    private boolean ontCodeCommun(List<String> a, List<String> b) {
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (a.get(i).equals(b.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private String joinPretraite(Nom nom) {
        return String.join(" ", nom.getNomPretraite());
    }

    private String soundex(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String s = input.toUpperCase();
        StringBuilder code = new StringBuilder();
        code.append(s.charAt(0));

        for (int i = 1; i < s.length() && code.length() < 4; i++) {
            char c = s.charAt(i);
            int digit = soundexDigit(c);
            if (digit != 0 && digit != (code.charAt(code.length() - 1) - '0') && !(c == 'H' || c == 'W')) {
                code.append(digit);
            }
        }

        while (code.length() < 4) {
            code.append('0');
        }
        return code.toString();
    }

    private int soundexDigit(char c) {
        switch (c) {
            case 'B': case 'F': case 'P': case 'V': return 1;
            case 'C': case 'G': case 'J': case 'K': case 'Q': case 'S': case 'X': case 'Z': return 2;
            case 'D': case 'T': return 3;
            case 'L': return 4;
            case 'M': case 'N': return 5;
            case 'R': return 6;
            default: return 0;
        }
    }
}
