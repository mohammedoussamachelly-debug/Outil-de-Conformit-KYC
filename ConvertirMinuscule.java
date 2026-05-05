import java.util.ArrayList;
import java.util.List;
public class ConvertMinuscule extends Pretraiteur{
    
    public String[] pretraiter(String[] a){
        for (int i = 0; i < a.length ; i++) {
            a[i]=a[i].toLowerCase();
        }
        return a;
}
}