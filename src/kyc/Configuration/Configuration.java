package kyc.Configuration;

import kyc.comparateur.ComparateurDeNom;
import kyc.comparateur.ComparateurDeNomParChamp;
import kyc.comparateur.ComparateurLevenshtein;
import kyc.comparateur.ComparateurJaroWinkler;
import kyc.comparateur.ComparateurEgalitExact;
import kyc.GenerateurDeCandidats.GenerateurDeCandidat;
import kyc.GenerateurDeCandidats.LinearGenerator;
import kyc.GenerateurDeCandidats.GenerateurLongueurEgale;
import kyc.GenerateurDeCandidats.GenerateurPrefixe;
import kyc.indexation.Index;
import kyc.indexation.IndexDictionnaire;
import kyc.indexation.IndexTri;
import kyc.indexation.IndexArbre;
import kyc.pretraitement.ConvertirMinuscule;
import kyc.pretraitement.Pretraiteur;
import kyc.selection.Selectionneur;
import kyc.selection.SelectionneurTopN;
import kyc.selection.SelectionneurTopPourcentage;
import kyc.selection.SelectionneurTous;

public class Configuration {

    private Pretraiteur        pretraitement;
    private Index              indexeur;
    private ComparateurDeNom   comparateur;
    private GenerateurDeCandidat generateur;
    private Selectionneur      selectionneur;
    private double             seuil;
    private int                maxResultats;

    public Configuration(Pretraiteur pretraitement, Index indexeur, ComparateurDeNom comparateur,
                         GenerateurDeCandidat generateur, Selectionneur selectionneur,
                         double seuil, int maxResultats) {
        this.pretraitement = pretraitement;
        this.indexeur      = indexeur;
        this.comparateur   = comparateur;
        this.generateur    = generateur;
        this.selectionneur = selectionneur;
        this.seuil         = seuil;
        this.maxResultats  = maxResultats;
    }

    public Configuration() {
        this(new ConvertirMinuscule(),
             new IndexDictionnaire(),
             new ComparateurDeNomParChamp(new ComparateurLevenshtein()),
             new LinearGenerator(),
             new SelectionneurTopN(),
             0.8, 10);
    }

    public Pretraiteur        getPretraiteur()  { return pretraitement; }
    public Index              getIndexeur()     { return indexeur; }
    public ComparateurDeNom   getComparateur()  { return comparateur; }
    public GenerateurDeCandidat getGenerateur() { return generateur; }
    public Selectionneur      getSelectionneur(){ return selectionneur; }
    public double             getSeuil()        { return seuil; }
    public int                getMaxResultats() { return maxResultats; }

    public void setPretraiteur(Pretraiteur p)        { this.pretraitement = p; }
    public void setIndexeur(Index i)                 { this.indexeur      = i; }
    public void setComparateur(ComparateurDeNom c)   { this.comparateur   = c; }
    public void setGenerateur(GenerateurDeCandidat g){ this.generateur    = g; }
    public void setSelectionneur(Selectionneur s)    { this.selectionneur = s; }
    public void setSeuil(double s)                   { this.seuil         = s; }
    public void setMaxResultats(int m)               { this.maxResultats  = m; }

    public static GenerateurDeCandidat choisirGenerateur(String type) {
        switch (type) {
            case "Linear":          return new LinearGenerator();
            case "LongueurEgale":   return new GenerateurLongueurEgale();
            case "Prefixe":         return new GenerateurPrefixe();
            default: throw new IllegalArgumentException("Generateur inconnu : " + type);
        }
    }

    public static Index choisirIndex(String type) {
        switch (type) {
            case "Dictionnaire": return new IndexDictionnaire();
            case "Tri":          return new IndexTri();
            case "Arbre":        return new IndexArbre();
            default: throw new IllegalArgumentException("Index inconnu : " + type);
        }
    }

    public static ComparateurDeNom choisirComparateur(String type) {
        switch (type) {
            case "Levenshtein":  return new ComparateurDeNomParChamp(new ComparateurLevenshtein());
            case "JaroWinkler":  return new ComparateurDeNomParChamp(new ComparateurJaroWinkler());
            case "Exact":        return new ComparateurDeNomParChamp(new ComparateurEgalitExact());
            default: throw new IllegalArgumentException("Comparateur inconnu : " + type);
        }
    }

    public static Selectionneur choisirSelectionneur(String type, int param) {
        switch (type) {
            case "TopN":          return new SelectionneurTopN();
            case "TopPourcentage":return new SelectionneurTopPourcentage(param);
            case "Tous":          return new SelectionneurTous();
            default: throw new IllegalArgumentException("Selectionneur inconnu : " + type);
        }
    }
}
