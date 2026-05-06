public class ComparateurJaroWinkler implements ComparateurDeChaine {

    public double comparer(String nom1, String nom2) {
        return calculerJaroWinkler(nom1, nom2);
    }

    public double calculerJaroWinkler(String s1, String s2) {
        if (s1.equals(s2)) return 1.0;

        int len1 = s1.length();
        int len2 = s2.length();
        int matchDistance = Math.max(len1, len2) / 2 - 1;

        boolean[] s1Matches = new boolean[len1];
        boolean[] s2Matches = new boolean[len2];

        int matches = 0;
        int transpositions = 0;

        for (int i = 0; i < len1; i++) {
            int start = Math.max(0, i - matchDistance);
            int end = Math.min(i + matchDistance + 1, len2);
            for (int j = start; j < end; j++) {
                if (s2Matches[j] || s1.charAt(i) != s2.charAt(j)) continue;
                s1Matches[i] = true;
                s2Matches[j] = true;
                matches++;
                break;
            }
        }

        if (matches == 0) return 0.0;

        int k = 0;
        for (int i = 0; i < len1; i++) {
            if (!s1Matches[i]) continue;
            while (!s2Matches[k]) k++;
            if (s1.charAt(i) != s2.charAt(k)) transpositions++;
            k++;
        }

        double jaro = (matches / (double) len1
                     + matches / (double) len2
                     + (matches - transpositions / 2.0) / matches) / 3.0;

        int prefix = 0;
        for (int i = 0; i < Math.min(4, Math.min(len1, len2)); i++) {
            if (s1.charAt(i) == s2.charAt(i)) prefix++;
            else break;
        }

        return 
    }

    public boolean estSimilaire(double score, double seuil) {
        return score >= seuil;
    }
}
