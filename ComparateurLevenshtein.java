public class ComparateurLevenshtein implements ComparateurDeChaine {

    public double comparer(Nom a, Nom b) {
        String s1 = a.getNom();
        String s2 = b.getNom();

        int longueur1 = s1.length();
        int longueur2 = s2.length();
        int[][] tableau = new int[longueur1 + 1][longueur2 + 1];
      
        for (int i = 0; i <= longueur1; i++) {
            tableau[i][0] = i;
        }

        for (int j = 0; j <= longueur2; j++) {
            tableau[0][j] = j;
        }

        for (int i = 1; i <= longueur1; i++) {
            for (int j = 1; j <= longueur2; j++) {

                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Les lettres sont pareilles → on prend la valeur diagonale
                    tableau[i][j] = tableau[i - 1][j - 1];
                } else {
                    // Les lettres sont différentes → on prend le minimum des 3 voisins + 1
                    int supprimer  = tableau[i - 1][j];
                    int inserer    = tableau[i][j - 1];
                    int remplacer  = tableau[i - 1][j - 1];
                    tableau[i][j]  = 1 + Math.min(supprimer, Math.min(inserer, remplacer));
                }
            }
        }
        int distance = tableau[longueur1][longueur2];

        int longueurMax = Math.max(longueur1, longueur2);
        return 1.0 - (double) distance / longueurMax;
    }
}
