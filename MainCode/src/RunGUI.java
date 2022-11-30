import javax.swing.*;

public class RunGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel getStarted = new JPanel();
        JPanel insertPanel = new JPanel();

        tabbedPane.addTab("Get Started", getStarted);
        tabbedPane.addTab("Insert", insertPanel);

//        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        frame.getContentPane().add(tabbedPane);
    }
}
