import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class LoginApp {
    private static int loginAttempts = 0;
    private static JFrame frame;
    private static JTextField emailField;
    private static JPasswordField passwordField;
    private static JButton loginButton;
    private static JLabel messageLabel;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ime_baze"; // Zamijeni s pravim podacima
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginApp::showLoginForm);
    }

    private static void showLoginForm() {
        frame = new JFrame("Prijava");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(4, 2));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("PRIJAVI SE");
        messageLabel = new JLabel("");

        frame.add(new JLabel("Email:"));
        frame.add(emailField);
        frame.add(new JLabel("Lozinka:"));
        frame.add(passwordField);
        frame.add(new JLabel(""));
        frame.add(loginButton);
        frame.add(messageLabel);

        loginButton.addActionListener(e -> handleLogin());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE Email=? AND Password=?");
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int roleId = rs.getInt("RoleId");
                String name = rs.getString("FirstName") + " " + rs.getString("LastName");
                byte[] avatarImage = rs.getBytes("AvatarImage");

                if (loginAttempts < 3) {
                    openHomePage(roleId, name, avatarImage, conn);
                } else {
                    showCaptcha(email, password, roleId, name, avatarImage, conn);
                }
                return;
            }

            loginAttempts++;

            if (loginAttempts < 3) {
                messageLabel.setText("Prijava nije uspjela. Pokušajte ponovno.");
                emailField.setText("");
                passwordField.setText("");
            } else {
                emailField.setEnabled(false);
                passwordField.setEnabled(false);
                loginButton.setEnabled(false);
                messageLabel.setText("Daljnja prijava onemogućena, molimo obratite se administratoru.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void showCaptcha(String email, String password, int roleId, String name, byte[] avatarImage, Connection conn) {
        JFrame captchaFrame = new JFrame("Captcha - Mačke");
        captchaFrame.setSize(600, 400);
        captchaFrame.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel(new GridLayout(1, 3));
        List<JToggleButton> toggles = new ArrayList<>();
        List<String> catFiles = Arrays.asList("cat1.jpg", "dog1.jpg", "cat2.jpg"); // primjer
        Collections.shuffle(catFiles);

        for (String fileName : catFiles) {
            JToggleButton toggle = new JToggleButton(new ImageIcon("assets/proof-of-human/" + fileName));
            toggle.setActionCommand(fileName);
            toggles.add(toggle);
            imagePanel.add(toggle);
        }

        JButton doneButton = new JButton("GOTOVO");
        JLabel infoLabel = new JLabel("Označi sve slike koje sadrže mačku.", SwingConstants.CENTER);

        doneButton.addActionListener(e -> {
            boolean success = toggles.stream()
                    .filter(AbstractButton::isSelected)
                    .map(AbstractButton::getActionCommand)
                    .allMatch(name1 -> name1.contains("cat"))
                    && toggles.stream()
                    .filter(b -> !b.isSelected())
                    .map(AbstractButton::getActionCommand)
                    .noneMatch(name1 -> name1.contains("cat"));

            captchaFrame.dispose();

            if (success) {
                openHomePage(roleId, name, avatarImage, conn);
            } else {
                JOptionPane.showMessageDialog(null, "Niste uspjeli dokazati da ste ljudsko biće, molimo obratite se administratoru");
            }
        });

        captchaFrame.add(infoLabel, BorderLayout.NORTH);
        captchaFrame.add(imagePanel, BorderLayout.CENTER);
        captchaFrame.add(doneButton, BorderLayout.SOUTH);
        captchaFrame.setVisible(true);
    }

    private static void openHomePage(int roleId, String name, byte[] avatarImage, Connection conn) {
        frame.dispose();

        JFrame home = new JFrame("Dobrodošli");
        home.setSize(600, 400);
        home.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(name);
        JLabel avatarLabel = new JLabel();
        if (avatarImage != null) {
            avatarLabel.setIcon(new ImageIcon(avatarImage));
        }
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(avatarLabel, BorderLayout.EAST);
        home.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();

        switch (roleId) {
            case 1: // Admin
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT RoleId, COUNT(*) AS count FROM User GROUP BY RoleId");
                    JTextArea info = new JTextArea();
                    while (rs.next()) {
                        info.append("Role " + rs.getInt("RoleId") + ": " + rs.getInt("count") + " korisnika\n");
                    }
                    centerPanel.add(info);

                    JButton importBtn = new JButton("Importaj pacijente");
                    importBtn.addActionListener(ev -> {
                        JFileChooser chooser = new JFileChooser();
                        if (chooser.showOpenDialog(home) == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    String[] data = line.split(",");
                                    PreparedStatement ps = conn.prepareStatement(
                                            "INSERT INTO User (FirstName, LastName, Email, Password, RoleId, CreatedDate, UpdatedDate, AvatarImage) VALUES (?, ?, ?, ?, 3, NOW(), NOW(), 'none')");
                                    ps.setString(1, data[0]);
                                    ps.setString(2, data[1]);
                                    ps.setString(3, data[2]);
                                    ps.setString(4, data[3]);
                                    ps.executeUpdate();
                                }
                                JOptionPane.showMessageDialog(home, "Uspješno importano!");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    centerPanel.add(importBtn);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case 2: // Vlasnik klinike
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT ShortName FROM Clinic WHERE OwnerId = (SELECT Id FROM User WHERE CONCAT(FirstName, ' ', LastName) = ?)");
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        centerPanel.add(new JLabel(rs.getString("ShortName")));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case 3: // Pacijent
                centerPanel.add(new JLabel("//TODO"));
                break;
        }

        home.add(centerPanel, BorderLayout.CENTER);
        home.setLocationRelativeTo(null);
        home.setVisible(true);
    }
}
