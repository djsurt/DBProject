package gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertPanel {

    private final EnumMap<Relation, InsertForm> formMap = new EnumMap<>(Relation.class);

    private JPanel activePanel;
    private JPanel mainPanel = new JPanel();
    private JPanel form = new JPanel(); // contains the labels and text fields
    private JComboBox<Relation> comboBox = new JComboBox<>(Relation.values());

    public InsertPanel() {

        initializeMap();

        Box verticalBox = Box.createVerticalBox();

        // Set the default active panel
        activePanel = getActivePanel((Relation) comboBox.getSelectedItem());
        form.add(activePanel);

        // Changing active panel when combo box is changed
        comboBox.addActionListener (e -> {
            JPanel newActivePanel = getActivePanel((Relation) comboBox.getSelectedItem());

            form.removeAll();
            form.revalidate(); // refreshes the panel
            form.add(newActivePanel);

            activePanel = newActivePanel;
        });

        JButton insertButton = new JButton("Insert");

        insertButton.addActionListener(e -> insertData());

        // Add all remaining components
        verticalBox.add(comboBox);
        verticalBox.add(form);
        verticalBox.add(insertButton);

        mainPanel.add(verticalBox);
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Fills the formMap with predesignated keys and values (should be called in this class' constructor)
     */
    private void initializeMap() {
        formMap.put(Relation.COMPANY, new InsertForm("company_id", "name", "hq_location", "tier", "industry", "num_employees"));
        formMap.put(Relation.JOBS, new InsertForm("job_id", "company_id", "type", "role", "description", "benefit_id", "required_yoe", "location_id", "cycle", "date_opened"));
        formMap.put(Relation.LOCATION, new InsertForm("location_id", "country", "city", "state"));
    }

    /**
     * Return a new JPanel based on the given key
     *
     * @param key a Relation name
     * @return JPanel
     */
    private JPanel getActivePanel(Relation key) {
        return formMap.get(key).panel();
    }

    /**
     * @return the main JPanel
     */
    public JPanel panel() {
        return mainPanel;
    }

    /**
     * Insert data into the database
     */
    private void insertData() {
        InsertForm current = formMap.get((Relation) comboBox.getSelectedItem());

        Map<JLabel, JTextField> textFieldMap = current.textFieldMap();

        ArrayList<String> values = textFieldMap.values().stream().map(JTextComponent::getText).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> columns = textFieldMap.keySet().stream().map(JLabel::getText).collect(Collectors.toCollection(ArrayList::new));

        String tableName = comboBox.getSelectedItem().toString();

        // DatabaseMenu.insertData(values, columns, tableName);
    }

}
