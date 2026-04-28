public class Configuration {
  
    private Pretraitement pretraitement;
    private Index indexeur;
    private ComparateurDeChaine comparateur;
    private GenerateurDeCandidat generateur;
    private Sectionneur sectionneur; 
    private double seuil;
    private int maxResultats; 


    public Configuration(Pretraitement pretraitement, Index indexeur, ComparateurDeChaine comparateur, GenerateurDeCandidat generateur, Sectionneur sectionneur, double seuil, int maxResultats) {
        this.pretraitement = pretraitement;
        this.indexeur = indexeur;
        this.comparateur = comparateur;
        this.generateur = generateur;
        this.sectionneur = sectionneur;
        this.seuil = seuil;
        this.maxResultats = maxResultats;
    }
  
    public Configuration() {
      this( new PretraitementNormalisation() , new IndexArbre() , new ComparateurLevenshtein() , new GenerateurPrefixe() , new SectionneurTopScore() , 0.8, 10 );
    }

    public Pretraitement getPretraitement() { 
        return pretraitement; }
    public Index getIndexeur() { 
        return indexeur; }
    public ComparateurDeChaine getComparateur(){ 
        return comparateur; }
    public GenerateurDeCandidat getGenerateur() {  
        return generateur; }
    public Sectionneur getSectionneur() {
        return sectionneur; }
    public double getSeuil(){ 
        return seuil; }
    public int getMaxResultats(){ 
        return maxResultats; }


     public void setPretraitement(Pretraitement p){ 
        this.pretraitement = p; }
    public void setIndexeur(Index i){ 
        this.indexeur = i; }
    public void setComparateur(ComparateurDeChaine c){
         this.comparateur = c; }
    public void setGenerateur(GenerateurDeCandidat g){
         this.generateur = g; }
    public void setSectionneur(Sectionneur s){
         this.sectionneur = s; }
    public void setSeuil(double s){ 
        this.seuil = s; }
    public void setMaxResultats(int m){ 
        this.maxResultats = m; }


}
