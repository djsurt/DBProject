package gui;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;

/**
 * A class for generating JavaFX ComboBox with all the different tables inside of it
 */
public final class InsertWindow {

    private final VBox vbox = new VBox(); // main VBox
    private final VBox form = new VBox(); // has the form text fields inside

    private final EnumMap<Table, PaneObject> paneMap = new EnumMap<>(Table.class);
    private final ComboBox<Table> comboBox = new ComboBox<>();
    private GridPane activePane;

    public InsertWindow(int padding, DoubleExpression vboxHeight) {

        initializeMap();

        vbox.setPadding(new Insets(padding)); // main VBox
        vbox.prefHeightProperty().bind(vboxHeight);

        form.setPadding(new Insets(padding)); // main VBox

        // ComboBox
        comboBox.getItems().setAll(Table.values());
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

        button.setOnAction(e -> {
            PaneObject current = paneMap.get(comboBox.getValue());

            Map<Label, TextField> textFieldMap = current.textFieldMap();

            StringBuilder builder = new StringBuilder();

            String separator = "";
            boolean success = true;

            // Get the values from the textFieldMap and convert to a comma-separated list
            for(TextField textField : textFieldMap.values()) {

                String text = textField.getText();

                if(text.isEmpty()) {
                    success = false;
                    break;
                }

                builder.append(separator);
                separator = ", ";
                builder.append(text);
            }

            if(success){
                System.out.println(builder);
            } else {
                System.out.println("ERROR: Some fields were empty");
            }
        });

        // Add all components
        vbox.getChildren().addAll(comboBox, form, button);
    }

    /**
     * Fills the paneMap with predesignated keys and values (should be called in this class' constructor)
     */
    private void initializeMap() {
        paneMap.put(Table.COMPANY, new PaneObject("comp_id", "name", "hq_location", "tier", "industry", "num_employees", "revenue"));
        paneMap.put(Table.JOBS, new PaneObject("job_id", "comp_ID", "type", "role", "description", "total_compesantion", "required_yoe", "location", "cycle", "date_opened", "deadline"));
        paneMap.put(Table.LOCATION, new PaneObject("country", "city", "state"));
    }

    /**
     *
     * @param key Table enum
     * @return GridPane object mapped to the given key
     */
    private GridPane setActivePane(Table key) {
        return paneMap.get(key).gridPane();
    }

    /**
     * @return VBox containing the contents of this window
     */
    public VBox vBox() {
        return vbox;
    }
}
