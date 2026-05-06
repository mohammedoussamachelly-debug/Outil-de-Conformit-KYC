public class ComparateurJaroWinkler implements ComparateurDeChaine {

    
    public double comparer(String nom1, String nom2) {
        if (nom1.equals(nom2)) return 1.0;
        if (nom1.isEmpty() || nom2.isEmpty()) return 0.0;
        int fenetre = Math.max(nom1.length(), nom2.length()) / 2 - 1;
        if (fenetre < 0) fenetre = 0;
        boolean[] dejaUtilise1 = new boolean[nom1.length()];
        boolean[] dejaUtilise2 = new boolean[nom2.length()];

        int lettresCommunes = 0;
        for (int i = 0; i < nom1.length(); i++) {
            int debut = Math.max(0, i - fenetre);
            int fin   = Math.min(i + fenetre + 1, nom2.length());

            for (int j = debut; j < fin; j++) {
                if (dejaUtilise2[j]) continue;
                if (nom1.charAt(i) != nom2.charAt(j)) continue;

                dejaUtilise1[i] = true;
                dejaUtilise2[j] = true;
                lettresCommunes++;
                break;
            }
        }
        if (lettresCommunes == 0) return 0.0;
        int transpositions = 0;
        int k = 0;
        for (int i = 0; i < nom1.length(); i++) {
            if (!dejaUtilise1[i]) continue;
            while (!dejaUtilise2[k]) k++;
            if (nom1.charAt(i) != nom2.charAt(k)) transpositions++;
            k++;
        }
        double jaro = calculerJaro(nom1, nom2, lettresCommunes, transpositions);

        int prefixe = 0;
        for (int i = 0; i < Math.min(4, Math.min(nom1.length(), nom2.length())); i++) {
            if (nom1.charAt(i) == nom2.charAt(i)) prefixe++;
            else break;
        }
        return jaro + prefixe * 0.1 * (1 - jaro);
    }

    public int calculerJaro(String s1, String s2, int lettresCommunes, int transpositions) {
        if (lettresCommunes == 0) return 0;

        double jaro = (
            (double) lettresCommunes / s1.length() +
            (double) lettresCommunes / s2.length() +
            (double) (lettresCommunes - transpositions / 2) / lettresCommunes
        ) / 3.0;

        return (int)(jaro * 100); 


    public boolean estSimilaire(double score, double seuil) {
        return score >= seuil;
    }
}
