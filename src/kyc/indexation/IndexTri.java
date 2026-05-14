package kyc.indexation;

import java.util.ArrayList;
import java.util.List;

public class IndexTri implements Index {

    private List<String> noms = new ArrayList<>();

    @Override
    public void indexer(List<String> noms) {
        this.noms = new ArrayList<>();
        for (int i = 0; i < noms.size(); i++) {
            this.noms.add(noms.get(i));
        }
        // tri par ordre alphabetique (bubble sort)
        for (int i = 0; i < this.noms.size() - 1; i++) {
            for (int j = 0; j < this.noms.size() - 1 - i; j++) {
                if (this.noms.get(j).compareToIgnoreCase(this.noms.get(j + 1)) > 0) {
                    String temp = this.noms.get(j);
                    this.noms.set(j, this.noms.get(j + 1));
                    this.noms.set(j + 1, temp);
                }
            }
        }
    }

    @Override
    public List<String> getNoms() {
        return noms;
    }

    @Override
    public List<String> rechercherParMotCle(String motCle) {
        List<String> resultats = new ArrayList<>();
        for (int i = 0; i < noms.size(); i++) {
            if (noms.get(i).toLowerCase().contains(motCle.toLowerCase())) {
                resultats.add(noms.get(i));
            }
        }
        return resultats;
    }

    @Override
    public List<String> rechercherParPrefixe(String prefixe) {
        List<String> resultats = new ArrayList<>();
        for (int i = 0; i < noms.size(); i++) {
            if (noms.get(i).toLowerCase().startsWith(prefixe.toLowerCase())) {
                resultats.add(noms.get(i));
            }
        }
        return resultats;
    }
}
