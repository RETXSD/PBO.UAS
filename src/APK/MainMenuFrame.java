package APK;

import java.awt.*;
import javax.swing.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("BENGKEL SI - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700); // Ukuran frame lebih besar
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Judul aplikasi
        JLabel titleLabel = new JLabel("Selamat Datang di BENGKEL SI", JLabel.CENTER);
        titleLabel.setBounds(50, 30, 350, 30);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);

        // Logo aplikasi
        JLabel logoLabel = new JLabel();
        logoLabel.setBounds(150, 80, 150, 150); // Posisi dan ukuran logo
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon logoIcon = new ImageIcon("src/resource/download.png"); // Pastikan jalur file benar
        Image img = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Menyesuaikan ukuran
        logoIcon = new ImageIcon(img);
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("Logo not found");
            logoLabel.setForeground(Color.RED);
        }
        panel.add(logoLabel);

        // Tombol layanan
        JButton layananButton = new JButton("Layanan");
        layananButton.setBounds(75, 250, 300, 40);
        layananButton.setBackground(Color.WHITE);
        layananButton.setForeground(Color.BLUE);
        layananButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(layananButton);
        layananButton.addActionListener(e -> {
            new FormLayanan(); // Membuka LayananFrame
            dispose();          // Menutup MainMenuFrame
        });

        // Tombol kembali ke LoginFrame
        JButton backButton = new JButton("Kembali");
        backButton.setBounds(75, 300, 300, 40);
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFocusPainted(false); // Optional: to remove focus border
        panel.add(backButton);

        backButton.addActionListener(e -> {
            new LoginFrame(); // Membuka LoginFrame
            dispose();        // Menutup MainMenuFrame
        });

        // Label informasi
        JLabel infoLabel = new JLabel("Alasan menggunakan jasa kami:");
        infoLabel.setBounds(75, 360, 300, 25);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(infoLabel);

        // Area informasi
        JTextArea infoArea = new JTextArea(
            "- Pelayanan Terbaik & Cepat\n" +
            "- Harga Murah\n" +
            "- Berpengalaman\n" +
            "- Banyak promo menarik"
        );
        infoArea.setBounds(75, 390, 300, 100);
        infoArea.setBackground(Color.WHITE);
        infoArea.setForeground(Color.BLACK);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setEditable(false);
        panel.add(infoArea);
 
        // Alamat
        JLabel addressLabel = new JLabel("Alamat Bengkel:");
        addressLabel.setBounds(75, 510, 300, 25);
        addressLabel.setForeground(Color.WHITE);
        addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(addressLabel);

        JTextArea addressArea = new JTextArea(
            "Kec. Pondok Gede\n" +
            "Kota Bekasi, Jawa Barat\n" +
            "Jl. Jatiwaringin RT 05/11, No. 130"
        );
        addressArea.setBounds(75, 540, 300, 75);
        addressArea.setBackground(Color.WHITE);
        addressArea.setForeground(Color.BLACK);
        addressArea.setFont(new Font("Arial", Font.PLAIN, 14));
        addressArea.setEditable(false);
        panel.add(addressArea);

        // Tambahkan panel ke frame
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenuFrame();
    }
}