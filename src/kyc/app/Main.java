package kyc.app;

import kyc.Configuration.Configuration;
import kyc.MoteurDeRecherche.MoteurDeRecherche;
import kyc.indexation.Index;
import kyc.model.Nom;
import kyc.pretraitement.ConvertirMinuscule;
import kyc.pretraitement.SupprimerAccents;
import kyc.selection.Selectionneur;
import kyc.SourcesDeNoms.LecteurListeCSV;

import java.util.List;
import java.util.Scanner;

public class Main {

    static final String R  = "\033[0m";
    static final String B  = "\033[1m";
    static final String CY = "\033[36m";
    static final String GR = "\033[32m";
    static final String YL = "\033[33m";
    static final String MG = "\033[35m";
    static final String RD = "\033[31m";
    static final String WH = "\033[37m";

    static final Scanner sc = new Scanner(System.in);
    static List<Nom> database = null;
    static String cheminCSV = "data/noms.csv";

    public static void main(String[] args) {
        while (true) {
            System.out.println();
            System.out.println(CY + B + "  ╔══════════════════════════════════╗");
            System.out.println(       "  ║    OUTIL DE CONFORMITE KYC       ║");
            System.out.println(       "  ╚══════════════════════════════════╝" + R);
            if (database == null)
                System.out.println(RD + "  Aucune liste chargee" + R);
            else
                System.out.println(GR + "  Liste : \"" + cheminCSV + "\"  (" + database.size() + " noms)" + R);

            System.out.println();
            System.out.println(MG + "  1" + R + ". Charger une liste CSV");
            System.out.println(MG + "  2" + R + ". Lancer une recherche");
            System.out.println(MG + "  3" + R + ". Afficher la liste chargee");
            System.out.println(MG + "  4" + R + ". Quitter");
            System.out.print(GR + "\n  Votre choix : " + R);

            int choix = lireEntier(0);

            if      (choix == 1) chargerCSV();
            else if (choix == 2) lanceRecherche();
            else if (choix == 3) afficherListe();
            else if (choix == 4) { System.out.println(CY + "\n  Au revoir !\n" + R); return; }
            else System.out.println(RD + "  Choix invalide." + R);
        }
    }

    static void chargerCSV() {
        System.out.print(GR + "\n  Chemin du fichier CSV [" + cheminCSV + "] : " + R);
        String saisie = lireLigne();
        if (!saisie.isEmpty()) cheminCSV = saisie;

        try {
            database = new LecteurListeCSV(cheminCSV).lire();
            System.out.println(GR + "  OK : " + database.size() + " noms charges." + R);
        } catch (Exception e) {
            System.out.println(RD + "  Erreur : " + e.getMessage() + R);
            database = null;
        }
    }

