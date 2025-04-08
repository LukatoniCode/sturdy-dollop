import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;

public class atmosfere extends JFrame{
    private JComboBox comboBox1;
    private JButton odabirButton;
    private JButton importButton;
    private JTable table1;
    private JPanel ea;
    private JLabel od;
    private File odabranaDatoteka = null;
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    JFrame f = new JFrame();
    int i = 0;

    public atmosfere(){
        conn =  JavaConnect.connectDb();
        setContentPane(ea);
        setTitle("Atmosfere");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);

        ucitajPlanete();

        comboBox1.addActionListener(e -> prikaziAtmosferu());

        odabirButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                odabranaDatoteka = fileChooser.getSelectedFile();
                od.setText("Odabrana datoteka: " + odabranaDatoteka.getAbsolutePath());
            }
        });
        importButton.addActionListener(e -> {
            if (odabranaDatoteka != null) {
                importajCSV(odabranaDatoteka);
            } else {
                JOptionPane.showMessageDialog(this, "Prvo odaberite CSV datoteku.");
            }
        });

    }

    private void importajCSV(File file) {
        int idp;
        Integer ide;
        String[] polje = new String[4];
        try {
            Scanner sc = new Scanner(file);

            if(sc.hasNextLine()) sc.nextLine();

            while (sc.hasNextLine()){
                i++;
                polje = sc.nextLine().split(";");

                idp = getOrInsertPlanet(conn, polje[0]);

                ide = getElementId(conn, polje[2]);

                if(ide == null){
                    ide = getOrInsertSpoj(conn, polje[2]);
                    if(!existsInAtmosfera(conn, idp, null, ide)){
                        System.out.println(existsInAtmosfera(conn, idp, null, ide));
                        if (polje.length < 4){
                            insertIntoAtmosfera(conn, idp, null, ide, null);
                        }else {
                            insertIntoAtmosfera(conn, idp, null, ide, Float.valueOf(polje[3]));
                        }

                        i++;
                    }
                }else {
                    if(!existsInAtmosfera(conn, idp, ide, null)){
                        System.out.println(existsInAtmosfera(conn, idp, ide, null));
                        if (polje.length < 4){
                            insertIntoAtmosfera(conn, idp, ide, null, null);
                        }else {
                            insertIntoAtmosfera(conn, idp, ide, null, Float.valueOf(polje[3]));
                        }

                        i++;
                    }
                }

                System.out.println(polje[2]);
                System.out.println(i);








            }





        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    private static int getOrInsertPlanet(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT PlanetId FROM Planet WHERE naziv = ?");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt("PlanetId");

        stmt = conn.prepareStatement("INSERT INTO Planet(naziv) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private static Integer getElementId(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT ElementId FROM Element WHERE PunoIme = ?");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt("ElementId");
        return null;
    }

    private static Integer getOrInsertSpoj(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT SpojId FROM Spoj WHERE naziv = ?");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt("SpojId");

        stmt = conn.prepareStatement("INSERT INTO Spoj(naziv, MolarnaMasa) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.setFloat(2, 0);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private static boolean existsInAtmosfera(Connection conn, int planetId, Integer elementId, Integer spojId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Atmosfera WHERE PlanetId = ? AND " +
                (elementId != null ? "ElementId = ?" : "SpojId = ?");
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, planetId);
        stmt.setInt(2, elementId != null ? elementId : spojId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        System.out.println(rs.getInt(1) > 0);
        return rs.getInt(1) > 0;
    }

    private void insertIntoAtmosfera(Connection conn, int planetId, Integer elementId, Integer spojId, Float share) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Atmosfera (PlanetId, ElementId, SpojId, Udio) VALUES (?, ?, ?, ?)");
        stmt.setInt(1, planetId);
        if (elementId != null) stmt.setInt(2, elementId); else stmt.setNull(2, Types.INTEGER);
        if (spojId != null) stmt.setInt(3, spojId); else stmt.setNull(3, Types.INTEGER);
        if (share != null) stmt.setFloat(4, share); else stmt.setNull(4, Types.FLOAT);
        stmt.executeUpdate();
    }

    private void prikaziAtmosferu() {
        String planet = (String) comboBox1.getSelectedItem();
        if (planet == null) return;

        DefaultTableModel model = new DefaultTableModel(new String[]{"Tvar", "Udio"}, 0);
        try {
            String sql = "SELECT COALESCE(e.PunoIme, s.Naziv) AS Tvar, a.Udio " +
                    "FROM Atmosfera a " +
                    "LEFT JOIN Element e ON a.ElementId = e.ElementId " +
                    "LEFT JOIN Spoj s ON a.SpojId = s.SpojId " +
                    "JOIN Planet p ON a.PlanetId = p.PlanetId " +
                    "WHERE p.Naziv = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, planet);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Tvar"), rs.getString("Udio")});
            }
            table1.setModel(model);
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Greška pri prikazu atmosfere: " + ex.getMessage());
        }
    }

    private void ucitajPlanete() {
        comboBox1.removeAllItems();
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT Naziv FROM session2.planet;");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                comboBox1.addItem(rs.getString("Naziv"));
            }
            pst.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju planeta: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        new atmosfere();
    }

}
