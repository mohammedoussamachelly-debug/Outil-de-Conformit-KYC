import java.util.List;

public interface Selectionneur {
    List<Couple> selectionner(Nom nom, List<Couple> candidats, int max);
}
