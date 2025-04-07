import javax.swing.*;
import java.sql.*;

public class Poc extends JFrame {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    private JPanel panle1;
    private JButton elementiButton;
    private JButton atmosferePlanetaButton;
    private JButton statistikaButton;

    public Poc() {
        setContentPane(panle1);
        setTitle("Početna");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);

        elementiButton.addActionListener(e -> openScreen("elementi"));
        atmosferePlanetaButton.addActionListener(e -> openScreen("atmosfere"));
        statistikaButton.addActionListener(e -> openScreen("statistika"));
    }

    private void openScreen(String site) {
        dispose();
        switch (site){
            case "elementi":
                new elementi();
                break;
            case "atmosfere":
                new atmosfere();
                break;
            case "statisika":
                //new statistika();
                break;
        }
    }

    public static void main(String[] args) {
        new Poc();
    }
}
