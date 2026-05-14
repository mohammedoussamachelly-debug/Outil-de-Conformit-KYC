package kyc.model;

public class Couple {
    private Nom nom1;
    private Nom nom2;
    private double score;

    public Couple(Nom nom1, Nom nom2, double score) {
        this.nom1 = nom1;
        this.nom2 = nom2;
        this.score = score;
    }

    public Nom getNom1()       { return nom1; }
    public Nom getNom2()       { return nom2; }
    public double getScore()   { return score; }
}
