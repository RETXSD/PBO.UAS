package APK;

import Database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import java.util.regex.*;
import javax.swing.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("BENGKEL SI - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700); // Ukuran frame lebih besar
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Judul aplikasi
        JLabel titleLabel = new JLabel("BENGKEL SI", JLabel.CENTER);
        titleLabel.setBounds(100, 20, 250, 40);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(titleLabel);

        // Nama lengkap
        JLabel nameLabel = new JLabel("Nama Lengkap:");
        nameLabel.setBounds(50, 80, 300, 25);
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(50, 110, 350, 30);
        panel.add(nameField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 300, 25);
        emailLabel.setForeground(Color.WHITE);
        panel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(50, 180, 350, 30);
        panel.add(emailField);

        JLabel emailWarningLabel = new JLabel("");
        emailWarningLabel.setBounds(50, 210, 350, 25);
        emailWarningLabel.setForeground(Color.RED);
        emailWarningLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(emailWarningLabel);

        // Nomor telepon
        JLabel phoneLabel = new JLabel("Nomor Telepon:");
        phoneLabel.setBounds(50, 240, 300, 25);
        phoneLabel.setForeground(Color.WHITE);
        panel.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(50, 270, 350, 30);
        panel.add(phoneField);

        // Kata sandi
        JLabel passwordLabel = new JLabel("Kata Sandi:");
        passwordLabel.setBounds(50, 310, 300, 25);
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 340, 350, 30);
        panel.add(passwordField);

        // Tombol daftar
        JButton registerButton = new JButton("DAFTAR");
        registerButton.setBounds(50, 400, 350, 40);
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(Color.BLUE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(registerButton);

        // Label untuk ke halaman login
        JLabel loginLabel = new JLabel("Masuk ke Halaman Login", JLabel.CENTER);
        loginLabel.setBounds(50, 460, 350, 25);
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(loginLabel);

        // Listener untuk tombol DAFTAR
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());

            // Validasi input
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi format email
            if (!isValidEmail(email)) {
                emailWarningLabel.setText("Email tidak valid! Harus berupa email Gmail.");
                return;
            } else {
                emailWarningLabel.setText("");
            }

            // Validasi format nomor telepon
            if (!isNumeric(phone)) {
                JOptionPane.showMessageDialog(this, "Nomor telepon harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Simpan data ke database
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO user(nama_lengkap, email, no_telepon, password) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, name);
                    statement.setString(2, email);
                    statement.setString(3, phone);
                    statement.setString(4, password);

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        new LoginFrame();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Pendaftaran gagal. Silakan coba lagi.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener untuk label login
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LoginFrame();
                dispose();
            }
        });

        // Tambahkan panel ke frame
        add(panel);
        setVisible(true);
    }

    // Validasi format email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    // Validasi hanya angka
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    public static void main(String[] args) {
        new RegisterFrame();
    }
}
