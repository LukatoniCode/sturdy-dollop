import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class InsertAt {
    private Connection conn;
    JFrame f = new JFrame();
    public InsertAt(int index, List<Integer> elementIds, DefaultTableModel tableModel) {
        conn = JavaConnect.connectDb();
            String simbol = JOptionPane.showInputDialog("Simbol elementa:");
            if (simbol == null || simbol.isEmpty()) return;

            try (PreparedStatement stmt = conn.prepareStatement("SELECT ElementId FROM Element WHERE Simbol = ?")) {
                stmt.setString(1, simbol);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int eid = rs.getInt(1);
                    String kolStr = JOptionPane.showInputDialog("Količina (veće od 0):");
                    int kol = Integer.parseInt(kolStr);
                    if (kol <= 0) throw new Exception();

                    elementIds.add(index, eid);
                    tableModel.insertRow(index, new Object[]{simbol, kol});
                } else {
                    JOptionPane.showMessageDialog(f, "Nepoznat element!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Neispravan unos.");
            }
    }
}
