import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

        GridPane pane = new GridPane();

        activePane = pane;

        // ComboBox
        comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().select(0); // set the default to the first option
        comboBox.setOnAction(e -> {
            vbox.getChildren().remove(activePane);
        });

        // Pane
        pane.setHgap(PADDING);
        pane.setVgap(PADDING);
        pane.setPadding(new Insets(PADDING));

        pane.add(new Text("Hello"), 0, 0, 2, 1);

        Label total = new Label("Income:");
        pane.add(total, 0, 1);
        final TextField totalField = new TextField();
        pane.add(totalField, 1, 1);
        Label percent = new Label("% Tax:");
        pane.add(percent,0,2);
        final TextField percentField = new TextField();
        pane.add(percentField, 1, 2);

        // Add all components
        vbox.getChildren().addAll(comboBox, pane);
    }

    public VBox vBox() {
        return vbox;
    }

    public ComboBox<String> comboBox() {
        return comboBox;
    }
}
