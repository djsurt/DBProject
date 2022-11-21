import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane layout = new StackPane();

        Button button = new Button("Click Me");
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 300, 300);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Title of Window");
        primaryStage.show();
    }
}
