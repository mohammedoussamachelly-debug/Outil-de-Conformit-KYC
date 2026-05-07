import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionneurTopN implements Selectionneur {
	public List<Triplet> selectionner(List<Triplet> triplets, int N) {
		if (triplets == null || triplets.isEmpty() || N <= 0) {
			return new ArrayList<>();
		}

		List<Triplet> copies = new ArrayList<>(triplets);
		Collections.sort(
			copies,
			Comparator.comparingDouble(Triplet::getScore)
				.thenComparingInt(Triplet::getScoreEntier)
				.reversed()
		);

		int limite = Math.min(N, copies.size());
		return new ArrayList<>(copies.subList(0, limite));
	}
}
