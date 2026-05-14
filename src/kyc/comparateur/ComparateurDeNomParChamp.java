
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

        double scoreAtoB = 0.0;
        for (int i = 0; i < champsA.size(); i++) {
            double best = 0.0;
            for (int j = 0; j < champsB.size(); j++) {
                double s = comparateurDeChaine.comparer(champsA.get(i), champsB.get(j));
                if (s > best) best = s;
            }
            scoreAtoB += best;
        }
        scoreAtoB /= champsA.size();

        double scoreBtoA = 0.0;
        for (int i = 0; i < champsB.size(); i++) {
            double best = 0.0;
            for (int j = 0; j < champsA.size(); j++) {
                double s = comparateurDeChaine.comparer(champsB.get(i), champsA.get(j));
                if (s > best) best = s;
            }
            scoreBtoA += best;
        }
        scoreBtoA /= champsB.size();

        return (scoreAtoB + scoreBtoA) / 2.0;
    }
}
