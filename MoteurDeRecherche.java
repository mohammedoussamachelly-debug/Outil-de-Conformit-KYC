import java.util.Arrays;
import java.util.List;

public class MoteurDeRecherche {

    private Configuration config;
    private Selectionneur selectionneur;
    private LivreurDeResultat livreur;

    public MoteurDeRecherche(Configuration config) {
        this.config = config;
        this.selectionneur = config.getSectionneur();
        this.livreur = new LivreurDeResultat();
    }

    public MoteurDeRecherche(Configuration config, Selectionneur selectionneur) {
        this.config = config;
        this.selectionneur = selectionneur;
        this.livreur = new LivreurDeResultat();
    }

    public Groupe chercher(Nom nom, String typeGenerationDeCandidat) {
        config.getPretraitement().pretraiter(nom);
        GenerateurDeCandidat generateur = choisirGenerateur(typeGenerationDeCandidat);
        Couple[] candidats = generateur.generercandidat(new Nom[]{nom});
        List<Couple> selectionnes = selectionneur.selectionner(nom, Arrays.asList(candidats), config.getMaxResultats());
        Groupe groupe = new Groupe(config.getSeuil());
        for (Couple couple : selectionnes) {
            groupe.ajouter(couple);
        }
        livreur.livrer(groupe);
        return groupe;
    }

    private GenerateurDeCandidat choisirGenerateur(String type) {
        switch (type) {
            case "Phonetique": return new PhonetiqueGenerator();
            case "Linear":     return new LinearGenerator();
            default:           return config.getGenerateur();
        }
    }

}
