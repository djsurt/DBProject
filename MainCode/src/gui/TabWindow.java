package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private static ObservableList<String> options = FXCollections.observableArrayList("Option 1", "Option 2", "Option 3");
    private ComboBox<String> comboBox;

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
        comboBox = new ComboBox<>(options);
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

    public ComboBox<String> comboBox() {
        return comboBox;
    }
}
