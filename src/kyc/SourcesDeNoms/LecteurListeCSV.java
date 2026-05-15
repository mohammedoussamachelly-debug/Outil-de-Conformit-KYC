
package kyc.SourcesDeNoms;

import kyc.model.Nom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LecteurListeCSV {

    private final String cheminFichier;
    private final char separateur;

    public LecteurListeCSV(String cheminFichier, char separateur) {
        this.cheminFichier = cheminFichier;
        this.separateur = separateur;
    }

    public LecteurListeCSV(String cheminFichier) {
        this(cheminFichier, ',');
    }

    // CSV format: id,nom[,source]  — id may be a non-numeric string
    public List<Nom> lire() throws IOException {
        List<Nom> noms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            br.readLine(); // skip header
            int compteur = 0;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;
                String[] colonnes = ligne.split(String.valueOf(separateur));
                if (colonnes.length < 2) continue;
                String idOriginal = colonnes[0].trim();
                String nom        = colonnes[1].trim();
                String source     = colonnes.length >= 3 ? colonnes[2].trim() : "CSV";
                noms.add(new Nom(compteur++, idOriginal, nom, source));
            }
        }
        return noms;
    }
}
