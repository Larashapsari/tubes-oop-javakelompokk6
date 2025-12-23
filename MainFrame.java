package MainFrame;

import Resep.Resep;
import ResepServis.ResepService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextField tfNama, tfTipe;
    private JTextArea taBahan, taLangkah, taDaftarResep;
    private JButton btnSimpan, btnLihatDaftar, btnKembali;
    private CardLayout cardLayout;
    private JPanel containerPanel;

    public MainFrame() {
        ResepService service = new ResepService();
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        setTitle("Aplikasi Resep Masakan dan Nutrisi");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
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

        taBahan = new JTextArea(5, 30);
        taBahan.setLineWrap(true);
        JScrollPane scrollBahan = new JScrollPane(taBahan);
        JPanel bahanPanel = new JPanel(new BorderLayout(5,5));
        bahanPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bahan:", TitledBorder.LEFT, TitledBorder.TOP));
        
        JTextArea infoBahan = new JTextArea("CATATAN\nFormat: nama(kalori)\nGunakan tanda titik (.) untuk desimal\nContoh: Ayam (250.50)");
        infoBahan.setEditable(false);
        infoBahan.setBackground(null);
        infoBahan.setFont(new Font("Times New Roman", Font.ITALIC, 11));
        infoBahan.setForeground(Color.DARK_GRAY);
        
        bahanPanel.add(scrollBahan, BorderLayout.CENTER);
        bahanPanel.add(infoBahan, BorderLayout.SOUTH);

        taLangkah = new JTextArea(5, 30);
        taLangkah.setLineWrap(true);
        JScrollPane scrollLangkah = new JScrollPane(taLangkah);
        JPanel langkahPanel = new JPanel(new BorderLayout(5,5));
        langkahPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Langkah:", TitledBorder.LEFT, TitledBorder.TOP));
        
        JTextArea infoLangkah = new JTextArea("CATATAN\nGunakan ENTER untuk langkah berikutnya.");
        infoLangkah.setEditable(false);
        infoLangkah.setBackground(null);
        infoLangkah.setFont(new Font("Times New Roman", Font.ITALIC, 11));
        infoLangkah.setForeground(Color.DARK_GRAY);
        
        langkahPanel.add(scrollLangkah, BorderLayout.CENTER);
        langkahPanel.add(infoLangkah, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(bahanPanel);
        centerPanel.add(langkahPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        btnSimpan = new JButton("Simpan Resep");
        btnLihatDaftar = new JButton("Lihat Daftar Resep");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSimpan);
        btnPanel.add(btnLihatDaftar);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        taDaftarResep = new JTextArea();
        taDaftarResep.setEditable(false);
        btnKembali = new JButton("Kembali ke Input");
        
        listPanel.add(new JLabel("=== DAFTAR RESEP TERSIMPAN ===", SwingConstants.CENTER), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(taDaftarResep), BorderLayout.CENTER);
        listPanel.add(btnKembali, BorderLayout.SOUTH);

        containerPanel.add(mainPanel, "INPUT");
        containerPanel.add(listPanel, "DAFTAR");
        add(containerPanel);

        btnSimpan.addActionListener(e -> simpanResep(service));
        btnLihatDaftar.addActionListener(e -> {
            updateTampilanDaftar(service);
            cardLayout.show(containerPanel, "DAFTAR");
        });
        btnKembali.addActionListener(e -> cardLayout.show(containerPanel, "INPUT"));
    }

    private void updateTampilanDaftar(ResepService service) {
        taDaftarResep.setText("");
        for (Resep r : service.getDaftarResep()) {
            taDaftarResep.append("NAMA: " + r.getNamaResep() + "\n");
            taDaftarResep.append("KALORI: " + r.hitungKalori() + " kkal\n");
            taDaftarResep.append("--------------------------------\n");
        }
    }

    private void simpanResep(ResepService service) {
        try {
            Resep resep = new Resep(tfNama.getText(), tfTipe.getText());
            for (String b : taBahan.getText().split("\n")) {
                if(!b.trim().isEmpty()) resep.tambahBahan(b.trim());
            }
            for (String l : taLangkah.getText().split("\n")) {
                if(!l.trim().isEmpty()) resep.tambahLangkah(l.trim());
            }
            service.simpanResep(resep);
            JOptionPane.showMessageDialog(this, "Resep Berhasil Disimpan!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}