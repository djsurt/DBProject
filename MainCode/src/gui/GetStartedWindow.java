package gui;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GetStartedWindow {

    private VBox vbox = new VBox();

    private final String fontFamily = "Helvetica";
    private final Font headingFont = Font.font(fontFamily, FontWeight.BOLD, FontPosture.REGULAR, 30);
    private final Font normalFont = Font.font(fontFamily, FontWeight.NORMAL, FontPosture.REGULAR, 15);

    public GetStartedWindow(int padding, DoubleExpression vboxHeight) {
        vbox.setPadding(new Insets(padding));
        vbox.prefHeightProperty().bind(vboxHeight);
        vbox.setSpacing(20);

        Text headingText = new Text("Computer Science Jobs Database");
        headingText.setFont(headingFont);

        Text bodyText = new Text(
                "CSDS 341: Introduction to Database Systems | Fall 2022 with Professor Dianne Foreback \n\nCreated by Eric Elizes, Omar Loudghiri, Georgia Martinez, Sam Michalski, and Dhananjay Surti"
        );
        bodyText.setFont(normalFont);
        bodyText.setLineSpacing(3);

        vbox.getChildren().addAll(headingText, bodyText);
    }

    public VBox vBox() {
        return vbox;
    }
}
