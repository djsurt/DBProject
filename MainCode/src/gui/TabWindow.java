package gui;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A class for generating JavaFX ComboBox with all the different tables inside of it
 */
public final class TabWindow {

    private VBox vbox;
    private ComboBox<Table> comboBox;
    private Pane activePane;

    private final int PADDING;

    public TabWindow(int padding) {

        this.PADDING = padding;

        // VBox
        vbox = new VBox();
        vbox.setPadding(new Insets(padding));

        // Pane
        PaneManager paneManager = new PaneManager("Hello", PADDING, "Field 1", "Field 2");
        GridPane gridPane = paneManager.gridPane();

        activePane = gridPane;

        // ComboBox
        comboBox = new ComboBox<>();
        comboBox.getItems().setAll(Table.values());

        comboBox.getSelectionModel().select(0); // set the default to the first option
        comboBox.setOnAction(e -> {
            comboBox.getValue();

            vbox.getChildren().remove(activePane);
        });

        // Add all components
        vbox.getChildren().addAll(comboBox, gridPane);
    }

    public VBox vBox() {
        return vbox;
    }
}
