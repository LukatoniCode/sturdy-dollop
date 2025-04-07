import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateDb {
    JFrame f = new JFrame();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    public UpdateDb(JTable table, String tablename, String newname) {
        conn = JavaConnect.connectDb();
        int selectedRow = table.getSelectedRow();
        String oldTypeName = table.getValueAt(selectedRow, 0).toString();
        try {
            String sql = "UPDATE ? SET Naziv = ? WHERE Naziv = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, tablename);
            pst.setString(1, newname);
            pst.setString(2, oldTypeName);

            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(f, "Type updated successfully.");
            } else {
                JOptionPane.showMessageDialog(f, "Failed to update type.");
            }
        } catch (Exception ex) {
            System.out.println(ex);

            throw new RuntimeException(ex);
        }
    }
}
