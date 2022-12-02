package dbproject.gui;

import dbproject.DatabaseMenu;
import java.sql.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertPanel {

    // private final EnumMap<Relation, InsertForm> formMap = new EnumMap<>(Map.of(
    //         Relation.COMPANY, new InsertForm("company_id", "name", "hq_location", "tier", "industry", "num_employees"),
    //         Relation.JOBS, new InsertForm("job_id", "company_id", "type", "role", "description", "benefit_id", "required_yoe", "location_id", "cycle", "date_opened"),
    //         Relation.LOCATION, new InsertForm("location_id", "country", "city", "state")
    // ));

    private final Map<String, InsertForm> formMap = new HashMap<>();

    private JPanel activePanel;
    private JPanel mainPanel = new JPanel();
    private JPanel form = new JPanel(); // contains the labels and text fields
    private JComboBox<String> comboBox; // = new JComboBox<>(Relation.values());

    public InsertPanel() {
        initializeMap();

        Box verticalBox = Box.createVerticalBox();

        // Set up the combobox
        String[] tableNames = RunGUI.tableNames.toArray(new String[RunGUI.tableNames.size()]);

        comboBox = new JComboBox<>(tableNames);

        // Set the default active panel
        activePanel = getActivePanel((String) comboBox.getSelectedItem());
        form.add(activePanel);

        comboBox.addActionListener (e ->  setActivePanel());

        // Insert button
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
     * Fills the formMap
     */
    private void initializeMap() {
        
        for(String table : RunGUI.tableNames) {
            ResultSetMetaData rsmd = DatabaseMenu.getColumnRSMDFromTable(RunGUI.resultSet, table);

            ArrayList<String> attributes = DatabaseMenu.getColumnsFromRSMD(rsmd);

            formMap.put(table, new InsertForm(attributes));
        }
    }

    /**
     * @return the main JPanel
     */
    public JPanel panel() {
        return mainPanel;
    }

    /**
     * Return a new JPanel based on the given key
     *
     * @param key a Relation name
     * @return JPanel
     */
    private JPanel getActivePanel(String key) {
        return formMap.get(key).panel();
    }

    private void setActivePanel() {
        JPanel newActivePanel = getActivePanel((String) comboBox.getSelectedItem());

        form.removeAll();
        form.revalidate(); // refreshes the panel
        form.add(newActivePanel);

        activePanel = newActivePanel;
    }

    /**
     * Insert data into the database
     */
    private void insertData() {
        InsertForm current = formMap.get((String) comboBox.getSelectedItem());

        Map<JLabel, JTextField> textFieldMap = current.textFieldMap();

        ArrayList<String> values = textFieldMap.values().stream().map(JTextComponent::getText).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> columns = textFieldMap.keySet().stream().map(JLabel::getText).collect(Collectors.toCollection(ArrayList::new));

        String tableName = comboBox.getSelectedItem().toString();

        if(RunGUI.DEBUG) {
            System.out.println("Table: " + tableName);

            System.out.print("\ncolumns: ");
            columns.stream().forEach(col -> System.out.print(col + " "));
            System.out.println("\n");

            System.out.print("Values: ");
            values.stream().forEach(val -> System.out.print(val + " "));
        }

        DatabaseMenu.insertData(values, columns, tableName);

        // Clear the text fields
        textFieldMap.values().stream().forEach(field -> field.setText(""));
    }

}