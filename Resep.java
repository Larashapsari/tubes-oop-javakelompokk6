package Resep;

import InputKosongException.InputKosongException;
import java.util.ArrayList;

public class Resep {

    private String namaResep;
    private String tipeMasakan;
    private ArrayList<String> bahan;
    private ArrayList<String> langkah;
    private double totalKalori;

    public Resep(String namaResep, String tipeMasakan) throws InputKosongException {
        if (namaResep.trim().isEmpty() || tipeMasakan.trim().isEmpty()) {
            throw new InputKosongException("Nama resep dan tipe masakan tidak boleh kosong");
        }

        this.namaResep = namaResep;
        this.tipeMasakan = tipeMasakan;
        bahan = new ArrayList<>();
        langkah = new ArrayList<>();
    }

    public void tambahBahan(String inputBahan) throws Exception {
        if (inputBahan.trim().isEmpty())
            throw new InputKosongException("Bahan tidak boleh kosong");

        String nama = inputBahan.substring(0, inputBahan.indexOf("(")).trim();
        String kaloriStr = inputBahan.substring(
                inputBahan.indexOf("(") + 1,
                inputBahan.indexOf(")")
        ).trim();

        double kalori = Double.parseDouble(kaloriStr);
        bahan.add(nama);
        totalKalori += kalori;
    }

    public void tambahLangkah(String l) throws InputKosongException {
        if (l.trim().isEmpty())
            throw new InputKosongException("Langkah tidak boleh kosong");
        langkah.add(l);
    }

    public double hitungKalori() {
        return totalKalori;
    }

    public String getNamaResep() {
        return namaResep;
    }

    public void setNamaResep(String namaResep) {
        this.namaResep = namaResep;
    }

    public String getTipeMasakan() {
        return tipeMasakan;
    }

    public void setTipeMasakan(String tipeMasakan) {
        this.tipeMasakan = tipeMasakan;
    }

    public ArrayList<String> getBahan() {
        return bahan;
    }

    public void setBahan(ArrayList<String> bahan) {
        this.bahan = bahan;
    }

    public ArrayList<String> getLangkah() {
        return langkah;
    }

    public void setLangkah(ArrayList<String> langkah) {
        this.langkah = langkah;
    }

    public double getTotalKalori() {
        return totalKalori;
    }

    public void setTotalKalori(double totalKalori) {
        this.totalKalori = totalKalori;
    }
}