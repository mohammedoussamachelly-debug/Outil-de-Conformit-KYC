package kyc.indexation;

import java.util.List;

public interface Index {

    void indexer(List<String> noms);

    List<String> getNoms();
}

