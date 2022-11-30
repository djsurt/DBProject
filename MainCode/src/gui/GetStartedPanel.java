package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GetStartedPanel {

    private JPanel mainPanel = new JPanel();

    private final String fontFamily = "Calibri";
    private final Font headingFont = new Font(fontFamily, Font.BOLD, 30);
    private final Font normalFont = new Font(fontFamily, Font.PLAIN, 15);

    public GetStartedPanel() {

        Box verticalBox = Box.createVerticalBox();

        JLabel heading = new JLabel();
        heading.setText("Computer Science Jobs Database");
        heading.setFont(headingFont);
        heading.setBorder(new EmptyBorder(0,0,20,0));

        JLabel body = new JLabel();
        String bodyText = "CSDS 341: Introduction to Database Systems | Fall 2022 with Professor Dianne Foreback <br> Created by Eric Elizes, Omar Loudghiri, Georgia Martinez, Sam Michalski, and Dhananjay Surti";

        body.setText("<html>"+bodyText+"</html>");

        body.setFont(normalFont);

        verticalBox.add(heading);
        verticalBox.add(body);

        mainPanel.add(verticalBox);
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * @return the main JPanel
     */
    public JPanel panel() {
        return mainPanel;
    }

}
