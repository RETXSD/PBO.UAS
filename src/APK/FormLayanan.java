package APK;

import Database.DatabaseConnection;
import Parent.Layanan;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class FormLayanan extends JFrame {
    private JComboBox<String> oliDropdown;
    private JComboBox<String> banDropdown;
    private JComboBox<String> serviceDropdown;
    private ArrayList<Layanan> pesananList = new ArrayList<>();
    private Connection connection;

    public FormLayanan() {
        // Koneksi database
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal koneksi ke database: " + e.getMessage());
            return;
        }

        setTitle("BENGKEL SI - Layanan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Label dan Dropdown untuk setiap kategori
        JLabel dropdownLabelOli = new JLabel("Pilih Oli:");
        dropdownLabelOli.setBounds(50, 30, 200, 30);
        dropdownLabelOli.setForeground(Color.WHITE);
        panel.add(dropdownLabelOli);

        oliDropdown = new JComboBox<>(getLayananByKategori("Oli"));
        oliDropdown.setBounds(50, 60, 350, 30);
        oliDropdown.setMaximumRowCount(4);
        panel.add(oliDropdown);

        JLabel dropdownLabelBan = new JLabel("Pilih Ganti Ban:");
        dropdownLabelBan.setBounds(50, 160, 200, 30);
        dropdownLabelBan.setForeground(Color.WHITE);
        panel.add(dropdownLabelBan);

        banDropdown = new JComboBox<>(getLayananByKategori("Ban"));
        banDropdown.setBounds(50, 190, 350, 30);
        banDropdown.setMaximumRowCount(4);
        panel.add(banDropdown);

        JLabel dropdownLabelService = new JLabel("Pilih Jasa Service:");
        dropdownLabelService.setBounds(50, 290, 200, 30);
        dropdownLabelService.setForeground(Color.WHITE);
        panel.add(dropdownLabelService);

        serviceDropdown = new JComboBox<>(getLayananByKategori("Service"));
        serviceDropdown.setBounds(50, 320, 350, 30);
        serviceDropdown.setMaximumRowCount(4);
        panel.add(serviceDropdown);

        // Tombol Tambah
        JButton tambahOliButton = new JButton("Tambah Oli");
    tambahOliButton.setBounds(50, 100, 120, 30);
    tambahOliButton.setBackground(new Color(0, 150, 0));
    tambahOliButton.setForeground(Color.WHITE);
    panel.add(tambahOliButton);

    JButton tambahBanButton = new JButton("Tambah Ban");
    tambahBanButton.setBounds(50, 230, 120, 30);
    tambahBanButton.setBackground(new Color(0, 150, 0));
    tambahBanButton.setForeground(Color.WHITE);
    panel.add(tambahBanButton);

    JButton tambahServiceButton = new JButton("Tambah Service");
    tambahServiceButton.setBounds(50, 360, 120, 30);
    tambahServiceButton.setBackground(new Color(0, 150, 0));
    tambahServiceButton.setForeground(Color.WHITE);
    panel.add(tambahServiceButton);

    // Add action listeners for each button
    tambahOliButton.addActionListener(e -> {
        String selectedOli = (String) oliDropdown.getSelectedItem();
        if (selectedOli != null && !isLayananInPesanan(selectedOli)) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tambahkan " + selectedOli + " ke pesanan?", 
                "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tambahPesanan(selectedOli);
            }
        }
    });

    tambahBanButton.addActionListener(e -> {
        String selectedBan = (String) banDropdown.getSelectedItem();
        if (selectedBan != null && !isLayananInPesanan(selectedBan)) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tambahkan " + selectedBan + " ke pesanan?", 
                "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tambahPesanan(selectedBan);
            }
        }
    });

    tambahServiceButton.addActionListener(e -> {
        String selectedService = (String) serviceDropdown.getSelectedItem();
        if (selectedService != null && !isLayananInPesanan(selectedService)) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tambahkan " + selectedService + " ke pesanan?", 
                "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tambahPesanan(selectedService);
            }
        }
    });
        

        // Tombol Pesan
        JButton pesanButton = new JButton("Pesan");
        pesanButton.setBounds(150, 500, 120, 40);
        pesanButton.setBackground(new Color(0, 100, 255));
        pesanButton.setForeground(Color.WHITE);
        pesanButton.addActionListener(e -> {
            if (pesananList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap tambahkan layanan sebelum memesan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                new FormPesanan(pesananList, connection);
                dispose();
            }
        });
        panel.add(pesanButton);

        add(panel);
        setVisible(true);
    }

    private String[] getLayananByKategori(String kategori) {
        ArrayList<String> layananList = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT jenis FROM layanan WHERE kategori = ?");
            pstmt.setString(1, kategori);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                layananList.add(rs.getString("jenis"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil layanan: " + e.getMessage());
        }
        return layananList.toArray(new String[0]);
    }

    private void tambahPesanan(String jenis) {
        // Debugging: cek layanan yang ditambahkan
        System.out.println("Layanan yang ditambahkan: " + jenis);

        if (jenis != null) {
            try {
                PreparedStatement pstmt = connection.prepareStatement("SELECT jenis, harga, estimasi_waktu FROM layanan WHERE jenis = ?");
                pstmt.setString(1, jenis);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    pesananList.add(new Layanan(rs.getString("jenis"), rs.getInt("harga"), rs.getString("estimasi_waktu")));
                    System.out.println("Layanan berhasil dimasukkan ke pesanan.");
                } else {
                    JOptionPane.showMessageDialog(this, "Layanan tidak ditemukan di database.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan layanan: " + e.getMessage());
            }
        }
    }

    private boolean isLayananInPesanan(String jenis) {
        for (Layanan layanan : pesananList) {
            if (layanan.getJenis().equals(jenis)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        new FormLayanan();
    }
}