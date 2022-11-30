package gui;

import javax.swing.*;
import java.awt.*;

public class SelectPanel {

    private JPanel mainPanel = new JPanel();

    public SelectPanel() {

        mainPanel.setVisible(true);

        String[] columns = {"Column1", "Column2", "Column3"};

        String[][] data = {
                {"Hello", "Hello", "Hello"},
                {"Hello", "Hello", "Hello"},
                {"Hello", "Hello", "Hello"},
                {"Hello", "Hello", "Hello"},
        };

        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(1000, 500));

        mainPanel.add(scrollPane);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /**
     * @return GridPane
     */
    public JPanel panel() {
        return mainPanel;
    }
}
