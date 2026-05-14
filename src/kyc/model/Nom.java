
package kyc.model;

import java.util.ArrayList;
import java.util.List;

public class Nom {

    private int id;
    private String idOriginal;
    private String nom_original;
    private List<String> nom_pretraite;
    private String source;

    public Nom(int id, String idOriginal, String nom_original, String source) {
        this.id = id;
        this.idOriginal = idOriginal;
        this.source = source;
        this.nom_pretraite = new ArrayList<>();
        this.nom_original = nom_original;
        String[] mots = nom_original.split(" ");
        for (int i = 0; i < mots.length; i++) {
            this.nom_pretraite.add(mots[i]);
        }
    }

    public Nom(int id, String nom_original, String source) {
        this(id, String.valueOf(id), nom_original, source);
    }

    public Nom(String nom_original) {
        this.id = -1;
        this.idOriginal = null;
        this.nom_original = nom_original;
        this.source = null;
        this.nom_pretraite = new ArrayList<>();
        String[] mots = nom_original.split(" ");
        for (int i = 0; i < mots.length; i++) {
            this.nom_pretraite.add(mots[i]);
        }
    }

    public int getId()                              { return id; }
    public String getIdOriginal()                  { return idOriginal; }
    public String getNomOriginal()                 { return nom_original; }
    public List<String> getNomPretraite()          { return nom_pretraite; }
    public void setNomPretraite(List<String> mots) { this.nom_pretraite = mots; }
    public String getSource()                      { return source; }
}
