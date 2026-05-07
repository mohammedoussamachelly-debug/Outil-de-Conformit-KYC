import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopPourcentage implements Selectionneur {
    private final Configuration configuration;

    public SelectionneurTopPourcentage(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<Triplet> selectionner(List<Triplet> triplets) {
        int pourcentage = configuration == null ? 0 : configuration.getMaxResultats();
        if (triplets == null || triplets.isEmpty() || pourcentage <= 0) {
            return new ArrayList<>();
        }

        List<Triplet> copies = new ArrayList<>(triplets);
        Collections.sort(
            copies,
            Comparator.comparingDouble(Triplet::getScore)
                .thenComparingInt(Triplet::getScoreEntier)
                .reversed()
        );

        int borne = Math.min(100, pourcentage);
        int limite = (int) Math.ceil(copies.size() * (borne / 100.0));
        if (limite <= 0) {
            return new ArrayList<>();
        }

        return new ArrayList<>(copies.subList(0, limite));
    }
}
