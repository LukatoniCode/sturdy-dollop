import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertIntoTable {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    JTable table;

    JFrame f = new JFrame();

    public InsertIntoTable(String tableName, String insertValue) {
        conn = JavaConnect.connectDb();

        String sql;

        try {
            sql = "INSERT INTO ? (Naziv) VALUES (?)";

            pst = conn.prepareStatement(sql);

            pst.setString(1, tableName);
            pst.setString(2, insertValue);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(f, "Tip uspješno dodan!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);


        } catch (Exception ef) {
            System.out.println(ef);
            JOptionPane.showMessageDialog(f, ef);
        }
    }
}
