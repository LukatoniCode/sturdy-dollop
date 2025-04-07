import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import net.proteanit.sql.*;

public class sifranik extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton obrišiButton;
    private JButton izmijeniButton;
    private JButton noviButton;
    private JTextField nazivTextField;
    private JButton spremiButton;
    private JButton odustaniButton;
    private JButton glavniEkranButton;
    private JTable table1;
    private JButton glavniEkranButton1;
    private JTable table2;
    int red_a = table2.getSelectedRow();
    JFrame f = new JFrame();
    int odabir = 0;

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public sifranik() {
        conn = JavaConnect.connectDb();
        setTitle("Glavni ekran");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(tabbedPane1);
        inicijalizirajTablicuTipova();
        inicijalizirajTablicu();
        setVisible(true);
        spremiButton.setEnabled(false);
        odustaniButton.setEnabled(false);




        glavniEkranButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Poc();
            }
        });
        noviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nazivTextField.setBackground(Color.green);
                nazivTextField.setText("");
                odabir = 1;
                spremiButton.setEnabled(true);
                odustaniButton.setEnabled(true);
                izmijeniButton.setEnabled(false);
                obrišiButton.setEnabled(false);
            }
        });
        spremiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (izmijeniButton.isEnabled()){
                    String typeName = nazivTextField.getText().trim();
                    int selectedRow = table2.getSelectedRow();
                    String oldTypeName = table2.getValueAt(selectedRow, 0).toString();
                    try {
                        String sql = "UPDATE Tip SET Naziv = ? WHERE Naziv = ?";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, typeName);
                        pst.setString(2, oldTypeName);

                        int result = pst.executeUpdate();

                        if (result > 0) {
                            JOptionPane.showMessageDialog(f, "Type updated successfully.");
                            inicijalizirajTablicuTipova();
                            noviButton.setEnabled(true);
                            obrišiButton.setEnabled(true);
                        } else {
                            JOptionPane.showMessageDialog(f, "Failed to update type.");
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);

                        throw new RuntimeException(ex);
                    }
                }
                if (noviButton.isEnabled()) {
                    String naziv = nazivTextField.getText();
                    if (naziv == null || naziv.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(f, "Naziv ne može biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);

                    }

                    String sql;

                    try {
                        sql = "INSERT INTO Tip (Naziv) VALUES (?)";

                        pst = conn.prepareStatement(sql);

                        pst.setString(1, naziv);

                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(f, "Tip uspješno dodan!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                        inicijalizirajTablicuTipova();

                        izmijeniButton.setEnabled(true);
                        obrišiButton.setEnabled(true);

                    } catch (Exception ef) {
                        System.out.println(ef);
                        JOptionPane.showMessageDialog(f, ef);
                    }
                }
            }
        });
        odustaniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nazivTextField.setText("Naziv");
                nazivTextField.setBackground(Color.white);
                odabir = 0;
                spremiButton.setEnabled(false);
                odustaniButton.setEnabled(false);
                izmijeniButton.setEnabled(true);
                noviButton.setEnabled(true);
                obrišiButton.setEnabled(true);
            }
        });
        izmijeniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                odabir = 2;
                obrišiButton.setEnabled(false);
                noviButton.setEnabled(false);
                spremiButton.setEnabled(true);
                odustaniButton.setEnabled(true);

            }
        });
        obrišiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                izmijeniButton.setEnabled(false);
                noviButton.setEnabled(false);
                int selectedRow = table2.getSelectedRow();
                int Tipid = getTipId(table2.getValueAt(selectedRow, 0).toString());
                if (selectedRow != -1) {
                    deleteType(Tipid);
                }
                izmijeniButton.setEnabled(true);
                noviButton.setEnabled(true);
            }

            private int getTipId(String ime) {
                String sql;
                sql = " SELECT TipId FROM session1.tip where Naziv = ?;";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, ime);
                    rs = pst.executeQuery();

                    rs.next();

                    return rs.getInt(1);


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void deleteType(int typeName) {
                String checkSql = "SELECT COUNT(*) FROM Element WHERE TipId = ?";
                try {
                    pst = conn.prepareStatement(checkSql);
                    pst.setInt(1, typeName);
                    ResultSet rs = pst.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);

                    if (count > 0) {
                        String elements = getLinkedElements(typeName);
                        JOptionPane.showMessageDialog(f, "Cannot delete type because it is linked to elements: " + elements);
                    } else {
                        String deleteSql = "DELETE FROM Tip WHERE TipId = ?";
                        PreparedStatement deletePst = conn.prepareStatement(deleteSql);
                        deletePst.setInt(1, typeName);
                        int result = deletePst.executeUpdate();

                        if (result > 0) {
                            JOptionPane.showMessageDialog(f, "Type deleted successfully.");
                            inicijalizirajTablicuTipova();
                        } else {
                            JOptionPane.showMessageDialog(f, "Failed to delete type.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    throw new RuntimeException(e);
                }

            }

            private String getLinkedElements(int typeName) throws SQLException {
                String sql = "SELECT PunoIme FROM Element WHERE TipId = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, typeName);
                ResultSet rs = pst.executeQuery();

                ArrayList<String> elements = new ArrayList<>();
                while (rs.next()) {
                    elements.add(rs.getString("PunoIme"));
                }
                return String.join(", ", elements);
            }
        });
        glavniEkranButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Poc();
            }
        });
    }

    private void inicijalizirajTablicu() {
        String sql = "SELECT \n" +
                "    s.Naziv AS NazivSpoja,\n" +
                "    s.MolarnaMasa,\n" +
                "    GROUP_CONCAT(\n" +
                "        CONCAT(e.Simbol,\n" +
                "               CASE \n" +
                "                   WHEN f.Kolicina = 1 THEN '' \n" +
                "                   ELSE f.Kolicina \n" +
                "               END)\n" +
                "        ORDER BY f.RedniBroj\n" +
                "        SEPARATOR ''\n" +
                "    ) AS Formula\n" +
                "FROM \n" +
                "    Spoj s\n" +
                "LEFT JOIN Formula f ON s.SpojId = f.SpojId\n" +
                "LEFT JOIN Element e ON f.ElementId = e.ElementId\n" +
                "GROUP BY \n" +
                "    s.SpojId, s.Naziv, s.MolarnaMasa;";

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);

            table1.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            pst.close();

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private void inicijalizirajTablicuTipova() {
        String sql;
        try {
            sql = "SELECT Naziv FROM session1.tip;";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            table2.setModel(DbUtils.resultSetToTableModel(rs));



            table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {  // This checks that the selection is final
                        int selectedRow = table2.getSelectedRow();  // Get the selected row
                        if (selectedRow != -1) {
                            // Get the name from the table column (assuming the name is in column 1)
                            String selectedName = (String) table2.getValueAt(selectedRow, 0);
                            nazivTextField.setText(selectedName);
                        }
                    }
                }
            });


            rs.close();
            pst.close();


        }catch (SQLException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(f, e);
        }
    }

    public static void main(String[] args) {
        new sifranik();
    }
}
