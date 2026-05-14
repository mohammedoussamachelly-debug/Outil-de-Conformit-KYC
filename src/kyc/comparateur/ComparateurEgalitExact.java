package kyc.comparateur;

public class ComparateurEgalitExact implements ComparateurDeChaine {

    @Override
    public double comparer(String a, String b) {
        return a.equals(b) ? 1.0 : 0.0;
    }
}
