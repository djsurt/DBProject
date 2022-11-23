package gui;

import javafx.application.Application;
import javafx.beans.binding.DoubleExpression;
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

        Tab getStartedTab = new Tab("Get Started");
        Tab insertTab = new Tab("Insert");
        Tab deleteTab = new Tab("Delete");
        Tab updateTab = new Tab("Update");

        tabPane.getTabs().addAll(getStartedTab, insertTab, deleteTab, updateTab);

        VBox mainBox = new VBox(tabPane);

        final int PADDING = 10;

        DoubleExpression vboxHeight = primaryStage.heightProperty().multiply(.9);

        // Setting up the insert tab
        TabWindow insertWindow = new TabWindow(PADDING, vboxHeight);
        VBox insertBox = insertWindow.vBox();
        insertTab.setContent(insertBox);

        // Setting up the delete tab
        TabWindow deleteWindow = new TabWindow(PADDING, vboxHeight);
        VBox deleteBox = deleteWindow.vBox();
        deleteTab.setContent(deleteBox);

        // Setting up the update tab
        TabWindow updateWindow = new TabWindow(PADDING, vboxHeight);
        VBox updateBox = updateWindow.vBox();
        updateTab.setContent(updateBox);

        // Setting up the stage
        Scene scene = new Scene(mainBox);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // makes window take up full screen

        primaryStage.show();
    }
}
