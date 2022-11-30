package gui;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class InsertWindow {

    private final VBox vbox = new VBox(); // main VBox
    private final VBox form = new VBox(); // has the form text fields inside

    private final EnumMap<Relation, PaneObject> paneMap = new EnumMap<>(Relation.class);
    private final ComboBox<Relation> comboBox = new ComboBox<>();
    private GridPane activePane;

    public InsertWindow(int padding, DoubleExpression vboxHeight) {

        initializeMap();

        vbox.setPadding(new Insets(padding)); // main VBox
        vbox.prefHeightProperty().bind(vboxHeight);

        form.setPadding(new Insets(padding));

        // ComboBox
        comboBox.getItems().setAll(Relation.values());
        comboBox.getSelectionModel().select(0); // set the default to the first option

        // Set the active pane
        activePane = setActivePane(comboBox.getValue());

        form.getChildren().add(activePane);

        // Changing active pane when combo box is changed
        comboBox.setOnAction(e -> {
            GridPane newActivePane = setActivePane(comboBox.getValue());

            form.getChildren().remove(activePane);
            form.getChildren().add(newActivePane);

            activePane = newActivePane;
        });

        // Add button
        Button button = new Button("Insert");
        button.setAlignment(Pos.BOTTOM_CENTER);

        button.setOnAction(e -> insertData());

        // Add all components
        vbox.getChildren().addAll(comboBox, form, button);
    }

    /**
     * Fills the paneMap with predesignated keys and values (should be called in this class' constructor)
     */
    private void initializeMap() {
        paneMap.put(Relation.COMPANY, new PaneObject("comp_id", "name", "hq_location", "tier", "industry", "num_employees", "revenue"));
        paneMap.put(Relation.JOBS, new PaneObject("job_id", "comp_ID", "type", "role", "description", "total_compesantion", "required_yoe", "location", "cycle", "date_opened", "deadline"));
        paneMap.put(Relation.LOCATION, new PaneObject("country", "city", "state"));
    }

    /**
     *
     * @param key Table enum
     * @return GridPane object mapped to the given key
     */
    private GridPane setActivePane(Relation key) {
        return paneMap.get(key).gridPane();
    }

    /**
     * @return VBox containing the contents of this window
     */
    public VBox vBox() {
        return vbox;
    }

    /**
     * Inserts data into the database
     */
    private void insertData() {
        PaneObject current = paneMap.get(comboBox.getValue());

        Map<Label, TextField> textFieldMap = current.textFieldMap();

        ArrayList<String> values = textFieldMap.values().stream().map(field -> field.getText()).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> columns = textFieldMap.keySet().stream().map(label -> label.getText()).collect(Collectors.toCollection(ArrayList::new));

        String tableName = comboBox.getValue().toString();

        // DatabaseMenu.insertData(values, columns, tableName);
    }
}