    static void lanceRecherche() {
        if (database == null || database.isEmpty()) {
            System.out.println(RD + "\n  Chargez d'abord un fichier CSV." + R);
            return;
        }

        // Mode de recherche
        System.out.println("\n" + CY + "  Mode de recherche :" + R);
        System.out.println(MG + "  1" + R + ". Un seul nom");
        System.out.println(MG + "  2" + R + ". Une liste de noms (CSV)");
        System.out.print(GR + "  Choix [1] : " + R);
        int modeRecherche = lireEntier(1);

        List<Nom> requetes = new java.util.ArrayList<>();
        if (modeRecherche == 2) {
            System.out.print(GR + "\n  Chemin du CSV de requetes : " + R);
            String cheminRequetes = lireLigne();
            if (cheminRequetes.isEmpty()) return;
            try {
                requetes = new LecteurListeCSV(cheminRequetes).lire();
                System.out.println(GR + "  " + requetes.size() + " nom(s) de requete charges." + R);
                if (requetes.isEmpty()) return;
            } catch (Exception e) {
                System.out.println(RD + "  Erreur : " + e.getMessage() + R);
                return;
            }
        } else {
            System.out.print(GR + "\n  Nom a rechercher : " + R);
            String nomStr = lireLigne();
            if (nomStr.isEmpty()) return;
            requetes.add(new Nom(nomStr));
        }

        // Comparateur
        System.out.println("\n" + CY + "  Algorithme :" + R);
        System.out.println(MG + "  1" + R + ". Levenshtein");
        System.out.println(MG + "  2" + R + ". Jaro-Winkler");
        System.out.println(MG + "  3" + R + ". Egalite exacte");
        System.out.print(GR + "  Choix [1] : " + R);
        int choixComp = lireEntier(1);
        String typeComp = choixComp == 2 ? "JaroWinkler" : choixComp == 3 ? "Exact" : "Levenshtein";

        // Seuil
        System.out.print(GR + "  Seuil de similarite (0.0 - 1.0) [0.70] : " + R);
        double seuil = lireDouble(0.70);

        // Selectionneur
        System.out.println("\n" + CY + "  Selection :" + R);
        System.out.println(MG + "  1" + R + ". Top N");
        System.out.println(MG + "  2" + R + ". Top Pourcentage");
        System.out.println(MG + "  3" + R + ". Tous");
        System.out.print(GR + "  Choix [1] : " + R);
        int choixSel = lireEntier(1);

        Selectionneur selectionneur;
        int maxResultats = 10;

        if (choixSel == 2) {
            System.out.print(GR + "  Pourcentage (1-100) [50] : " + R);
            int pct = (int) lireDouble(50);
            selectionneur = Configuration.choisirSelectionneur("TopPourcentage", pct);
        } else if (choixSel == 3) {
            selectionneur = Configuration.choisirSelectionneur("Tous", 0);
        } else {
            System.out.print(GR + "  Nombre de resultats N [10] : " + R);
            maxResultats = (int) lireDouble(10);
            selectionneur = Configuration.choisirSelectionneur("TopN", maxResultats);
        }

        // Generateur
        System.out.println("\n" + CY + "  Generateur de candidats :" + R);
        System.out.println(MG + "  1" + R + ". Lineaire (brute-force)");
        System.out.println(MG + "  2" + R + ". Longueur egale");
        System.out.println(MG + "  3" + R + ". Prefixe");
        System.out.print(GR + "  Choix [1] : " + R);
        int choixGen = lireEntier(1);
        String typeGen = choixGen == 2 ? "LongueurEgale" : choixGen == 3 ? "Prefixe" : "Linear";

        // Index
        System.out.println("\n" + CY + "  Index :" + R);
        System.out.println(MG + "  1" + R + ". Dictionnaire");
        System.out.println(MG + "  2" + R + ". Tri");
        System.out.println(MG + "  3" + R + ". Arbre");
        System.out.print(GR + "  Choix [1] : " + R);
        int choixIndex = lireEntier(1);
        String typeIndex = choixIndex == 2 ? "Tri" : choixIndex == 3 ? "Arbre" : "Dictionnaire";
        Index index = Configuration.choisirIndex(typeIndex);

        // Pretraitement
        System.out.println("\n" + CY + "  Pretraitement :" + R);
        System.out.println(MG + "  1" + R + ". Convertir en minuscules");
        System.out.println(MG + "  2" + R + ". Supprimer les accents");
        System.out.print(GR + "  Choix [1] : " + R);
        boolean accents = lireEntier(1) == 2;

        // Sortie CSV ou terminal
        System.out.print(GR + "\n  Sauvegarder les résultats dans un fichier CSV ? (o/n) [n] : " + R);
        boolean versCSV = lireLigne().equalsIgnoreCase("o");
        String fichierCSV = null;
        if (versCSV) {
            System.out.print(GR + "  Nom du fichier [resultats.csv] : " + R);
            fichierCSV = lireLigne();
            if (fichierCSV.isEmpty()) fichierCSV = "resultats.csv";
        }

        Configuration config = new Configuration(
                accents ? new SupprimerAccents() : new ConvertirMinuscule(),
                index,
                Configuration.choisirComparateur(typeComp),
                Configuration.choisirGenerateur(typeGen),
                selectionneur, seuil, maxResultats);

        System.out.println();
        MoteurDeRecherche moteur = new MoteurDeRecherche(config, selectionneur, fichierCSV);
        if (modeRecherche == 2) {
            moteur.chercherListe(requetes, database);
        } else {
            moteur.chercher(requetes.get(0), database);
        }
    }

    static void afficherListe() {
        if (database == null || database.isEmpty()) {
            System.out.println(RD + "\n  Aucune liste chargee." + R);
            return;
        }
        System.out.println(CY + "\n  === LISTE (" + database.size() + " noms) ===" + R);
        int lim = Math.min(20, database.size());
        for (int i = 0; i < lim; i++) {
            Nom n = database.get(i);
            System.out.printf("  " + MG + "%4d" + R + "  %-35s  " + WH + "(%s)" + R + "\n",
                    n.getId(), n.getNomOriginal(), n.getSource());
        }
        if (database.size() > 20)
            System.out.println(YL + "  ... et " + (database.size() - 20) + " autres." + R);
    }

    static String lireLigne() {
        if (!sc.hasNextLine()) return "";
        return sc.nextLine().trim();
    }

    static int lireEntier(int defaut) {
        String ligne = lireLigne();
        if (ligne.isEmpty()) return defaut;
        try { return Integer.parseInt(ligne); }
        catch (NumberFormatException e) { return defaut; }
    }

    static double lireDouble(double defaut) {
        String ligne = lireLigne();
        if (ligne.isEmpty()) return defaut;
        try { return Double.parseDouble(ligne); }
        catch (NumberFormatException e) { return defaut; }
    }
}
