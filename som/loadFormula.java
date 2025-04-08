import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class loadFormula {
    private Connection conn;
    JFrame f = new JFrame();
    public loadFormula(DefaultTableModel tableModel, List<Integer> elementIds, JComboBox<String> spojeviCombo, Map<String, Integer> spojeviMap) {
        conn = JavaConnect.connectDb();
        tableModel.setRowCount(0);
        elementIds.clear();
        String selected = (String) spojeviCombo.getSelectedItem();
        if (selected == null) return;

        int spojId = spojeviMap.get(selected);
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT f.ElementId, e.Simbol, f.Kolicina FROM Formula f JOIN Element e ON f.ElementId = e.ElementId WHERE f.SpojId = ? ORDER BY f.RedniBroj")) {
            stmt.setInt(1, spojId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int eid = rs.getInt("ElementId");
                String simbol = rs.getString("Simbol");
                int kolicina = rs.getInt("Kolicina");
                elementIds.add(eid);
                tableModel.addRow(new Object[]{simbol, kolicina});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
