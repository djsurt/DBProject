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

        // ComboBox
        comboBox = new ComboBox<>();
        comboBox.getItems().setAll(Table.values());

        comboBox.getSelectionModel().select(0); // set the default to the first option

        PaneManager defaultPaneManager = new PaneManager("comp_id", "name", "hq_location", "tier", "industry", "num_employees", "revenue");

        activePane = defaultPaneManager.gridPane();

                defaultPaneManager.gridPane();

        comboBox.setOnAction(e -> {
            GridPane newActivePane = getActivePane(comboBox.getValue());

            vbox.getChildren().remove(activePane);
            vbox.getChildren().add(newActivePane);

            activePane = newActivePane;
        });

        // Add all components
        vbox.getChildren().addAll(comboBox, activePane);
    }

    private GridPane getActivePane(Table key) {
        return PaneManager.paneMap.get(key).gridPane();
    }

    public VBox vBox() {
        return vbox;
    }
}
