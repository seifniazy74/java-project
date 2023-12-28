import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SubjectEntryFrame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Subject Entry Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);

        // Set background color to LIGHTBLUE
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Add subject label and text field
        Label subjectLabel = new Label("Subject:");
        GridPane.setConstraints(subjectLabel, 0, 0);

        TextField subjectField = new TextField();
        subjectField.setPromptText("Enter the subject");
        subjectField.setPrefWidth(200); // Set preferred width
        subjectField.setPrefHeight(30); // Set preferred height
        GridPane.setConstraints(subjectField, 1, 0);

        // Add OK button
        Button okButton = new Button("OK");
        okButton.setPrefWidth(200); // Set preferred width
        okButton.setPrefHeight(30); // Set preferred height
        okButton.setOnAction(event -> {
            // Handle the OK button action here
            String subject = subjectField.getText();
            System.out.println("Entered subject: " + subject);
        });
        GridPane.setConstraints(okButton, 1, 1);

        // Add elements to the grid
        gridPane.getChildren().addAll(subjectLabel, subjectField, okButton);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
