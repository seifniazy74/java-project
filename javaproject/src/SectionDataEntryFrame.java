import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SectionDataEntryFrame extends Application {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentaff1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "seif";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Section Data Entry");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);

        // Set background color
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Add section name label and text field
        Label nameLabel = new Label("Enter the Section Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameField = new TextField();
        nameField.setPromptText("Enter the Section Name");
        nameField.setPrefWidth(250);
        GridPane.setConstraints(nameField, 1, 0);

        // Add section code label and text field
        Label sectionLabel = new Label("Enter the code of the section:");
        GridPane.setConstraints(sectionLabel, 0, 1);
        TextField sectionField = new TextField();
        sectionField.setPromptText("Enter the code of the section");
        sectionField.setPrefWidth(250);
        GridPane.setConstraints(sectionField, 1, 1);

        // Add OK button
        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        okButton.setPrefWidth(250);
        okButton.setOnAction(e -> {
            // Handle the OK button action here
            String sectionName = nameField.getText();
            String sectionCode = sectionField.getText();

            if (!sectionName.isEmpty() && !sectionCode.isEmpty()) {
                insertDataIntoDatabase(sectionName, sectionCode);
            } else {
                showErrorAlert("Error", "Please enter all the required information.");
            }
        });
        GridPane.setConstraints(okButton, 1, 2);

        // Add elements to the grid
        gridPane.getChildren().addAll(nameLabel, nameField, sectionLabel, sectionField, okButton);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void insertDataIntoDatabase(String sectionName, String sectionCode) {
        try {
            // Load the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish a connection
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 // Create a PreparedStatement with the query
                 PreparedStatement preparedStatement = con.prepareStatement(
                         "INSERT INTO sections (section_name, section_code) VALUES (?, ?)")) {

                // Set values for the parameters
                preparedStatement.setString(1, sectionName);
                preparedStatement.setString(2, sectionCode);

                // Execute the query
                preparedStatement.executeUpdate();

                // Show success message
                showInfoAlert("Success", "Data added successfully.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            showErrorAlert("Error", "Error inserting data into the database.");
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
