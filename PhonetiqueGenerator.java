import java.util.ArrayList;
import java.util.List;

public class PhonetiqueGenerator extends GenerateurdeCandidat {

    public PhonetiqueGenerator() {
    }
    // We got this methode from a repo github but we understood its concept it converts a name into a short code based on how it sounds 
    private String soundex(String name) { 
        if (name == null || name.isEmpty()) return "";
        name = name.toUpperCase();
        char firstLetter = name.charAt(0);
        String digits = name.substring(1)
            .replaceAll("[AEIOUYHW]", "0")
            .replaceAll("[BFPV]", "1")
            .replaceAll("[CGJKQSXZ]", "2")
            .replaceAll("[DT]", "3")
            .replaceAll("[L]", "4")
            .replaceAll("[MN]", "5")
            .replaceAll("[R]", "6")
        StringBuilder sb = new StringBuilder();
        char prev = '0';
        for (char c : digits.toCharArray()) {
            if (c != prev) {
                sb.append(c);
                prev = c;
            }
        }
        String code = firstLetter + sb.toString().replaceAll("0", "");
        code = (code + "000").substring(0, 4);
        return code;
    }
    public Couple[] generercandidat(Nom[] noms) {
        return null;
    }
}
public Couple[] generercandidat(Nom[] noms) {
        List<Couple> couples = new ArrayList<>();
        for (int i = 0; i < noms.length - 1; i++) {
            for (int j = i + 1; j < noms.length; j++) {
                if Soundex(nom[i])==Soundex(nom[j]){
                    couples.add(new Couple(noms[i], noms[j]));
            }
            
            }
            
        couples.toArray(new Couple[0]);
        }

