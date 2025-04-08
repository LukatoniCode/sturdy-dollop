import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class SpojeviForm extends JFrame {
    private JComboBox<String> spojeviCombo;
    private JTable formulaTable;
    private DefaultTableModel tableModel;
    private JLabel formulaLabel;
    private JButton spremiButton;

    private Map<String, Integer> spojeviMap = new HashMap<>();
    private List<Integer> elementIds = new ArrayList<>();

    private Connection conn;

    public SpojeviForm() {
        setTitle("Spojevi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        conn = JavaConnect.connectDb();

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Odaberite spoj:"));
        spojeviCombo = new JComboBox<>();
        topPanel.add(spojeviCombo);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Element", "Količina"}, 0);
        formulaTable = new JTable(tableModel);
        formulaTable.setComponentPopupMenu(createPopupMenu());
        formulaTable.addMouseListener(new TableClickListener());
        JScrollPane scrollPane = new JScrollPane(formulaTable);
        add(scrollPane, BorderLayout.CENTER);

        formulaLabel = new JLabel("Trenutna formula: ");
        spremiButton = new JButton("SPREMI FORMULU");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formulaLabel, BorderLayout.CENTER);
        bottomPanel.add(spremiButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        loadSpojevi();

        spojeviCombo.addActionListener(e -> loadFormula());
        spremiButton.addActionListener(e -> saveFormula());
    }



    private void loadSpojevi() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SpojId, Naziv FROM Spoj")) {
            spojeviCombo.removeAllItems();
            spojeviMap.clear();
            while (rs.next()) {
                String naziv = rs.getString("Naziv");
                int id = rs.getInt("SpojId");
                spojeviCombo.addItem(naziv);
                spojeviMap.put(naziv, id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadFormula() {
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
        updateFormula();
    }

    private void updateFormula() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String simbol = tableModel.getValueAt(i, 0).toString();
            int kol = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            sb.append(simbol);
            if (kol > 1) sb.append(kol);
        }
        formulaLabel.setText("Trenutna formula: " + sb);
    }

    private void saveFormula() {
        String selected = (String) spojeviCombo.getSelectedItem();
        if (selected == null) return;
        int spojId = spojeviMap.get(selected);

        try {
            conn.setAutoCommit(false);
            PreparedStatement del = conn.prepareStatement("DELETE FROM Formula WHERE SpojId = ?");
            del.setInt(1, spojId);
            del.executeUpdate();

            PreparedStatement ins = conn.prepareStatement("INSERT INTO Formula (SpojId, RedniBroj, ElementId, Kolicina) VALUES (?, ?, ?, ?)");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                ins.setInt(1, spojId);
                ins.setInt(2, i);
                ins.setInt(3, elementIds.get(i));
                ins.setInt(4, Integer.parseInt(tableModel.getValueAt(i, 1).toString()));
                ins.addBatch();
            }
            ins.executeBatch();
            conn.commit();
            JOptionPane.showMessageDialog(this, "Formula spremljena.");
        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            ex.printStackTrace();
        }
    }

    private JPopupMenu createPopupMenu() {
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
                updateFormula();
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
                        updateFormula();
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
                updateFormula();
            } else {
                JOptionPane.showMessageDialog(this, "Nepoznat element!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Neispravan unos.");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpojeviForm().setVisible(true));
    }
}
