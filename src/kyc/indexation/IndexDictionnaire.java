package kyc.indexation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexDictionnaire implements Index {

    private Map<String, List<String>> dictionnaire = new HashMap<>();
    private List<String> allNames = new ArrayList<>();

    @Override
    public void indexer(List<String> noms) {
        for (int i = 0; i < noms.size(); i++) {
            String nom = noms.get(i);
            allNames.add(nom);
            String cle = String.valueOf(nom.charAt(0)).toLowerCase();
            if (dictionnaire.get(cle) == null) {
                dictionnaire.put(cle, new ArrayList<>());
            }
            dictionnaire.get(cle).add(nom);
        }
    }

    @Override
    public List<String> getNoms() {
        return allNames;
    }

    public Map<String, List<String>> getDictionnaire() {
        return dictionnaire;
    }
}

