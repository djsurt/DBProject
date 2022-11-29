package gui;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.Locale;

public class SearchWindow {

    private final VBox vbox = new VBox(); // main VBox
    private final VBox tableBox = new VBox(); // contains the table

    public SearchWindow(int padding, DoubleExpression vboxHeight) {
        vbox.setPadding(new Insets(padding)); // main VBox
        vbox.prefHeightProperty().bind(vboxHeight);

        tableBox.setPadding(new Insets(padding)); // main VBox

        TableView table = new TableView();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        TableColumn emailCol = new TableColumn("Email");

        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        tableBox.getChildren().addAll(table);

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
