import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataEntryFrame extends Application {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentaff1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "seif";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Entry Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);

        // Set background color
        gridPane.setStyle("-fx-background-color: LIGHTBLUE;");

        // Add "Enter Student Name" label and text field
        Label nameLabel = new Label("Enter the Student Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameField = new TextField();
        GridPane.setConstraints(nameField, 1, 0);

        // Add "Enter Year" label and text field
        Label yearLabel = new Label("Enter the Year:");
        GridPane.setConstraints(yearLabel, 0, 1);
        TextField yearField = new TextField();
        GridPane.setConstraints(yearField, 1, 1);

        // Add "Enter Subject 1" label and text field
        Label sub1Label = new Label("Enter Subject 1:");
        GridPane.setConstraints(sub1Label, 0, 2);
        TextField sub1Field = new TextField();
        GridPane.setConstraints(sub1Field, 1, 2);

        // Add "Enter Code 1" label and text field
        Label code1Label = new Label("Enter Code 1:");
        GridPane.setConstraints(code1Label, 0, 3);
        TextField code1Field = new TextField();
        GridPane.setConstraints(code1Field, 1, 3);

        // Add "Enter Subject 2" label and text field
        Label sub2Label = new Label("Enter Subject 2:");
        GridPane.setConstraints(sub2Label, 0, 4);
        TextField sub2Field = new TextField();
        GridPane.setConstraints(sub2Field, 1, 4);

        // Add "Enter Code 2" label and text field
        Label code2Label = new Label("Enter Code 2:");
        GridPane.setConstraints(code2Label, 0, 5);
        TextField code2Field = new TextField();
        GridPane.setConstraints(code2Field, 1, 5);

        // Add "Enter Subject 3" label and text field
        Label sub3Label = new Label("Enter Subject 3:");
        GridPane.setConstraints(sub3Label, 0, 6);
        TextField sub3Field = new TextField();
        GridPane.setConstraints(sub3Field, 1, 6);

        // Add "Enter Code 3" label and text field
        Label code3Label = new Label("Enter Code 3:");
        GridPane.setConstraints(code3Label, 0, 7);
        TextField code3Field = new TextField();
        GridPane.setConstraints(code3Field, 1, 7);

        // Add OK button
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            // Handle the action for the OK button
            String studentName = nameField.getText();
            String yearStr = yearField.getText();
            String sub1 = sub1Field.getText();
            String sub2 = sub2Field.getText();
            String sub3 = sub3Field.getText();

            int code1 = 0;
            int code2 = 0;
            int code3 = 0;

            try {
                code1 = Integer.parseInt(code1Field.getText());
                code2 = Integer.parseInt(code2Field.getText());
                code3 = Integer.parseInt(code3Field.getText());
            } catch (NumberFormatException ex) {
                showErrorAlert("Error", "Please enter valid integer values for Code fields.");
                return;
            }

            if (studentName.isEmpty() || yearStr.isEmpty() || sub1.isEmpty() || sub2.isEmpty() || sub3.isEmpty()) {
                showErrorAlert("Error", "Please enter all the required information.");
            } else {
                int year = 0;

                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException ex) {
                    showErrorAlert("Error", "Please enter a valid integer value for Year.");
                    return;
                }

                // Insert data into the database
                insertDataIntoDatabase(studentName, year, sub1, code1, sub2, code2, sub3, code3);

                // Create tables for subjects
                createSubjectTable(sub1);
                createSubjectTable(sub2);
                createSubjectTable(sub3);

                showInfoAlert("Data Entered Successfully", "Student Name: " + studentName +
                        "\nYear: " + year +
                        "\nSubject 1: " + sub1 + " (Code: " + code1 + ")" +
                        "\nSubject 2: " + sub2 + " (Code: " + code2 + ")" +
                        "\nSubject 3: " + sub3 + " (Code: " + code3 + ")");
            }
        });
        GridPane.setConstraints(okButton, 1, 8);

        // Add elements to the grid
        gridPane.getChildren().addAll(nameLabel, nameField, yearLabel, yearField,
                sub1Label, sub1Field, code1Label, code1Field,
                sub2Label, sub2Field, code2Label, code2Field,
                sub3Label, sub3Field, code3Label, code3Field, okButton);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void insertDataIntoDatabase(String studentName, int year, String sub1, int code1, String sub2, int code2, String sub3, int code3) {
        try {
            // Load the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish a connection
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Insert data into the subjects table
                insertDataIntoSubjectsTable(con, studentName, year, sub1, code1, sub2, code2, sub3, code3);

                // Insert data into sub1, sub2, and sub3 tables
                insertDataIntoSubTable(con, sub1, studentName, year);
                insertDataIntoSubTable(con, sub2, studentName, year);
                insertDataIntoSubTable(con, sub3, studentName, year);
            }
        } catch (ClassNotFoundException | SQLException e) {
            showErrorAlert("Error", "Error inserting data into the database.");
        }
    }

    private void insertDataIntoSubjectsTable(Connection con, String studentName, int year, String sub1, int code1, String sub2, int code2, String sub3, int code3) throws SQLException {
        String sql = "INSERT INTO subjects (name, year, sub1, code1, sub2, code2, sub3, code3) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, year);
            preparedStatement.setString(3, sub1);
            preparedStatement.setInt(4, code1);
            preparedStatement.setString(5, sub2);
            preparedStatement.setInt(6, code2);
            preparedStatement.setString(7, sub3);
            preparedStatement.setInt(8, code3);
            preparedStatement.executeUpdate();
        }
    }

    private void insertDataIntoSubTable(Connection con, String tableName, String studentName, int year) throws SQLException {
        // Check if the table exists; if not, create it
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (name VARCHAR(255), year INT)";
        try (PreparedStatement createTableStatement = con.prepareStatement(createTableSQL)) {
            createTableStatement.executeUpdate();
        }

        // Insert data into the table
        String insertDataSQL = "INSERT INTO " + tableName + " (name, year) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(insertDataSQL)) {
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, year);
            preparedStatement.executeUpdate();
        }
    }

    private void createSubjectTable(String tableName) {
        try {
            // Load the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish a connection
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Check if the table exists; if not, create it
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (name VARCHAR(255), year INT)";
                try (PreparedStatement createTableStatement = con.prepareStatement(createTableSQL)) {
                    createTableStatement.executeUpdate();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            showErrorAlert("Error", "Error creating table for subject: " + tableName);
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
