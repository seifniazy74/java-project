import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

public class SignUpFrame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(25, 25, 25, 25));

        // Set background color to LIGHTBLUE
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Create labels and text fields
        Label usernameLabel = new Label("New Username:");
        TextField usernameTextField = new TextField();
        Label passwordLabel = new Label("New Password:");
        PasswordField passwordField = new PasswordField();
        Label typeLabel = new Label("Type:");
        TextField typeTextField = new TextField();

        // Create "Sign Up" button
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Perform action on Sign Up button click
            String newUsername = usernameTextField.getText();
            String newPassword = passwordField.getText();
            String type = typeTextField.getText();

            if (!newUsername.isEmpty() && !newPassword.isEmpty() && !type.isEmpty()) {
                // Insert data into the database
                if (insertUserData(newUsername, newPassword, type)) {
                    showMessage("Success", "Your registration is successful");
                    // You can show a message or redirect to another page after successful registration
                } else {
                    showMessage("Error", "Error during registration");
                }
            } else {
                showMessage("Error", "Please fill in all fields");
            }
        });

        // Create "Back" button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            // Go back to the LoginApp frame
            LoginApp loginApp = new LoginApp();
            Stage loginStage = new Stage();
            loginApp.start(loginStage);
            primaryStage.close(); // Close the current stage
        });

        // Add elements to the grid
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameTextField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(typeLabel, 0, 2);
        gridPane.add(typeTextField, 1, 2);
        gridPane.add(signUpButton, 1, 3);
        gridPane.add(backButton, 0, 3);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean insertUserData(String username, String password, String userType) {
        String insertQuery = "INSERT INTO users (user_name, password, user_type) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentaff1", "root", "seif");
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, userType);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // If the insertion is successful, return true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
