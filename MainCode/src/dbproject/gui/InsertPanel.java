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

    private final Map<String, InsertForm> formMap = new HashMap<>();

    private JPanel activePanel;
    private JPanel mainPanel = new JPanel();
    private JPanel form = new JPanel(); 
    private JComboBox<String> comboBox;

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

        // Reset database button
        JButton resetButton = new JButton("Reset Database");
        resetButton.addActionListener(e -> resetDatabase());

        // Add all remaining components
        verticalBox.add(comboBox);
        verticalBox.add(form);
        verticalBox.add(insertButton);
        verticalBox.add(resetButton);

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
            ArrayList<String> types = DatabaseMenu.mapSQLTypeArrayToString(DatabaseMenu.getColumnTypesFromRSMD(rsmd));

            formMap.put(table, new InsertForm(attributes, types));
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

        boolean hasEmptyValue = values.stream().anyMatch(val -> val.trim().equals(""));

        if(hasEmptyValue) {
            String msg = "Please enter values for all fields before trying to insert";
            JOptionPane.showMessageDialog(null, msg);

            return;
        }
        
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

        String msg = "Your values were inserted into the " + tableName + " table!";
        JOptionPane.showMessageDialog(null, msg);
    }

    private void resetDatabase() {
        DatabaseMenu.resetDatabase();

        String msg = "The database has been reset to its default values";
        JOptionPane.showMessageDialog(null, msg);
    }

}