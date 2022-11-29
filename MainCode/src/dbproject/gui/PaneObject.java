package dbproject.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;
import java.util.Map;

public class PaneObject {

    private GridPane gridPane = new GridPane();
    private Map<Label, TextField> textFieldMap = new LinkedHashMap<>();

    private final int PADDING = 10;

    public PaneObject(String... fields) {

        // Set up the pain
        gridPane.setHgap(PADDING);
        gridPane.setVgap(PADDING);
        gridPane.setPadding(new Insets(PADDING));

        for(int i = 0; i < fields.length; i++) {
            String labelText = fields[i];

            Label label = new Label(labelText);
            TextField textField = new TextField();

            gridPane.add(label, 0, i);
            gridPane.add(textField, 1, i);

            textFieldMap.put(label, textField);
        }
    }

    /**
     * @return GridPane
     */
    public GridPane gridPane() {
        return gridPane;
    }

    /**
     * @return a map with Labels as keys and the corresponding TextField as values
     */
    public Map<Label, TextField> textFieldMap() {
        return textFieldMap;
    }
}
