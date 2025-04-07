import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import net.proteanit.sql.*;

public class elementi extends JFrame{
    ArrayList<Element> elementArrayList = new ArrayList<>();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    JFrame f = new JFrame();
    private JTable table1;
    private JTextField textField1;
    private JSlider slider1;
    private JSlider slider2;
    private JSpinner spinner1;
    private JButton tražiButton;
    private JButton resetirajTablicuButton;
    private JLabel maxa;
    private JLabel mina;
    private JPanel panel1;

    public elementi() {
        conn = JavaConnect.connectDb();
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Poćetna");
        setSize(600,600);
        setVisible(true);
        DefaultTableModel model = new DefaultTableModel();
        table1.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table1.setRowSorter(sorter);

        model.addColumn("Ime");
        model.addColumn("Simbol");
        model.addColumn("Atomski Broj");
        model.addColumn("Atomska masa");
        model.addColumn("Broj protona");
        model.addColumn("broj izotopa");
        model.addColumn("Details");

        new ButtonColumn(table1, DetailsAction, 6);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int i = slider1.getValue();
                mina.setText(String.valueOf(i));
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int i = slider2.getValue();
                maxa.setText(String.valueOf(i));
            }
        });
        tražiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                elementArrayList.clear();
                String sql;
                try{
                    sql = "SELECT PunoIme, Simbol, AtomskiBroj, AtomskaMasa, BrojProtona, COUNT(i.IzotopId) AS broj_izotopa, e.VrstaId\n" +
                            "FROM element e \n" +
                            "LEFT JOIN izotop i ON e.ElementId = i.ElementId\n" +
                            "where e.PunoIme like '"+ textField1.getText().toLowerCase() + "%' and e.AtomskiBroj between "+ slider1.getValue() + " and  " + slider2.getValue() + " " +
                            "GROUP BY e.PunoIme, e.Simbol, e.AtomskiBroj, e.AtomskaMasa, e.BrojProtona, e.VrstaId " +
                            "HAVING COUNT(i.IzotopId) >= " + spinner1.getValue() + " ";

                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();

                    while (rs.next()){
                        String ime = rs.getString("PunoIme");
                        String simbol = rs.getString("Simbol");
                        int Atb = rs.getInt("AtomskiBroj");
                        int Atm = rs.getInt("AtomskaMasa");
                        int BP = rs.getInt("BrojProtona");
                        int Bi = rs.getInt("broj_izotopa");
                        int Vid = rs.getInt("VrstaId");

                        model.addRow(new Object[]{ime, simbol, Atb, Atm, BP, Bi, "Details"});
                        table1.setValueAt("Details",rs.getRow()-1, 6);

                        Element el = new Element(ime, simbol, Atb, Atm, BP, Bi, Vid);
                        elementArrayList.add(el);

                        //setupButtonColumn(model);
                    }

                    pst.close();
                    rs.close();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(f, ex);
                    System.out.println(ex);
                }
            }
        });
        resetirajTablicuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                elementArrayList.clear();
                String sql;

                try{
                    sql = "SELECT PunoIme, Simbol, AtomskiBroj, AtomskaMasa, BrojProtona, COUNT(i.IzotopId) AS broj_izotopa, e.VrstaId\n" +
                            "FROM element e \n" +
                            "LEFT JOIN izotop i ON e.ElementId = i.ElementId\n" +
                            "GROUP BY e.PunoIme, e.Simbol, e.AtomskiBroj, e.AtomskaMasa, e.BrojProtona, e.VrstaId";

                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();

                    while (rs.next()){
                        String ime = rs.getString("PunoIme");
                        String simbol = rs.getString("Simbol");
                        int Atb = rs.getInt("AtomskiBroj");
                        int Atm = rs.getInt("AtomskaMasa");
                        int BP = rs.getInt("BrojProtona");
                        int Bi = rs.getInt("broj_izotopa");
                        int Vid = rs.getInt("VrstaId");

                        model.addRow(new Object[]{ime, simbol, Atb, Atm, BP, Bi, "Details"});
                        table1.setValueAt("Details",rs.getRow()-1, 6);
                        Element el = new Element(ime, simbol, Atb, Atm, BP, Bi, Vid);
                        elementArrayList.add(el);

                        //setupButtonColumn(model);
                    }

                    pst.close();
                    rs.close();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(f, ex);
                    System.out.println(ex);
                }
            }
        });
    }

    Action DetailsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = Integer.parseInt(e.getActionCommand());
            new Details(elementArrayList, row);
            System.out.println(row);
            System.out.println(table1.getSelectedRow());
        }
    };

    public static void main(String[] args) {
        new elementi();

    }

}
