package APK;

import Database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField emailOrPhoneField;
    private JPasswordField passwordField;
    public static String namaPelanggan;

    public LoginFrame() {
        setTitle("BENGKEL SI - Login"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700); 
        setLocationRelativeTo(null);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(62, 100, 170));
        panel.setLayout(null);

        // Nama aplikasi
        JLabel nama = new JLabel("BENGKEL SI ");
        nama.setBounds(125, 30, 200, 30);
        nama.setHorizontalAlignment(SwingConstants.CENTER);
        nama.setForeground(Color.WHITE);
        nama.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(nama);

        // Logo aplikasi
        JLabel logoLabel = new JLabel();
        logoLabel.setBounds(150, 80, 150, 150); // Posisi dan ukuran logo
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Memuat gambar
        ImageIcon logoIcon = new ImageIcon("src/resource/download.png"); 
        Image img = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); 
        logoIcon = new ImageIcon(img);
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("Logo not found");
            logoLabel.setForeground(Color.RED);
        }
        panel.add(logoLabel);

        // Email/Username Label dan Field
        JLabel emailOrPhoneLabel = new JLabel("Username:");
        emailOrPhoneLabel.setBounds(75, 250, 300, 20);
        emailOrPhoneLabel.setForeground(Color.WHITE);
        panel.add(emailOrPhoneLabel);

        emailOrPhoneField = new JTextField();
        emailOrPhoneField.setBounds(75, 275, 300, 30);
        panel.add(emailOrPhoneField);

        // Password Label dan Field
        JLabel passwordLabel = new JLabel("Kata Sandi:");
        passwordLabel.setBounds(75, 320, 300, 20);
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(75, 345, 300, 30);
        panel.add(passwordField);

        // Tombol Login
        JButton loginButton = new JButton("MASUK");
        loginButton.setBounds(75, 400, 300, 40);
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(new Color(0, 51, 153));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(e -> {
            String emailOrPhone = emailOrPhoneField.getText();
            String password = new String(passwordField.getPassword());
            if (isValidLogin(emailOrPhone, password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Login Berhasil!");
                new MainMenuFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Username atau Kata Sandi salah.");
            }
        });
        panel.add(loginButton);

        // Lupa Kata Sandi
        JLabel forgotPasswordLabel = new JLabel("Lupa Kata Sandi?");
        forgotPasswordLabel.setBounds(75, 460, 300, 20);
        forgotPasswordLabel.setForeground(Color.WHITE);
        forgotPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new FormLupaSandi();
                dispose();
            }
        });
        panel.add(forgotPasswordLabel);

        // Daftar Akun
        JLabel registerLabel = new JLabel("Tidak Punya Akun? Daftar");
        registerLabel.setBounds(75, 490, 300, 20);
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegisterFrame();
                dispose();
            }
        });
        panel.add(registerLabel);

        add(panel);
        setVisible(true);
    }

    private boolean isValidLogin(String username, String password) {
        boolean isValid = false;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT nama_lengkap FROM user WHERE nama_lengkap = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    namaPelanggan = rs.getString("nama_lengkap"); // Simpan nama pelanggan
                    isValid = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }
    public static void main(String[] args) {
        new LoginFrame();
    }
}
