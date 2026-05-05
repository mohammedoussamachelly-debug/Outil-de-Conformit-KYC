public class ComparateurEgalitExact extends ComparateurNom {
    @Override
    public double comparer(Nom a, Nom b) {
        if (a.getNom().equals(b.getNom())) {
            return true; // identiques
        } else {
            return false; // différents
        }
    }
}
