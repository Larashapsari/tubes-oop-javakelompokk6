package RESEPJAVA;
import java.util.ArrayList;
/**
 *
 * @author Debora
 */
public class ResepService {

    private ArrayList<Resep> daftarResep = new ArrayList<>();

    public void simpanResep(Resep resep) {
        daftarResep.add(resep);
    }

    public String bacaSemuaResep() {
        if (daftarResep.isEmpty()) {
            return "Belum ada resep tersimpan.";
        }

        StringBuilder sb = new StringBuilder();
        for (Resep r : daftarResep) {
            sb.append(r.toString());
        }
        return sb.toString();
    }
}
