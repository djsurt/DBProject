package dbproject.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertForm {

    private JPanel panel = new JPanel();
    private GridLayout grid;
    private Map<JLabel, JTextField> textFieldMap = new LinkedHashMap<>();

    public InsertForm(String... fields) {

        grid = new GridLayout(fields.length, 2);
        grid.setVgap(5);
        grid.setHgap(10);

        panel.setLayout(grid);
        panel.setPreferredSize(new Dimension(500, fields.length * 30));

        for(int i = 0; i < fields.length; i++) {
            String labelText = fields[i];

            JLabel label = new JLabel(labelText);
            JTextField textField = new JTextField();

            panel.add(label);
            panel.add(textField);

            textFieldMap.put(label, textField);
        }
    }

    /**
     * @return GridPane
     */
    public JPanel panel() {
        return panel;
    }

    /**
     * @return a map with Labels as keys and the corresponding TextField as values
     */
    public Map<JLabel, JTextField> textFieldMap() {
        return textFieldMap;
    }
}