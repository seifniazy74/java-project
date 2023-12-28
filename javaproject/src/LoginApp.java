import javafx.application.Application;
import javafx.geometry.Insets;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login page");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);
        gridPane.setHgap(15);

        // Set background color
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Add email label and text field
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 0);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(emailField, 1, 0);

        // Add password label and password field
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(passwordField, 1, 1);

        // Add user type choice box
        Label userTypeLabel = new Label("User Type:");
        GridPane.setConstraints(userTypeLabel, 0, 2);
        ChoiceBox<String> userTypeChoiceBox = new ChoiceBox<>();
        userTypeChoiceBox.getItems().addAll("Employee", "Admin");
        userTypeChoiceBox.setValue("Employee"); // Default value
        userTypeChoiceBox.setPrefWidth(250); // Set preferred width
        GridPane.setConstraints(userTypeChoiceBox, 1, 2);

        // Add OK button
        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Button color
        okButton.setPrefWidth(250); // Set preferred width
        okButton.setOnAction(e -> {
            // Handle the OK button action here
            String enteredEmail = emailField.getText();
            String enteredPassword = passwordField.getText();
            String userType = userTypeChoiceBox.getValue();

            // Check user input based on the selected user type
            if (authenticateUser(enteredEmail, enteredPassword, userType)) {
                // If authentication is successful, proceed
                if ("Admin".equals(userType)) {
                    // If user is an admin, open the SystemAdminFrame
                    openSystemAdminFrame(primaryStage);
                } else {
                    // If user is an employee, open the EmployeeFrame
                    openEmployeeFrame(primaryStage);
                }
            } else {
                showErrorAlert("Authentication Failed", "Invalid email or password. Please enter correct credentials.");
            }
        });
        GridPane.setConstraints(okButton, 1, 3);

        // Add Sign Up button
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Button color
        signUpButton.setPrefWidth(250); // Set preferred width
        signUpButton.setOnAction(e -> {
            // Handle the Sign Up button action here
            openSignUpFrame(primaryStage);
        });
        GridPane.setConstraints(signUpButton, 1, 4);

        // Add elements to the grid
        gridPane.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, userTypeLabel, userTypeChoiceBox, okButton, signUpButton);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateUser(String username, String password, String userType) {
        String query = "SELECT * FROM users WHERE user_name = ? AND password = ? AND user_type = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentaff1", "root", "seif");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, userType);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If there is a result, authentication is successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openSystemAdminFrame(Stage primaryStage) {
        SystemAdminFrame systemAdminFrame = new SystemAdminFrame();
        Stage systemAdminStage = new Stage();
        systemAdminFrame.start(systemAdminStage);
        primaryStage.close(); // Close the login stage
    }

    private void openEmployeeFrame(Stage primaryStage) {
        EmployeeFrame employeeFrame = new EmployeeFrame();
        Stage employeeStage = new Stage();
        employeeFrame.start(employeeStage);
        primaryStage.close(); // Close the login stage
    }

    private void openSignUpFrame(Stage primaryStage) {
        SignUpFrame signUpFrame = new SignUpFrame();
        Stage signUpStage = new Stage();
        signUpFrame.start(signUpStage);
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
