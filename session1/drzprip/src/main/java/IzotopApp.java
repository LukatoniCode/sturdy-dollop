import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class IzotopApp extends JFrame {
    private JComboBox<Element> comboElement;
    private JComboBox<Izotop> comboIzotop;
    private JPanel drawingPanel;
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public IzotopApp() {
        conn = JavaConnect.connectDb();
        setTitle("Izotopi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        comboElement = new JComboBox<>();
        comboIzotop = new JComboBox<>();
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawIzotop(g);
            }
        };

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(new JLabel("Odaberite element:"));
        topPanel.add(comboElement);
        topPanel.add(new JLabel("Odaberite izotop:"));
        topPanel.add(comboIzotop);

        add(topPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);

        loadElementi();

        comboElement.addActionListener(e -> loadIzotopi());
        comboIzotop.addActionListener(e -> drawingPanel.repaint());

        setSize(600, 400);
        setVisible(true);
    }

    private void loadElementi() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:1433/Session1", "root", "Root");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT DISTINCT e.ElementId, e.PunoIme FROM Element e " +
                            "JOIN Izotop i ON e.ElementId = i.ElementId"
            );
            while (rs.next()) {
                comboElement.addItem(new Element(rs.getInt("ElementId"), rs.getString("PunoIme")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIzotopi() {
        comboIzotop.removeAllItems();
        Element selected = (Element) comboElement.getSelectedItem();
        if (selected == null) return;

        try (PreparedStatement stmt = conn.prepareStatement("SELECT IzotopId, BrojNeutrona, Naziv FROM Izotop WHERE ElementId = ?")) {
            stmt.setInt(1, selected.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboIzotop.addItem(new Izotop(
                        rs.getInt("IzotopId"),
                        rs.getInt("BrojNeutrona"),
                        rs.getString("Naziv"),
                        selected
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawIzotop(Graphics g) {
        Izotop izotop = (Izotop) comboIzotop.getSelectedItem();
        if (izotop == null) return;

        int boxSize = 12;
        int perRow = 40;

        int electrons = izotop.element.brojElektrona();
        int protons = izotop.element.brojProtona();
        int neutrons = izotop.brojNeutrona;

        // Omotač (elektroni - zeleno)
        g.setColor(Color.BLACK);
        g.drawString("Omotač (zeleno su prikazani elektroni)", 10, 20);
        g.setColor(Color.GREEN);
        for (int i = 0; i < electrons; i++) {
            int x = 10 + (i % perRow) * boxSize;
            int y = 30 + (i / perRow) * boxSize;
            g.fillRect(x, y, 10, 10);
        }

        // Jezgra (protoni i neutroni)
        int yOffset = 30 + ((electrons + perRow - 1) / perRow) * boxSize + 40;

        g.setColor(Color.BLACK);
        g.drawString("Jezgra (crveno su prikazani protoni, bijelo neutroni)", 10, yOffset - 10);

        // Protoni - crveno
        g.setColor(Color.RED);
        for (int i = 0; i < protons; i++) {
            int x = 10 + (i % perRow) * boxSize;
            int y = yOffset + (i / perRow) * boxSize;
            g.fillRect(x, y, 10, 10);
        }

        // Neutroni - bijelo
        g.setColor(Color.WHITE);
        for (int i = 0; i < neutrons; i++) {
            int x = 10 + ((protons + i) % perRow) * boxSize;
            int y = yOffset + ((protons + i) / perRow) * boxSize;
            g.fillRect(x, y, 10, 10);
        }

        // Optional: draw borders
        g.setColor(Color.BLACK);
        for (int i = 0; i < protons + neutrons; i++) {
            int x = 10 + (i % perRow) * boxSize;
            int y = yOffset + (i / perRow) * boxSize;
            g.drawRect(x, y, 10, 10);
        }
    }

    public static void main(String[] args) {
        new IzotopApp();
    }

    // Helper klase
    static class Element {
        int id;
        String ime;

        public Element(int id, String ime) {
            this.id = id;
            this.ime = ime;
        }

        public int brojElektrona() {
            try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:1433/Session1", "root", "Root");
                 PreparedStatement stmt = conn.prepareStatement("SELECT BrojElektrona FROM Element WHERE ElementId = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getInt(1);
            } catch (Exception e) { e.printStackTrace(); }
            return 0;
        }

        public int brojProtona() {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:1433/Session1", "root", "Root");
                 PreparedStatement stmt = conn.prepareStatement("SELECT BrojProtona FROM Element WHERE ElementId = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getInt(1);
            } catch (Exception e) { e.printStackTrace(); }
            return 0;
        }

        public String toString() {
            return ime;
        }
    }

    static class Izotop {
        int id;
        int brojNeutrona;
        String naziv;
        Element element;

        public Izotop(int id, int brojNeutrona, String naziv, Element element) {
            this.id = id;
            this.brojNeutrona = brojNeutrona;
            this.naziv = naziv;
            this.element = element;
        }

        public String toString() {
            return naziv;
        }
    }
}

