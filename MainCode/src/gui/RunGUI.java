package gui;

import javax.swing.*;

public class RunGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Computer Science Jobs Database");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel getStarted = new GetStartedPanel().panel();
        JPanel insertPanel = new InsertPanel().panel();
        JPanel searchPanel = new JPanel();

        tabbedPane.addTab("Get Started", getStarted);
        tabbedPane.addTab("Insert", insertPanel);
        tabbedPane.addTab("Search", searchPanel);

        frame.getContentPane().add(tabbedPane);
    }
}
