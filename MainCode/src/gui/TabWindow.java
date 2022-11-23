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

    private VBox vbox = new VBox();
    private PaneManager paneManager = new PaneManager();
    private ComboBox<Table> comboBox = new ComboBox<>();
    private GridPane activePane;

    private final int PADDING;

    public TabWindow(int padding) {

        this.PADDING = padding;

        // VBox
        vbox.setPadding(new Insets(padding));

        // ComboBox
        comboBox.getItems().setAll(Table.values());
        comboBox.getSelectionModel().select(0); // set the default to the first option

        activePane = paneManager.gridPane(comboBox.getValue());

        comboBox.setOnAction(e -> {
            GridPane newActivePane = paneManager.gridPane(comboBox.getValue());

            vbox.getChildren().remove(activePane);
            vbox.getChildren().add(newActivePane);

            activePane = newActivePane;
        });

        // Add all components
        vbox.getChildren().addAll(comboBox, activePane);
    }

//    private GridPane getActivePane(Table key) {
//        return PaneObject.paneMap.get(key).gridPane();
//    }

    public VBox vBox() {
        return vbox;
    }
}
