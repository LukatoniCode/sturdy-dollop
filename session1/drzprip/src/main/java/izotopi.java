import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class izotopi extends JFrame{
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JPanel panel;
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public izotopi(){
        conn = JavaConnect.connectDb();
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setTitle("IZOTOPI");
        setVisible(true);

        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAtom(g);
            }
        };
        add(drawingPanel, BorderLayout.CENTER);
    }

    private void drawAtom(Graphics g) {
        // Dummy values for protons, neutrons, electrons for demonstration.
        int protons = 6;  // Example number of protons
        int neutrons = 6;  // Example number of neutrons
        int electrons = 118;  // Example number of electrons

        // Draw the nucleus (circle containing protons and neutrons)
        g.setColor(Color.RED);
        g.fillOval(150, 150, 50, 50);  // Draw nucleus

        // Draw protons (red)
        for (int i = 0; i < protons; i++) {
            g.setColor(Color.RED);
            g.fillOval(150 + i * 10, 150 + i * 10, 10, 10);  // Draw each proton
        }

        // Draw neutrons (green)
        for (int i = 0; i < neutrons; i++) {
            g.setColor(Color.GREEN);
            g.fillOval(150 + i * 10, 150 - i * 10, 10, 10);  // Draw each neutron
        }

        // Draw the electron shell (circle)
        g.setColor(Color.BLUE);
        g.drawOval(100, 100, 200, 200);  // Draw electron shell

        // Draw electrons (blue)
        for (int i = 0; i < electrons; i++) {
            g.setColor(Color.BLUE);
            g.fillOval(100 + i * 15, 100 + i * 15, 10, 10);  // Draw each electron
        }
    }

    public static void main(String[] args) {
        new izotopi();
    }
}
