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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.TitledBorder;

public class MainFrame extends JFrame {

    private JTextField tfNama;
    private JComboBox<String> cbTipe;
    private JTextArea taBahan, taLangkah;
    private JButton btnSimpan, btnLihat, btnKembali, btnEdit;
    private JPanel cards;
    private CardLayout cardLayout;

    private ArrayList<StringBuilder> daftarResep = new ArrayList<>();
    private int editIndex = -1; // indeks resep yang sedang diedit

    public MainFrame() {
        setTitle("Aplikasi Resep Masakan dan Nutrisi");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        JPanel inputPanelFull = buatPanelInputResep();
        JPanel lihatPanelFull = buatPanelLihatResep();

        cards.add(inputPanelFull, "InputResep");
        cards.add(lihatPanelFull, "LihatResep");

        add(cards);
        cardLayout.show(cards, "InputResep");
    }

    private JPanel buatPanelInputResep() {
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNama = new JTextField(20);
        cbTipe = new JComboBox<>(new String[]{"Nusantara", "Internasional"});

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Resep"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Tipe Masakan"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbTipe, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // PANEL BAHAN
        taBahan = new JTextArea(6,30);
        taBahan.setLineWrap(true);
        taBahan.setWrapStyleWord(true);
        JPanel bahanPanel = new JPanel(new BorderLayout(5,5));
        bahanPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Bahan", TitledBorder.LEFT, TitledBorder.TOP
        ));
        JTextArea infoBahan = new JTextArea(
                "CATATAN\nFormat: nama(kalori)\nGunakan titik (.) untuk desimal\nContoh: Ayam (250.50)"
        );
        infoBahan.setEditable(false); infoBahan.setBackground(null);
        infoBahan.setFont(new Font("Times New Roman", Font.ITALIC, 10));
        infoBahan.setForeground(Color.DARK_GRAY);
        bahanPanel.add(new JScrollPane(taBahan), BorderLayout.CENTER);
        bahanPanel.add(infoBahan, BorderLayout.SOUTH);

        // PANEL LANGKAH
        taLangkah = new JTextArea(6,30);
        taLangkah.setLineWrap(true); taLangkah.setWrapStyleWord(true);
        JPanel langkahPanel = new JPanel(new BorderLayout(5,5));
        langkahPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Langkah", TitledBorder.LEFT, TitledBorder.TOP
        ));
        JTextArea infoLangkah = new JTextArea(
                "CATATAN\nGunakan ENTER untuk langkah berikutnya"
        );
        infoLangkah.setEditable(false); infoLangkah.setBackground(null);
        infoLangkah.setFont(new Font("Times New Roman", Font.ITALIC, 10));
        infoLangkah.setForeground(Color.DARK_GRAY);
        langkahPanel.add(new JScrollPane(taLangkah), BorderLayout.CENTER);
        langkahPanel.add(infoLangkah, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridLayout(2,1,10,10));
        centerPanel.add(bahanPanel);
        centerPanel.add(langkahPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // BUTTON SIMPAN & LIHAT
        btnSimpan = new JButton("Simpan Resep");
        btnLihat = new JButton("Lihat Resep");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSimpan); btnPanel.add(btnLihat);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // EVENT
        btnSimpan.addActionListener(e -> simpanResep());
        btnLihat.addActionListener(e -> cardLayout.show(cards, "LihatResep"));

        return mainPanel;
    }

    private JPanel buatPanelLihatResep() {
        JPanel lihatPanel = new JPanel(new BorderLayout(10,10));
        lihatPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextArea area = new JTextArea(); area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        lihatPanel.add(scroll, BorderLayout.CENTER);

        btnKembali = new JButton("Kembali ke Input");
        btnEdit = new JButton("Edit Resep");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnEdit); btnPanel.add(btnKembali);
        lihatPanel.add(btnPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> cardLayout.show(cards, "InputResep"));

        // pilih resep index terakhir (sederhana) untuk diedit
        btnEdit.addActionListener(e -> {
            if (daftarResep.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Belum ada resep tersimpan");
                return;
            }
            String pilih = JOptionPane.showInputDialog(this,
                    "Masukkan nomor resep yang ingin diedit (1-"+daftarResep.size()+"):");
            try {
                int idx = Integer.parseInt(pilih)-1;
                if (idx<0 || idx>=daftarResep.size()) throw new NumberFormatException();
                editIndex = idx;
                StringBuilder r = daftarResep.get(idx);
                String[] lines = r.toString().split("\n");
                tfNama.setText(lines[0].replace("Nama Resep: ",""));
                cbTipe.setSelectedItem(lines[1].replace("Tipe Masakan: ",""));
                StringBuilder bahan = new StringBuilder();
                StringBuilder langkah = new StringBuilder();
                boolean bahanFlag=false, langkahFlag=false;
                for(String l : lines){
                    if(l.equals("Bahan:")) { bahanFlag=true; langkahFlag=false; continue; }
                    if(l.equals("Langkah:")) { bahanFlag=false; langkahFlag=true; continue; }
                    if(bahanFlag) bahan.append(l).append("\n");
                    if(langkahFlag) langkah.append(l).append("\n");
                }
                taBahan.setText(bahan.toString());
                taLangkah.setText(langkah.toString());
                cardLayout.show(cards, "InputResep");
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Input salah!");
            }
        });

        lihatPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                area.setText("");
                if (daftarResep.isEmpty()) area.setText("Belum ada resep tersimpan");
                else {
                    for(int i=0;i<daftarResep.size();i++){
                        area.append((i+1)+". "+daftarResep.get(i).toString()+"\n");
                    }
                }
            }
        });

        return lihatPanel;
    }

    private void validasiInput() throws InputKosongException{
        if(tfNama.getText().trim().isEmpty()) throw new InputKosongException("Nama resep wajib diisi!");
        if(taBahan.getText().trim().isEmpty()) throw new InputKosongException("Bahan wajib diisi!");
        if(taLangkah.getText().trim().isEmpty()) throw new InputKosongException("Langkah pembuatan wajib diisi!");
    }

    private void simpanResep(){
        try{
            validasiInput();
        }catch(InputKosongException e){
            JOptionPane.showMessageDialog(this,e.getMessage(),"Input Kosong",JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pattern pola = Pattern.compile("(.+)\\((.+)\\)");
        double totalKalori = 0;
        for(String baris: taBahan.getText().split("\n")){
            Matcher m = pola.matcher(baris.trim());
            if(!m.matches()){
                JOptionPane.showMessageDialog(this,"Format bahan salah! Gunakan nama(kalori)","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            try{ totalKalori += Double.parseDouble(m.group(2)); } 
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Kalori harus angka!","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Nama Resep: ").append(tfNama.getText()).append("\n");
        sb.append("Tipe Masakan: ").append(cbTipe.getSelectedItem().toString()).append("\n");
        sb.append("Bahan:\n").append(taBahan.getText()).append("\n");
        sb.append("Langkah:\n").append(taLangkah.getText()).append("\n");
        sb.append("Total Kalori: ").append(totalKalori).append(" kkal\n");
        sb.append("----------------------------------\n");

        if(editIndex>=0){
            daftarResep.set(editIndex, sb); // timpa resep lama
            editIndex = -1;
        } else {
            daftarResep.add(sb);
        }

        JOptionPane.showMessageDialog(this,"Resep berhasil disimpan!","Sukses",JOptionPane.INFORMATION_MESSAGE);

        tfNama.setText(""); cbTipe.setSelectedIndex(0); taBahan.setText(""); taLangkah.setText("");
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> { new MainFrame().setVisible(true); });
    }
}
