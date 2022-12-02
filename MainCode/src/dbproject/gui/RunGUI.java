package dbproject.gui;

import javax.swing.*;
import java.sql.*;

public class RunGUI {

    //TODO: public final static DatabaseMenu = new DatabaseMenu();

    public final static boolean DEBUG = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Computer Science Jobs Database");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel getStarted = new GetStartedPanel().panel();
        JPanel insertPanel = new InsertPanel().panel();

        tabbedPane.addTab("Get Started", getStarted);
        tabbedPane.addTab("Insert", insertPanel);

        frame.getContentPane().add(tabbedPane);
    }
}
