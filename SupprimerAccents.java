import java.util.ArrayList;
import java.util.List;
public class SupprimerAccents extends Pretraiteur{
    public List<String> pretraiter(Nom a){
        for (int i=0;i<a.getNomPretraite().getlength();i++){
            a[i]=a[i].replaceAll("[à]", "a");
            a[i]=a[i].replaceAll("[é]", "e");
            a[i]=a[i].replaceAll("[è]", "e");
            a[i]=a[i].replaceAll("[ç]", "c");
            a[i]=a[i].replaceAll("[ù]", "u");
        }
        return Arrays.asList(a)

    }

}

