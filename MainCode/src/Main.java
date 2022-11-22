import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

        Tab tab1 = new Tab("Planes", new Label("Show all planes available"));
        Tab tab2 = new Tab("Cars"  , new Label("Show all cars available"));
        Tab tab3 = new Tab("Boats" , new Label("Show all boats available"));

        tabPane.getTabs().addAll(tab1, tab2, tab3);

        VBox vBox = new VBox(tabPane);

        Button button = new Button("Click Me");
        vBox.getChildren().add(button);

        Scene scene = new Scene(vBox);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        primaryStage.show();
    }
}
