package kyc.model;

import java.util.ArrayList;
import java.util.List;

public class Groupe {

    private double score;
    private List<Couple> couples;

    public Groupe(double score) {
        this.score   = score;
        this.couples = new ArrayList<>();
    }

    public void ajouter(Couple couple) {
        couples.add(couple);
    }

    public double       getScore()  { return score; }
    public List<Couple> getCouples(){ return couples; }
}
