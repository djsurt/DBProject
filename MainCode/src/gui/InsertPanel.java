package gui;

import javax.swing.*;
import java.util.EnumMap;

public class InsertPanel {

    private final EnumMap<Relation, InsertForm> formMap = new EnumMap<>(Relation.class);

    private JPanel panel = new JPanel();
    private JPanel form = new JPanel();
    private JPanel activePanel;
    private JComboBox<Relation> comboBox = new JComboBox<>(Relation.values());

    public InsertPanel() {

        initializeMap();

        activePanel = setActivePanel((Relation) comboBox.getSelectedItem());
        form.add(activePanel);

        comboBox.addActionListener (e -> {
            JPanel newActivePanel = setActivePanel((Relation) comboBox.getSelectedItem());

            form.removeAll();
            form.revalidate(); // refreshes the panel
            form.add(newActivePanel);

            activePanel = newActivePanel;
        });

        panel.add(comboBox);
        panel.add(form);
    }

    /**
     * Fills the paneMap with predesignated keys and values (should be called in this class' constructor)
     */
    private void initializeMap() {
        formMap.put(Relation.COMPANY, new InsertForm("comp_id", "name", "hq_location", "tier", "industry", "num_employees", "revenue"));
        formMap.put(Relation.JOBS, new InsertForm("job_id", "comp_ID", "type", "role", "description", "total_compesantion", "required_yoe", "location", "cycle", "date_opened", "deadline"));
        formMap.put(Relation.LOCATION, new InsertForm("country", "city", "state"));
    }

    private JPanel setActivePanel(Relation key) {
        return formMap.get(key).panel();
    }

    public JPanel panel() {
        return panel;
    }

}
