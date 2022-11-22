package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PaneManager {

    public static EnumMap<Table, PaneManager> paneMap = new EnumMap<>(Table.class);

    static {
        paneMap.put(Table.COMPANY, new PaneManager("comp_id", "name", "hq_location", "tier", "industry", "num_employees", "revenue"));
        paneMap.put(Table.JOBS, new PaneManager("job_id", "comp_ID", "type", "role", "description", "total_compesantion", "required_yoe", "location", "cycle", "date_opened", "deadline"));
    }

    private GridPane gridPane = new GridPane();
    private Map<Label, TextField> textFieldMap = new HashMap<>();

    private final int PADDING = 10;

    public PaneManager(String... fields) {

        // Set up the pain
        gridPane.setHgap(PADDING);
        gridPane.setVgap(PADDING);
        gridPane.setPadding(new Insets(PADDING));
        gridPane.setMinHeight(500);

        for(int i = 0; i < fields.length; i++) {
            String labelText = fields[i];

            Label label = new Label(labelText);
            TextField textField = new TextField();

            gridPane.add(label, 0, i);
            gridPane.add(textField, 1, i);

            textFieldMap.put(label, textField);
        }
    }

    public GridPane gridPane() {
        return gridPane;
    }
}
