package dbproject.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertForm {

    private JPanel panel = new JPanel();
    private GridLayout grid;
    private Map<JLabel, JTextField> textFieldMap = new LinkedHashMap<>();

    public InsertForm(ArrayList<String> fields, ArrayList<String> types) {

        grid = new GridLayout(fields.size(), 3);
        grid.setVgap(5);
        grid.setHgap(10);

        panel.setLayout(grid);
        panel.setPreferredSize(new Dimension(700, fields.size() * 30));

        for(int i = 0; i < fields.size(); i++) {
            String attributeText = fields.get(i);
            String typeText = "(type: " +types.get(i)+ ")";

            JLabel attributeLabel = new JLabel(attributeText);
            JTextField textField = new JTextField();
            JLabel typeLabel = new JLabel(typeText); // e.g. varchar

            panel.add(attributeLabel);
            panel.add(textField);
            panel.add(typeLabel);

            textFieldMap.put(attributeLabel, textField);
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