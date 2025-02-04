package APK;

import Database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class FormResetSandi extends JFrame {
    public FormResetSandi(String username) {
        setTitle("BENGKEL SI - Reset Kata Sandi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Judul
        JLabel titleLabel = new JLabel("Reset Kata Sandi", JLabel.CENTER);
        titleLabel.setBounds(100, 20, 250, 40);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel);

        // Input Kata Sandi Baru
        JLabel newPasswordLabel = new JLabel("Kata Sandi Baru:");
        newPasswordLabel.setBounds(50, 80, 300, 25);
        newPasswordLabel.setForeground(Color.WHITE);
        panel.add(newPasswordLabel);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBounds(50, 110, 350, 30);
        panel.add(newPasswordField);

        // Input Konfirmasi Kata Sandi Baru
        JLabel confirmPasswordLabel = new JLabel("Konfirmasi Kata Sandi:");
        confirmPasswordLabel.setBounds(50, 150, 300, 25);
        confirmPasswordLabel.setForeground(Color.WHITE);
        panel.add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(50, 180, 350, 30);
        panel.add(confirmPasswordField);

        // Tombol Simpan
        JButton saveButton = new JButton("Simpan");
        saveButton.setBounds(50, 230, 350, 40);
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLUE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(saveButton);

        // Listener untuk tombol Simpan
        saveButton.addActionListener(e -> {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Validasi input
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Kata sandi tidak cocok!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update kata sandi di database
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE user SET password = ? WHERE nama_lengkap = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, newPassword);
                    statement.setString(2, username);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Kata sandi berhasil diperbarui!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        new LoginFrame();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal memperbarui kata sandi.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
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
        // Hanya untuk pengujian
        new FormResetSandi("testUsername");
    }
}
