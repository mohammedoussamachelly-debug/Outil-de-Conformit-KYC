public class ComparateurEgalitExact implements ComparateurDeChaine {

    public double comparer(Nom a, Nom b) {
        if (a.getNom().equals(b.getNom())) {
            return 1.00; 
        } else {
            return 0.00; 
        }
    }
}
