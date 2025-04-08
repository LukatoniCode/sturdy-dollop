import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class toTable {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    private JTable table;

    public toTable(JTable table) {
        this.table = table;
        conn = JavaConnect.connectDb();
    }

    private void kolicina1izostavitekolicinu() {
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

            table.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            pst.close();

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
