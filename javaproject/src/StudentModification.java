import javafx.application.Application;
import javafx.scene.Scene;
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

public class StudentModification extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Data Modification Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(25, 25, 25, 25));

        // Set background color to LIGHTBLUE
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Create label "Enter the student name"
        Label nameLabel = new Label(" name:");
        gridPane.add(nameLabel, 0, 0);

        // Create TextField for the student name
        TextField nameTextField = new TextField();
        nameTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(nameTextField, 1, 0);

        // Create label "Enter the student year"
        Label yearLabel = new Label("year:");
        gridPane.add(yearLabel, 0, 1);

        // Create TextField for the student year
        TextField yearTextField = new TextField();
        yearTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(yearTextField, 1, 1);

        // Create label "Enter the student section"
        Label sectionLabel = new Label(" section:");
        gridPane.add(sectionLabel, 0, 2);

        // Create TextField for the student section
        TextField sectionTextField = new TextField();
        sectionTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(sectionTextField, 1, 2);

        // Create label "Enter the student code"
        Label codeLabel = new Label(" code:");
        gridPane.add(codeLabel, 0, 3);

        // Create TextField for the student code
        TextField codeTextField = new TextField();
        codeTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(codeTextField, 1, 3);

        // Create "Delete" button
        Button deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 1, 4);

        // Set action for the "Delete" button
        deleteButton.setOnAction(event -> {
            // Handle the Delete button action (call the method to delete data)
            String studentName = nameTextField.getText();
            int studentYear = Integer.parseInt(yearTextField.getText());
            deleteStudentData(studentName, studentYear, sectionTextField, codeTextField);
        });

        // Create "Modify" button
        Button modifyButton = new Button("Modify");
        gridPane.add(modifyButton, 2, 4);

        // Set action for the "Modify" button
        modifyButton.setOnAction(event -> {
            // Open the ModificationFrame
            openModificationFrame();
        });

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openModificationFrame() {
        // Create an instance of the ModificationFrame and show it
        ModificationFrame modificationFrame = new ModificationFrame();
        modificationFrame.start(new Stage());
    }

    private void deleteStudentData(String studentName, int studentYear, TextField sectionTextField, TextField codeTextField) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentaff1", "root", "seif")) {
            String tableName = getTableName(studentYear);
            String sql = "DELETE FROM " + tableName + " WHERE student_name = ? AND year = ? AND section = ? AND code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, studentName);
                preparedStatement.setInt(2, studentYear);
                preparedStatement.setString(3, sectionTextField.getText()); // Use the text from the sectionTextField
                preparedStatement.setInt(4, Integer.parseInt(codeTextField.getText())); // Parse the text to int for code
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the data was deleted successfully
                if (rowsAffected > 0) {
                    System.out.println("Data deleted successfully");
                } else {
                    System.out.println("Failed to delete data");
                }
            }
        } catch (SQLException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }

    private String getTableName(int studentYear) {
        switch (studentYear) {
            case 1:
                return "firstyear";
            case 2:
                return "secondyear";
            case 3:
                return "thirdyear";
            case 4:
                return "forthyear";
            default:
                throw new IllegalArgumentException("Invalid year: " + studentYear);
        }
    }
}