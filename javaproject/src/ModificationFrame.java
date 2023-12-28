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

public class ModificationFrame extends Application {

    private Label messageLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modification Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(25, 25, 25, 25));

        // Set background color to LIGHTBLUE
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Create label "Enter the new student name"
        Label nameLabel = new Label("new student name:");
        gridPane.add(nameLabel, 0, 0);

        // Create TextField for the new student name
        TextField nameTextField = new TextField();
        nameTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(nameTextField, 1, 0);

        // Create label "Enter the new student year"
        Label yearLabel = new Label(" new student year:");
        gridPane.add(yearLabel, 0, 1);

        // Create TextField for the new student year
        TextField yearTextField = new TextField();
        yearTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(yearTextField, 1, 1);

        // Create label "Enter the new student section"
        Label sectionLabel = new Label(" new student section:");
        gridPane.add(sectionLabel, 0, 2);

        // Create TextField for the new student section
        TextField sectionTextField = new TextField();
        sectionTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(sectionTextField, 1, 2);

        // Create label "Enter the new student code"
        Label codeLabel = new Label(" new student code:");
        gridPane.add(codeLabel, 0, 3);

        // Create TextField for the new student code
        TextField codeTextField = new TextField();
        codeTextField.setPrefWidth(200); // Set preferred width
        gridPane.add(codeTextField, 1, 3);

        // Create "OK" button
        Button okButton = new Button("OK");
        gridPane.add(okButton, 1, 4);

        // Create message label
        messageLabel = new Label("");
        gridPane.add(messageLabel, 1, 5);

        // Set action for the "OK" button
        okButton.setOnAction(event -> {
            // Get the entered values
            String newName = nameTextField.getText();
            int newYear = Integer.parseInt(yearTextField.getText());
            String newSection = sectionTextField.getText();
            String newCode = codeTextField.getText();

            // Connect to the database and insert the new data
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentaff1", "root", "seif")) {
                String tableName = getTableName(newYear);
                String sql = "INSERT INTO " + tableName + " (student_name, year, section, code) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, newName);
                    preparedStatement.setInt(2, newYear);
                    preparedStatement.setString(3, newSection);
                    preparedStatement.setString(4, newCode);
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Check if the data was inserted successfully
                    if (rowsAffected > 0) {
                        messageLabel.setText("Data modified successfully");
                        System.out.println("Data modified successfully");
                    } else {
                        messageLabel.setText("Failed to modify data");
                        System.out.println("Failed to modify data");
                    }
                }
            } catch (SQLException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
        });

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
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

    void show() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
