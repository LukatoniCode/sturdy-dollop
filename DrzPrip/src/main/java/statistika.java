import net.proteanit.sql.DbUtils;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class statistika extends JFrame{
    private JPanel panel1;
    private JLabel nvtt;
    private JLabel nmtt;
    private JPanel gr;
    private JTable table1;
    private JPanel otkrica;
    JFrame f = new JFrame();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    DefaultPieDataset dataset = new DefaultPieDataset<>();
    DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

    public statistika(){
        conn = JavaConnect.connectDb();
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        Maxtt();
        Mintt();
        Graf();
        setTable();
        Otkrica();
        setVisible(true);



    }

    private void Otkrica() {
        try{
            String sql = "SELECT \n" +
                    "    CASE\n" +
                    "        WHEN OtkriceGodina < 1800 THEN 'Ranije'\n" +
                    "        WHEN OtkriceGodina BETWEEN 1801 AND 1900 THEN '18. stoljeće'\n" +
                    "        WHEN OtkriceGodina BETWEEN 1901 AND 2000 THEN '19. stoljeće'\n" +
                    "        WHEN OtkriceGodina BETWEEN 2001 AND 2100 THEN '20. stoljeće'\n" +
                    "        WHEN OtkriceGodina > 2100 THEN '21. stoljeće'\n" +
                    "        ELSE 'Nepoznato'\n" +
                    "    END AS Period,\n" +
                    "    COUNT(*) AS broj\n" +
                    "FROM Element\n" +
                    "GROUP BY Period;";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while(rs.next()){
                String period = rs.getString("Period");
                int broj = rs.getInt("broj");
                dataset2.addValue(broj, "Otkriveni elementi", period);
            }

            pst.close();
            rs.close();


        } catch (Exception e) {
            JOptionPane.showMessageDialog(f, e);
            System.out.println(e);
            throw new RuntimeException(e);
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "Otkrića",
                "Razdoblje",
                "Broj elemenata",
                dataset2,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        otkrica.add(chartPanel);
    }

    private void setTable() {
        try{
            String sql = "SELECT BrojLjuski, COUNT(*) AS broj, GROUP_CONCAT(Simbol) AS elementi\n" +
                    "FROM Element\n" +
                    "GROUP BY BrojLjuski\n" +
                    "ORDER BY BrojLjuski;";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            table1.setModel(DbUtils.resultSetToTableModel(rs));

            pst.close();
            rs.close();


        } catch (Exception e) {
            JOptionPane.showMessageDialog(f, e);
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private void Graf() {
        try{
            String sql = "SELECT VrstaId, COUNT(*) AS broj\n" +
                    "FROM Element\n" +
                    "GROUP BY VrstaId;";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()){
                int vrstaId = rs.getInt("VrstaId");
                int broj = rs.getInt("broj");
                String vrsta = getVrsta(vrstaId);
                dataset.setValue(vrsta, broj);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(f, e);
            System.out.println(e);
            throw new RuntimeException(e);
        }

        JFreeChart chart = ChartFactory.createPieChart("Vrsta Elemenata", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));  // Set size for the chart panel
        gr.add(chartPanel);  // Add the chart panel to the panel

        // Refresh panel to ensure it renders correctly
        gr.revalidate();
        gr.repaint();
    }

    private String getVrsta(int vrstaId) {
        if (vrstaId == 1) return "metal";
        else if (vrstaId == 2) {
            return "metaloid";
        }
        else return "nemetal";
    }

    private void Maxtt() {

        try{
            String sql = "SELECT PunoIme, TockaTaljenja \n" +
                    "FROM Element \n" +
                    "where TockaTaljenja is not null\n" +
                    "ORDER BY TockaTaljenja desc LIMIT 1; ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println(rs.getFloat("TockaTaljenja"));
                nvtt.setText(rs.getString(1) + " (" + rs.getFloat(2) + ")");
            }

            rs.close();
            pst.close();
        }catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(f, e);
        }

    }

    private void Mintt() {

        try{
            String sql = "SELECT PunoIme, TockaTaljenja \n" +
                    "FROM Element \n" +
                    "where TockaTaljenja is not null\n" +
                    "ORDER BY TockaTaljenja ASC LIMIT 1; ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println(rs.getFloat("TockaTaljenja"));
                nmtt.setText(rs.getString(1) + " (" + rs.getFloat(2) + ")");
            }

            rs.close();
            pst.close();
        }catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(f, e);
        }

    }

    public static void main(String[] args) {
        new statistika();
    }
}
