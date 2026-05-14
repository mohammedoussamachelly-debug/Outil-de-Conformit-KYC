package kyc.Configuration;

import java.util.List;

public class Configuration {

    private List<String> pretraiteurs;
    private String       index;
    private String       comparateur;
    private String       generateur;
    private String       selectionneur;
    private int          paramSelection;
    private double       seuil;

    public Configuration(List<String> pretraiteurs, String index, String comparateur,
                         String generateur, String selectionneur,
                         int paramSelection, double seuil) {
        this.pretraiteurs   = pretraiteurs;
        this.index          = index;
        this.comparateur    = comparateur;
        this.generateur     = generateur;
        this.selectionneur  = selectionneur;
        this.paramSelection = paramSelection;
        this.seuil          = seuil;
    }

    public List<String> getPretraiteurs()   { return pretraiteurs; }
    public String       getIndex()          { return index; }
    public String       getComparateur()    { return comparateur; }
    public String       getGenerateur()     { return generateur; }
    public String       getSelectionneur()  { return selectionneur; }
    public int          getParamSelection() { return paramSelection; }
    public double       getSeuil()          { return seuil; }
}
