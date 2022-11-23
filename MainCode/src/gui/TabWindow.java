package gui;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * A class for generating JavaFX ComboBox with all the different tables inside of it
 */
public final class TabWindow {

    private final VBox vbox = new VBox(); // main VBox
    private final VBox form = new VBox(); // has the form text fields inside

    private final PaneManager paneManager = new PaneManager();
    private final ComboBox<Table> comboBox = new ComboBox<>();
    private GridPane activePane;

    public TabWindow(int padding, DoubleExpression vboxHeight) {

        vbox.setPadding(new Insets(padding)); // main VBox
        vbox.prefHeightProperty().bind(vboxHeight);

//        String cssLayout =
//                "-fx-border-color: red;\n" +
//                "-fx-border-insets: 5;\n" +
//                "-fx-border-width: 3;";
//
//        vbox.setStyle(cssLayout);

        form.setPadding(new Insets(padding)); // main VBox

        // ComboBox
        comboBox.getItems().setAll(Table.values());
        comboBox.getSelectionModel().select(0); // set the default to the first option

        // Set the active pane
        activePane = paneManager.gridPane(comboBox.getValue());

        form.getChildren().add(activePane);

        // Add button
        Button button = new Button("Click me");
        button.setAlignment(Pos.BOTTOM_CENTER);

        // Add all components
        vbox.getChildren().addAll(comboBox, form, button);

        // Changing active pane when combo box is changed
        comboBox.setOnAction(e -> {
            GridPane newActivePane = paneManager.gridPane(comboBox.getValue());

            form.getChildren().remove(activePane);
            form.getChildren().add(newActivePane);

            activePane = newActivePane;
        });
    }

    public VBox vBox() {
        return vbox;
    }
}
