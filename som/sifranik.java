import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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
        setVisible(true);




        glavniEkranButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Poc();
            }
        });
        spremiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newNaziv = nazivTextField.getText();

            }
        });
        noviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nazivTextField.setBackground(Color.green);
                nazivTextField.setText("");
                odabir(1);
            }
        });
        spremiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                }catch (Exception ef){
                    System.out.println(ef);
                    JOptionPane.showMessageDialog(f, ef);
                }

            }
        });
    }

    private int odabir(int i) {
        return i;
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
