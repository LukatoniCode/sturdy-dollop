import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createPopupMenu {
    private JTable formulaTable;
    private DefaultTableModel tableModel;
    private List<Integer> elementIds = new ArrayList<>();
    JFrame f = new JFrame();
    private Connection conn;
    public createPopupMenu(JTable formulaTable, DefaultTableModel tableModel, List<Integer> elementIds) {
        conn = JavaConnect.connectDb();
        this.formulaTable = formulaTable;
        this.tableModel = tableModel;
        this.elementIds = elementIds;
        create(formulaTable, tableModel, elementIds);
    }

    public JPopupMenu create(JTable formulaTable, DefaultTableModel tableModel, List<Integer> elementIds) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem ukloni = new JMenuItem("Ukloni");
        JMenuItem promijeni = new JMenuItem("Promijeni količinu");
        JMenuItem umetniIznad = new JMenuItem("Umetni iznad");
        JMenuItem umetniIspod = new JMenuItem("Umetni ispod");

        ukloni.addActionListener(e -> {
            int row = formulaTable.getSelectedRow();
            if (row >= 0) {
                tableModel.removeRow(row);
                elementIds.remove(row);
            }
        });

        promijeni.addActionListener(e -> {
            int row = formulaTable.getSelectedRow();
            if (row >= 0) {
                String input = JOptionPane.showInputDialog("Nova količina:");
                try {
                    int k = Integer.parseInt(input);
                    if (k > 0) {
                        tableModel.setValueAt(k, row, 1);
                    }
                } catch (Exception ignored) {}
            }
        });

        umetniIznad.addActionListener(e -> insertAt(formulaTable.getSelectedRow()));
        umetniIspod.addActionListener(e -> insertAt(formulaTable.getSelectedRow() + 1));

        menu.add(ukloni);
        menu.add(promijeni);
        menu.add(umetniIznad);
        menu.add(umetniIspod);

        return menu;
    }

    private void insertAt(int index) {
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

    private class TableClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            int row = formulaTable.rowAtPoint(e.getPoint());
            if (row >= 0) {
                formulaTable.setRowSelectionInterval(row, row);
            }
        }
    }
}
