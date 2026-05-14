package kyc.indexation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexArbre implements Index {

    private class Noeud {
        Map<Character, Noeud> enfants = new HashMap<>();
        List<String> noms = new ArrayList<>();
    }

    private Noeud racine = new Noeud();

    public void ajouterNom(String nom) {
        Noeud courant = racine;
        String nomMinuscule = nom.toLowerCase();
        for (int i = 0; i < nomMinuscule.length(); i++) {
            char c = nomMinuscule.charAt(i);
            if (courant.enfants.get(c) == null) {
                courant.enfants.put(c, new Noeud());
            }
            courant = courant.enfants.get(c);
        }
        courant.noms.add(nom);
    }

    public void collecterMotsNoeud(Noeud noeud, List<String> resultats) {
        for (int i = 0; i < noeud.noms.size(); i++) {
            resultats.add(noeud.noms.get(i));
        }
        for (Noeud enfant : noeud.enfants.values()) {
            collecterMotsNoeud(enfant, resultats);
        }
    }

    @Override
    public void indexer(List<String> noms) {
        for (int i = 0; i < noms.size(); i++) {
            ajouterNom(noms.get(i));
        }
    }

    @Override
    public List<String> getNoms() {
        List<String> resultats = new ArrayList<>();
        collecterMotsNoeud(racine, resultats);
        return resultats;
    }
}

