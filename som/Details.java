import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Details extends JDialog{
    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JPanel panel2;
    private JLabel Simbol;
    private JLabel atomb;
    private JLabel ime;
    private JLabel atomm;
    ArrayList<Element> elementArrayList = new ArrayList<>();

    public Details(ArrayList<Element> elementArrayList, int index) {


        if (index < 0 || index >= elementArrayList.size()) return;

        Element el = elementArrayList.get(index);

        System.out.println(el.ime);

        Simbol.setText(el.simbol);
        atomb.setText(String.valueOf(el.atomskiBroj));
        ime.setText(el.ime);
        atomm.setText(String.valueOf(el.atomskaMasa));

        switch (el.vrstaId) {
            case 1: panel2.setBackground(Color.YELLOW); break; // Metal
            case 2: panel2.setBackground(Color.GREEN); break; // Metaloid
            case 3: panel2.setBackground(Color.CYAN); break; // Non-metal
            default: panel2.setBackground(Color.LIGHT_GRAY); break;
        }

        button1.setEnabled(index > 0);
        button2.setEnabled(index < elementArrayList.size() - 1);

        button1.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new Details(elementArrayList, index - 1));
            dispose();

        });
        button2.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new Details(elementArrayList, index + 1));
            dispose();

        });

        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300,300);
        setModal(true);
        setVisible(true);
    }
}
