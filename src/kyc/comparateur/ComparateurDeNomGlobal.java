
package kyc.comparateur;

import kyc.model.Nom;

public class ComparateurDeNomGlobal implements ComparateurDeNom {

    private final ComparateurJaroWinkler jw = new ComparateurJaroWinkler();
    private final ComparateurLevenshtein lev = new ComparateurLevenshtein();

    public double comparer(Nom a, Nom b) {
        String strA = String.join(" ", a.getNomPretraite());
        String strB = String.join(" ", b.getNomPretraite());
        if (strA.isEmpty() || strB.isEmpty()) return 0.0;
        return (jw.comparer(strA, strB) + lev.comparer(strA, strB)) / 2.0;
    }
}
