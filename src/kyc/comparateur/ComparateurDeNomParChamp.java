package kyc.comparateur;

import kyc.model.Nom;
import java.util.List;

public class ComparateurDeNomParChamp implements ComparateurDeNom {

    private final ComparateurDeChaine comparateurDeChaine;

    public ComparateurDeNomParChamp(ComparateurDeChaine comparateurDeChaine) {
        this.comparateurDeChaine = comparateurDeChaine;
    }
    public double comparer(Nom a, Nom b) {
        List<String> champsA = a.getNomPretraite();
        List<String> champsB = b.getNomPretraite();
        if (champsA.isEmpty() || champsB.isEmpty()) return 0.0;
        double total = 0.0;
        for (String champA : champsA) {
            double best = 0.0;
            for (String champB : champsB) {
                double score = comparateurDeChaine.comparer(champA, champB);
                if (score > best) best = score;
            }
            total += best;
        }
        return total / champsA.size();
    }
}
