package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class PaneManager {

    private GridPane gridPane = new GridPane();
    private Map<Label, TextField> textFieldMap = new HashMap<>();
    private final int PADDING;

    public PaneManager(String title, int padding, String... fields) {

        this.PADDING = padding;

        // Set up the pain
        gridPane.setHgap(PADDING);
        gridPane.setVgap(PADDING);
        gridPane.setPadding(new Insets(PADDING));

        gridPane.add(new Text(title), 0, 0, 2, 1); // Set title

        for(int i = 0; i < fields.length; i++) {
            String labelText = fields[i];

            System.out.println(labelText);

            Label label = new Label(labelText);
            TextField textField = new TextField();

            int row = i + 1; // because the gridPane title is at index 0

            gridPane.add(label, 0, row);
            gridPane.add(textField, 1, row);

            textFieldMap.put(label, textField);
        }
    }

    public GridPane gridPane() {
        return gridPane;
    }
}
