import javax.swing.*;

public class Poc extends JFrame{
    private JPanel panel1;
    private JButton uređivanjeŠifrarnikaButton;
    private JButton izotopiButton;
    private JButton periodniSustavElemenataButton;
    private JButton spojeviButton;

    public Poc(){
        setTitle("Glavni ekran");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        if (panel1 == null) {
            panel1 = new JPanel();
        }

        JMenuBar meniBar = new JMenuBar();
        JMenu meni = new JMenu();
        meni.setText("Opcije");
        JMenuItem sifrarnikMenuItem = new JMenuItem("Uređivanje šifrarnika");
        JMenuItem izotopiMenuItem = new JMenuItem("Izotopi");
        JMenuItem spojeviMenuItem = new JMenuItem("Spojevi");
        JMenuItem periodniSustavMenuItem = new JMenuItem("Periodni sustav elemenata");

        meni.add(sifrarnikMenuItem);
        meni.add(izotopiMenuItem);
        meni.add(spojeviMenuItem);
        meni.add(periodniSustavMenuItem);

        meniBar.add(meni);

        setJMenuBar(meniBar);

        setContentPane(panel1);

        setVisible(true);

        sifrarnikMenuItem.addActionListener(e -> openScreen("sifranik"));
        izotopiMenuItem.addActionListener(e -> openScreen("izotopi"));
        spojeviMenuItem.addActionListener(e -> openScreen("spojevi"));
        periodniSustavMenuItem.addActionListener(e -> openScreen("periodni"));

        uređivanjeŠifrarnikaButton.addActionListener(e -> openScreen("sifranik"));
        izotopiButton.addActionListener(e -> openScreen("izotopi"));
        spojeviButton.addActionListener(e -> openScreen("izotopi"));
        periodniSustavElemenataButton.addActionListener(e -> openScreen("periodni"));

    }

    private void openScreen(String meni) {
        dispose();
        switch (meni){
            case "sifranik":
                new sifranik();
                break;
            case "izotopi":
                new izotopi();
                break;
            case "spojevi":
                new spojevi();
                break;
            case "periodni":
                new periodni();
                break;
        }
    }

    public static void main(String[] args) {
        new Poc();
    }
}
