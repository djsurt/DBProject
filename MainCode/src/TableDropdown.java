import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * A class for generating JavaFX ComboBox with all the different tables inside of it
 */
public final class TableDropdown {

    private static ObservableList<String> options = FXCollections.observableArrayList("Option 1", "Option 2", "Option 3");

    public static ComboBox<String> getComboBox() {
        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().select(0); // set the default to the first option

        return comboBox;
    }
}
