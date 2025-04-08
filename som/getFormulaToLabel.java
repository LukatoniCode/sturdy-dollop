import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class getFormulaToLabel {
    public getFormulaToLabel(DefaultTableModel tableModel, JLabel formulaLabel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String simbol = tableModel.getValueAt(i, 0).toString();
            int kol = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            sb.append(simbol);
            if (kol > 1) sb.append(kol);
        }
        formulaLabel.setText("Trenutna formula: " + sb);
    }
}
