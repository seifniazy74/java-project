import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDataEntryFrame extends Application {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentaff1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "seif";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Data Entry");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);

        // Set background color
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Add student name label and text field
        Label nameLabel = new Label("Enter the Student Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameField = new TextField();
        nameField.setPromptText("Enter the student name");
        nameField.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(nameField, 1, 0);

        // Add year label and text field
        Label yearLabel = new Label("Enter the Year:");
        GridPane.setConstraints(yearLabel, 0, 1);
        TextField yearField = new TextField();
        yearField.setPromptText("Enter the year");
        yearField.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(yearField, 1, 1);

        // Add section label and text field
        Label sectionLabel = new Label("Enter the Section:");
        GridPane.setConstraints(sectionLabel, 0, 2);
        TextField sectionField = new TextField();
        sectionField.setPromptText("Enter the section");
        sectionField.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(sectionField, 1, 2);

        // Add student code label and text field
        Label codeLabel = new Label("Enter the Code of Student:");
        GridPane.setConstraints(codeLabel, 0, 3);
        TextField codeField = new TextField();
        codeField.setPromptText("Enter the code of student");
        codeField.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(codeField, 1, 3);

        // Add OK button
        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Button color
        okButton.setPrefWidth(250); // Set preferred width
        okButton.setOnAction(e -> {
            // Handle the OK button action here
            String studentName = nameField.getText();
            String year = yearField.getText();
            String section = sectionField.getText();
            String studentCode = codeField.getText();

            if (!studentName.isEmpty() && !year.isEmpty() && !section.isEmpty() && !studentCode.isEmpty()) {
                insertDataIntoDatabase(studentName, year, section, studentCode);
            } else {
                showErrorAlert("Error", "Please enter all the required information.");
            }
        });
        GridPane.setConstraints(okButton, 1, 4);

        // Add elements to the grid
        gridPane.getChildren().addAll(nameLabel, nameField, yearLabel, yearField, sectionLabel, sectionField, codeLabel, codeField, okButton);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   private void insertDataIntoDatabase(String studentName, String year, String section, String studentCode) {
    try {
        // Load the JDBC driver
        Class.forName(JDBC_DRIVER);

        // Establish a connection
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             // Create a PreparedStatement with the query
             PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO "  + getTableName(Integer.parseInt(year)) + " (student_name, year, section, code) VALUES (?, ?, ?, ?)")) {

            // Set values for the parameters
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, Integer.parseInt(year));
            preparedStatement.setString(3, section);
            preparedStatement.setInt(4, Integer.parseInt(studentCode));

            // Execute the query
            preparedStatement.executeUpdate();
        }

        // Show success message
        showInfoAlert("Success", "Data added successfully.");

    } catch (ClassNotFoundException | SQLException e) {
        showErrorAlert("Error", "Error inserting data into the database.");
    }
}


    private String getTableName(int year) {
        switch (year) {
            case 1:
                return "firstyear";
            case 2:
                return "secondyear";
            case 3:
                return "thirdyear";
            case 4:
                return "forthyear";
            default:
                throw new IllegalArgumentException("Invalid year: " + year);
        }
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
