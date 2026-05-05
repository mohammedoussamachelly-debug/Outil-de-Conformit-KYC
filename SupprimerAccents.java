import java.util.ArrayList;
import java.util.List;
public class SupprimerAccents extends Pretraiteur{
    public String[] pretraiter(String[] a){
        for (int i=0;i<a.length;i++){
            a[i]=a[i].replaceAll("[à]", "a");
            a[i]=a[i].replaceAll("[é]", "e");
            a[i]=a[i].replaceAll("[è]", "e");
            a[i]=a[i].replaceAll("[ç]", "c");
            a[i]=a[i].replaceAll("[ù]", "u");
        }

        return a;
}

}
