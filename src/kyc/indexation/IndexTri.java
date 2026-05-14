package kyc.indexation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexTri implements Index {

    private List<String> noms = new ArrayList<>();

    @Override
    public void indexer(List<String> noms) {
        this.noms = new ArrayList<>(noms);
        Collections.sort(this.noms, String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public List<String> getNoms() {
        return noms;
    }
}

