/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.resepmasakan;

/**
 *
 * @author Ayass
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.TitledBorder;

public class MainFrame extends JFrame {

    private JTextField tfNama, tfTipe;
    private JTextArea taBahan, taLangkah;
    private JButton btnSimpan, btnLihat;

    // TEMPAT SIMPAN RESEP 
    private ArrayList<String> daftarResep = new ArrayList<>();

    public MainFrame() {

        setTitle("Aplikasi Resep Masakan dan Nutrisi");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // PANEL INPUT 
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNama = new JTextField(20);
        tfTipe = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Resep"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Tipe Masakan"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfTipe, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        //PANEL BAHAN 
        taBahan = new JTextArea(6,30);
        taBahan.setLineWrap(true);
        taBahan.setWrapStyleWord(true);

        JPanel bahanPanel = new JPanel(new BorderLayout(5,5));
        bahanPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Bahan",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        JTextArea infoBahan = new JTextArea(
                "CATATAN\nFormat: nama(kalori)\nGunakan titik (.)\nContoh: Ayam (250.50)"
        );
        infoBahan.setEditable(false);
        infoBahan.setBackground(null);
        infoBahan.setFont(new Font("Times New Roman", Font.ITALIC, 10));
        infoBahan.setForeground(Color.DARK_GRAY);

        bahanPanel.add(new JScrollPane(taBahan), BorderLayout.CENTER);
        bahanPanel.add(infoBahan, BorderLayout.SOUTH);

        // PANEL LANGKAH
        taLangkah = new JTextArea(6,30);
        taLangkah.setLineWrap(true);
        taLangkah.setWrapStyleWord(true);

        JPanel langkahPanel = new JPanel(new BorderLayout(5,5));
        langkahPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Langkah",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        JTextArea infoLangkah = new JTextArea(
                "CATATAN\nGunakan ENTER untuk langkah berikutnya"
        );
        infoLangkah.setEditable(false);
        infoLangkah.setBackground(null);
        infoLangkah.setFont(new Font("Times New Roman", Font.ITALIC, 10));
        infoLangkah.setForeground(Color.DARK_GRAY);

        langkahPanel.add(new JScrollPane(taLangkah), BorderLayout.CENTER);
        langkahPanel.add(infoLangkah, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridLayout(2,1,10,10));
        centerPanel.add(bahanPanel);
        centerPanel.add(langkahPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // BUTTON
        btnSimpan = new JButton("Simpan Resep");
        btnLihat  = new JButton("Lihat Resep");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSimpan);
        btnPanel.add(btnLihat);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // EVENT 
        btnSimpan.addActionListener(e -> simpanResep());
        btnLihat.addActionListener(e -> lihatResep());
    }

    //SIMPAN RESEP 
    private void simpanResep() {

        // VALIDASI KOSONG
        if (tfNama.getText().trim().isEmpty() ||
            tfTipe.getText().trim().isEmpty() ||
            taBahan.getText().trim().isEmpty() ||
            taLangkah.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Wajib diisi semua",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // VALIDASI FORMAT LANGKAH 
        String teksLangkah = taLangkah.getText().trim();

        if (teksLangkah.contains(",") && !teksLangkah.contains("\n")) {
            JOptionPane.showMessageDialog(
                this,
                "Format langkah salah.\nGunakan ENTER untuk memisahkan setiap langkah",
                "Format Salah",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // VALIDASI FORMAT BAHAN & KALORI 
        Pattern pola = Pattern.compile("(.+)\\((.+)\\)");
        double totalKalori = 0;

        for (String baris : taBahan.getText().split("\n")) {
            Matcher m = pola.matcher(baris.trim());

            if (!m.matches()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Format bahan salah\nGunakan: nama(kalori)",
                        "Format Salah",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                totalKalori += Double.parseDouble(m.group(2));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Kalori harus angka dan pakai titik (.)",
                        "Input Salah",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        // SIMPAN RESEP 
        StringBuilder sb = new StringBuilder();
        sb.append("Nama Resep: ").append(tfNama.getText()).append("\n");
        sb.append("Tipe Masakan: ").append(tfTipe.getText()).append("\n");
        sb.append("Bahan:\n").append(taBahan.getText()).append("\n");
        sb.append("Langkah:\n").append(taLangkah.getText()).append("\n");
        sb.append("Total Kalori: ").append(totalKalori).append(" kkal\n");
        sb.append("----------------------------------\n");

        daftarResep.add(sb.toString());

        JOptionPane.showMessageDialog(
                this,
                "Resep berhasil disimpan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
        );

        tfNama.setText("");
        tfTipe.setText("");
        taBahan.setText("");
        taLangkah.setText("");
    }

    //LIHAT RESEP
    private void lihatResep() {

        if (daftarResep.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Belum ada resep tersimpan",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JTextArea area = new JTextArea();
        area.setEditable(false);

        for (String r : daftarResep) {
            area.append(r);
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450,400));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Daftar Resep",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
