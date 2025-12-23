
import java.util.ArrayList;

public class ResepService {
    private ArrayList<Resep> daftarResep;

    public ResepService() {
        daftarResep = new ArrayList<>();
    }

    public void simpanResep(Resep resep) {
        daftarResep.add(resep);
    }

    public ArrayList<Resep> getDaftarResep() {
        return daftarResep;
    }
}