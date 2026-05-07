import java.util.List;
import java.util.ArrayList;

public class SelectionneurTous implements Selectionneur {

    public List<Couple> selectionner(Nom nom, List<Couple> candidats, int max) {
        List<Couple> resultat = new ArrayList<>();
        for (int i = 0; i < max && i < candidats.size(); i++) {
            resultat.add(candidats.get(i));
        }
        return resultat;
    }
}
