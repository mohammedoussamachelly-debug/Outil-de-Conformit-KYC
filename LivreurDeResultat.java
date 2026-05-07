public class LivreurDeResultat {

    public void livrer(Groupe groupe) {
        System.out.println("Résultats (seuil: " + groupe.getSeuil() + "):");
        for (Couple couple : groupe.getCouples()) {
            System.out.println("  " + couple.getNom1().getNomOriginal()
                    + " -> " + couple.getNom2().getNomOriginal()
                    + " | score: " + couple.getScore());
        }
    }
}
