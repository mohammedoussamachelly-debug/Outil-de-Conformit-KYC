package kyc.GenerateurDeCandidats;

import kyc.model.Couple;
import kyc.model.Nom;

import java.util.ArrayList;
import java.util.List;

public class GenerateurPrefixe implements GenerateurDeCandidat {

    // taille du prefixe a comparer
    private int taillePrefixe;

    public GenerateurPrefixe(int taillePrefixe) {
        this.taillePrefixe = taillePrefixe;
    }

    public GenerateurPrefixe() {
        this(3);
    }

    @Override
    public List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats) {
        List<Couple> couples = new ArrayList<>();

        for (int i = 0; i < requetes.size(); i++) {
            Nom requete = requetes.get(i);
            String nomRequete = requete.getNomOriginal().toLowerCase();

            int longueur = taillePrefixe;
            if (nomRequete.length() < longueur) {
                longueur = nomRequete.length();
            }
            String prefixeRequete = nomRequete.substring(0, longueur);

            for (int j = 0; j < candidats.size(); j++) {
                Nom candidat = candidats.get(j);
                String nomCandidat = candidat.getNomOriginal().toLowerCase();

                if (nomCandidat.startsWith(prefixeRequete)) {
                    couples.add(new Couple(requete, candidat, 0.0));
                }
            }
        }

        return couples;
    }
}
