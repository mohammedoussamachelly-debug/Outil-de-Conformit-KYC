public abstract class Nom {
     private int id;
     private string nom_original;  
     private string[] nom_pretraité; 
     private string source;
     
     public Nom(int id, string nom_original, str[] nom_pretraité , string source) {
            this.id = id;
            this.nom_original = nom_original;
            this.nom_pretraité = nom_pretraité;
            this.source = source;


    }
    public int getId() {
        return id;
    }

    public string getNomOriginal() {
        return nom_original;
    }

    public str[] getNomPretraite() {
        return nom_pretraité;
    }

    public string getSource() {
        return source;
    }
    
    
