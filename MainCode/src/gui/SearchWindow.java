package gui;

import gui.relations.Location;
import gui.relations.TableManager;
import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class SearchWindow {

    private final VBox vbox = new VBox(); // main VBox
    private final VBox tableBox = new VBox(); // contains the table

    private final TableManager tableManager = new TableManager();

    public SearchWindow(int padding, DoubleExpression vboxHeight) {
        vbox.setPadding(new Insets(padding)); // main VBox
        vbox.prefHeightProperty().bind(vboxHeight);

        tableBox.setPadding(new Insets(padding)); // main VBox

        tableBox.getChildren().add(tableManager.getTable(Relation.LOCATION));

//        ScrollPane scrollPane = new ScrollPane(tableManager.getTable(Relation.LOCATION));
//        scrollPane.setFitToWidth(true);
//        scrollPane.setFitToHeight(true);
//
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Add all components
        vbox.getChildren().addAll(tableBox);
    }

    /**
     * @return VBox containing the contents of this window
     */
    public VBox vBox() {
        return vbox;
    }
}
