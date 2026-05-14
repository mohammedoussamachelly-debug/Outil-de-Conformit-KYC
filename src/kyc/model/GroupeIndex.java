package kyc.model;

import java.util.ArrayList;
import java.util.List;

public class GroupeIndex {

    private String cle;
    private List<Nom> noms;

    public GroupeIndex(String cle) {
        this.cle  = cle;
        this.noms = new ArrayList<>();
    }

    public void ajouter(Nom nom) {
        noms.add(nom);
    }

    public String    getCle()  { return cle; }
    public List<Nom> getNoms() { return noms; }
}
