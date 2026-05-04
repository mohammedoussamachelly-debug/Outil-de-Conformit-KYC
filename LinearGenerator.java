import java.util.ArrayList;
import java.util.List;

public class LinearGenerator extends GenerateurdeCandidat {
    public Couple[] generercandidat(Nom[] noms) {
        List<Couple> couples = new ArrayList<>();
        for (int i = 0; i < noms.length - 1; i++) {
            for (int j = i + 1; j < noms.length; j++) {
                couples.add(new Couple(noms[i], noms[j]));
            }
        }

        return couples.toArray(new Couple[0]);
    }
}