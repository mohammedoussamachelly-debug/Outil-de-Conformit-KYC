package kyc.model;

import java.util.ArrayList;
import java.util.List;

public class Groupe {
    private double seuil;
    private List<Couple> couples;

    public Groupe(double seuil) {
        this.seuil = seuil;
        this.couples = new ArrayList<>();
    }

    public void ajouter(Couple couple) { couples.add(couple); }
    public double getSeuil()           { return seuil; }
    public List<Couple> getCouples()   { return couples; }
}
