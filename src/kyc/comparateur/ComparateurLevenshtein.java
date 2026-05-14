package kyc.comparateur;

public class ComparateurLevenshtein implements ComparateurDeChaine {

    @Override
    public double comparer(String s1, String s2) {
        int len1 = s1.length(), len2 = s2.length();
        if (len1 == 0 && len2 == 0) return 1.0;
        if (len1 == 0 || len2 == 0) return 0.0;

        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];
        for (int j = 0; j <= len2; j++) prev[j] = j;

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(prev[j] + 1, Math.min(curr[j - 1] + 1, prev[j - 1] + cost));
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }

        int dist = prev[len2];
        return 1.0 - (double) dist / Math.max(len1, len2);
    }
}
