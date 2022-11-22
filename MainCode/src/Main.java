import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        final int PADDING = 10;

        // Setting up the insert tab
        TabWindow insertWindow = new TabWindow(PADDING);
        VBox insertBox = insertWindow.vBox();
        insertTab.setContent(insertBox);

        // Setting up the delete tab
        TabWindow deleteWindow = new TabWindow(PADDING);
        VBox deleteBox = deleteWindow.vBox();
        deleteTab.setContent(deleteBox);

        // Setting up the stage
        Scene scene = new Scene(mainBox);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // makes window take up full screen

        primaryStage.show();
    }
}
