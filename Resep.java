package RESEPJAVA;
import java.util.ArrayList;
/**
 *
 * @author Debora
 */
public class Resep {

    private String namaResep;
    private String tipeMasakan;
    private ArrayList<String> bahan = new ArrayList<>();
    private ArrayList<String> langkah = new ArrayList<>();
    private double totalKalori = 0;

    public Resep(String namaResep, String tipeMasakan) {
        this.namaResep = namaResep;
        this.tipeMasakan = tipeMasakan;
    }

    public void tambahBahan(String input) {
        String nama = input.substring(0, input.indexOf("(")).trim();
        String kaloriStr = input.substring(
                input.indexOf("(") + 1,
                input.indexOf(")")
        );
        double kalori = Double.parseDouble(kaloriStr);
        bahan.add(nama);
        totalKalori += kalori;
    }

    public void tambahLangkah(String l) {
        langkah.add(l);
    }

    public double hitungKalori() {
        return totalKalori;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nama: ").append(namaResep).append("\n");
        sb.append("Tipe: ").append(tipeMasakan).append("\n");
        sb.append("Bahan:\n");
        for (String b : bahan) sb.append("- ").append(b).append("\n");
        sb.append("Langkah:\n");
        for (String l : langkah) sb.append("- ").append(l).append("\n");
        sb.append("Total Kalori: ").append(totalKalori).append(" kkal\n");
        sb.append("----------------------\n");
        return sb.toString();
    }
}
