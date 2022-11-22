import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Software Engineering Jobs Database");

        // Setting up tabs
        TabPane tabPane = new TabPane();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // removing close button from tabs

        Tab tab1 = new Tab("Planes");
        Tab tab2 = new Tab("Cars");
        Tab tab3 = new Tab("Boat");

        tabPane.getTabs().addAll(tab1, tab2, tab3);

        VBox mainBox = new VBox(tabPane);

        Button button = new Button("Click Me");

        VBox vbox1 = new VBox();
        tab1.setContent(vbox1);

        vbox1.getChildren().add(button);
        vbox1.setPadding(new Insets(10));

        // Setting up the stage
        Scene scene = new Scene(mainBox);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // makes window take up full screen

        primaryStage.show();
    }
}
