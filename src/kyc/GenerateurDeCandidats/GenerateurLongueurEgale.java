package kyc.GenerateurDeCandidats;

import kyc.model.Couple;
import kyc.model.Nom;

import java.util.ArrayList;
import java.util.List;

public class GenerateurLongueurEgale implements GenerateurDeCandidat {

    // tolerance : difference de longueur acceptee
    private int tolerance;

    public GenerateurLongueurEgale(int tolerance) {
        this.tolerance = tolerance;
    }

    public GenerateurLongueurEgale() {
        this(2);
    }

    @Override
    public List<Couple> generercandidat(List<Nom> requetes, List<Nom> candidats) {
        List<Couple> couples = new ArrayList<>();

        for (int i = 0; i < requetes.size(); i++) {
            Nom requete = requetes.get(i);
            int longueurRequete = requete.getNomOriginal().length();

            for (int j = 0; j < candidats.size(); j++) {
                Nom candidat = candidats.get(j);
                int longueurCandidat = candidat.getNomOriginal().length();

                int difference = longueurRequete - longueurCandidat;
                if (difference < 0) difference = -difference;

                if (difference <= tolerance) {
                    couples.add(new Couple(requete, candidat, 0.0));
                }
            }
        }

        return couples;
    }
}
