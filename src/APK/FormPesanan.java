package APK;

import Parent.Layanan;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;

public class FormPesanan extends JFrame {
    private JLabel totalLabel;
    private Connection connection;
    private ArrayList<Layanan> pesananList;

    public FormPesanan(ArrayList<Layanan> pesananList, Connection connection) {
        this.pesananList = pesananList;
        this.connection = connection;

        setTitle("BENGKEL SI - Pesanan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Judul
        JLabel titleLabel = new JLabel("Pesanan Anda", JLabel.CENTER);
        titleLabel.setBounds(50, 30, 350, 30);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel);

        // Panel untuk daftar pesanan
        JTextArea pesananArea = new JTextArea();
        pesananArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        pesananArea.setEditable(false);
        pesananArea.setLineWrap(true);
        pesananArea.setWrapStyleWord(true);
        pesananArea.setBackground(new Color(240, 240, 240));
        pesananArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JScrollPane scrollPane = new JScrollPane(pesananArea);
        scrollPane.setBounds(50, 80, 350, 250);
        panel.add(scrollPane);

        // Label total harga
        totalLabel = new JLabel("", JLabel.CENTER);
        totalLabel.setBounds(50, 350, 350, 30);
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(totalLabel);

        // Label terima kasih
        JLabel thanksLabel = new JLabel("Terima kasih telah memesan!", JLabel.CENTER);
        thanksLabel.setBounds(50, 400, 350, 30);
        thanksLabel.setForeground(Color.YELLOW);
        thanksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(thanksLabel);

        // Tombol kembali
        JButton backButton = new JButton("Kembali");
        backButton.setBounds(50, 500, 150, 40);
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> {
            new FormLayanan();
            dispose();
        });
        panel.add(backButton);

        // Tombol selesai
        JButton selesaiButton = new JButton("Selesai");
        selesaiButton.setBounds(250, 500, 150, 40);
        selesaiButton.setBackground(new Color(0, 150, 0));
        selesaiButton.setForeground(Color.WHITE);
        selesaiButton.setFont(new Font("Arial", Font.BOLD, 16));
        selesaiButton.addActionListener(e -> {
            if (simpanPesanan(pesananList)) {
                JOptionPane.showMessageDialog(this, "Pesanan berhasil disimpan! Terima kasih atas pesanan Anda.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan pesanan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(selesaiButton);

        // Tambahkan panel ke frame
        add(panel);

        // Tampilkan daftar pesanan dan total
        displayPesanan(pesananList, pesananArea);
        setVisible(true);
    }

    private void displayPesanan(ArrayList<Layanan> pesananList, JTextArea pesananArea) {
        StringBuilder pesananText = new StringBuilder();
        int totalHarga = 0;
        int totalWaktu = 0;

        pesananText.append("=== Daftar Pesanan ===\n\n");

        for (int i = 0; i < pesananList.size(); i++) {
            Layanan layanan = pesananList.get(i);
            int harga = layanan.getHarga();
            int waktu = Integer.parseInt(layanan.getEstimasiWaktu().replace(" menit", "").trim());
            totalHarga += harga;
            totalWaktu += waktu;

            pesananText.append(i + 1).append(". ")
                       .append(layanan.getJenis().toUpperCase()).append("\n   Harga  : Rp ")
                       .append(harga).append("\n   Waktu  : ")
                       .append(waktu).append(" menit\n")
                       .append("=================================\n");
        }

        pesananArea.setText(pesananText.toString());

        totalLabel.setText("TOTAL : Rp " + totalHarga + " (" + totalWaktu + " menit)");
    }

    private boolean simpanPesanan(ArrayList<Layanan> pesananList) {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        if (LoginFrame.namaPelanggan == null || LoginFrame.namaPelanggan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        String sql = "INSERT INTO pesanan (id_pesanan, jenis, harga, estimasi_waktu, kategori, nama) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
    
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (Layanan layanan : pesananList) {
                    String idPesanan = generateIdPesanan();
                    
                    stmt.setString(1, idPesanan);
                    stmt.setString(2, layanan.getJenis());
                    stmt.setInt(3, layanan.getHarga());
                    stmt.setString(4, layanan.getEstimasiWaktu());
                    stmt.setString(5, getKategoriFromJenis(layanan.getJenis())); // Get kategori based on jenis
                    stmt.setString(6, LoginFrame.namaPelanggan);
    
                    int result = stmt.executeUpdate();
                    if (result != 1) {
                        throw new SQLException("Gagal menyimpan pesanan: " + layanan.getJenis());
                    }
                }
                
                connection.commit();
                return true;
                
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Gagal Menyimpan", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error transaksi database: " + e.getMessage(), 
                "Gagal Menyimpan", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Helper method to get kategori from jenis
    private String getKategoriFromJenis(String jenis) {
        try {
            String sql = "SELECT kategori FROM layanan WHERE jenis = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, jenis);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("kategori");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    private String generateIdPesanan() {
        String idPesanan = "";
        String tahun = String.valueOf(LocalDate.now().getYear());
        String sql = "SELECT COUNT(*) FROM pesanan WHERE id_pesanan LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "PSN" + tahun + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                idPesanan = String.format("PSN%s%04d", tahun, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idPesanan;
    }
}
