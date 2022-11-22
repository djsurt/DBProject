import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Computer Science Jobs Database");

        // Setting up tabs
        TabPane tabPane = new TabPane();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // removing close button from tabs

        Tab insertTab = new Tab("Insert");
        Tab deleteTab = new Tab("Delete");

        tabPane.getTabs().addAll(insertTab, deleteTab);

        VBox mainBox = new VBox(tabPane);

        // Setting up the insert tab
        VBox vbox1 = new VBox();

        insertTab.setContent(vbox1);
        vbox1.setPadding(new Insets(10));

        ComboBox<String> comboBox = TableDropdown.getComboBox();

        vbox1.getChildren().addAll(comboBox);

        // Setting up the delete tab
        VBox vbox2 = new VBox();
        deleteTab.setContent(vbox2);
        vbox2.setPadding(new Insets(10));

        ComboBox<String> comboBox2 = TableDropdown.getComboBox();

        vbox2.getChildren().addAll(comboBox2);

        // Setting up the stage
        Scene scene = new Scene(mainBox);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // makes window take up full screen

        primaryStage.show();
    }
}
