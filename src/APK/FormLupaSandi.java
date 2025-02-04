package APK;

import Database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class FormLupaSandi extends JFrame {
    public FormLupaSandi() {
        setTitle("BENGKEL SI - Lupa Kata Sandi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Judul
        JLabel titleLabel = new JLabel("Lupa Kata Sandi", JLabel.CENTER);
        titleLabel.setBounds(100, 20, 250, 40);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel);

        // Input Username
        JLabel usernameLabel = new JLabel("masukan Username yang sudah anda daftarkan :");
        usernameLabel.setBounds(50, 80, 300, 25);
        usernameLabel.setForeground(Color.WHITE);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 110, 350, 30);
        panel.add(usernameField);

        // Input Email
        JLabel emailLabel = new JLabel("masukan Email yang sudah anda daftarkan :");
        emailLabel.setBounds(50, 150, 300, 25);
        emailLabel.setForeground(Color.WHITE);
        panel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(50, 180, 350, 30);
        panel.add(emailField);

        // Tombol Verifikasi
        JButton verifyButton = new JButton("Verifikasi");
        verifyButton.setBounds(50, 230, 350, 40);
        verifyButton.setBackground(Color.WHITE);
        verifyButton.setForeground(Color.BLUE);
        verifyButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(verifyButton);

        // Listener untuk tombol Verifikasi
        verifyButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();

            // Validasi input
            if (username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Periksa username dan email di database
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM user WHERE nama_lengkap = ? AND email = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);
                    statement.setString(2, email);

                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Verifikasi berhasil! Lanjut ke pengisian kata sandi baru.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        new FormResetSandi(username);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Username atau Email tidak cocok!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Tambahkan panel ke frame
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new FormLupaSandi();
    }
}
