import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Subjectgeneration extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Subject Input Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(25, 25, 25, 25));

        // Set background color to LIGHTBLUE
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Create label "Enter the subject"
        TextField subjectTextField = new TextField();
        subjectTextField.setPromptText("Enter the subject");
        subjectTextField.setPrefWidth(200);
        gridPane.add(subjectTextField, 0, 0);

        // Create "OK" button
        Button okButton = new Button("OK");
        gridPane.add(okButton, 0, 1);

        // Create TextArea for displaying data
        TextArea dataTextArea = new TextArea();
        dataTextArea.setPrefRowCount(5);
        dataTextArea.setPrefColumnCount(20);
        gridPane.add(dataTextArea, 0, 2);

        // Set action for the "OK" button
        okButton.setOnAction(event -> {
            // Perform action on OK button click
            String enteredSubject = subjectTextField.getText();
            System.out.println("Subject entered: " + enteredSubject);

            // Retrieve and display data from the table
            displayTableData(enteredSubject, dataTextArea);
        });

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayTableData(String subjectName, TextArea dataTextArea) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentaff1", "root", "seif")) {
            String query = "SELECT * FROM " + subjectName;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Clear previous data
                dataTextArea.clear();

                // Display data in the TextArea
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int year = resultSet.getInt("year");
                    dataTextArea.appendText("Name: " + name + ", Year: " + year + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
