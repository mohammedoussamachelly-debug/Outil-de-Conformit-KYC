package kyc.model;

import java.util.Arrays;
import java.util.List;

public class Nom {
    private int id;
    private String nom_original;
    private List<String> nom_pretraite;
    private String source;

    public Nom(int id, String nom_original, String[] nom_pretraite, String source) {
        this.id = id;
        this.nom_original = nom_original;
        this.nom_pretraite = Arrays.asList(nom_pretraite);
        this.source = source;
    }

    public Nom(int id, String nom_original, String source) {
        this.id = id;
        this.nom_original = nom_original;
        this.nom_pretraite = Arrays.asList(nom_original.split(" "));
        this.source = source;
    }

    public Nom(String nom_original) {
        this.id = -1;
        this.nom_original = nom_original;
        this.nom_pretraite = Arrays.asList(nom_original.split(" "));
        this.source = null;
    }

    public int getId()                  { return id; }
    public String getNomOriginal()      { return nom_original; }
    public List<String> getNomPretraite() { return nom_pretraite; }
    public String getSource()           { return source; }
}
