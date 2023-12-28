import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class YearSelectionFrame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Year Selection Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);

        // Set background color
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Add "Select Year" label and ChoiceBox
        Label selectYearLabel = new Label("Select Year:");
        selectYearLabel.setStyle("-fx-font-size: 18;");
        gridPane.add(selectYearLabel, 0, 0);

        ChoiceBox<String> yearChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("First Year", "Second Year", "Third Year", "Fourth Year"));
        yearChoiceBox.setStyle("-fx-font-size: 18;");
        gridPane.add(yearChoiceBox, 1, 0);

        // Add ListView to display data
        ListView<String> dataListView = new ListView<>();
        dataListView.setStyle("-fx-font-size: 16;");
        // Set preferred size
        dataListView.setPrefSize(500, 300);
        gridPane.add(dataListView, 0, 2, 2, 1);

        // Add "Show Data" button
        Button showDataButton = new Button("Show Data");
        showDataButton.setStyle("-fx-font-size: 18;");
        gridPane.add(showDataButton, 1, 1);

        // Event handler for the "Show Data" button
        showDataButton.setOnAction((ActionEvent event) -> {
            String selectedYear = yearChoiceBox.getValue();
            if (selectedYear != null) {
                // Connect to the database and retrieve data based on the selected year
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentaff1", "root", "seif")) {
                    String tableName = getTableName(selectedYear);
                    String sql = "SELECT * FROM " + tableName;
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            ResultSet resultSet = preparedStatement.executeQuery()) {
                        
                        // Retrieve data from the ResultSet
                        List<String> data = new ArrayList<>();
                        while (resultSet.next()) {
                            String name = resultSet.getString("student_name");
                            int year = resultSet.getInt("year");
                            String section = resultSet.getString("section");
                            int code = resultSet.getInt("code");
                            data.add("Name: " + name + ", Year: " + year + ", Section: " + section + ", Code: " + code);
                        }
                        
                        // Display data in the ListView
                        ObservableList<String> items = FXCollections.observableArrayList(data);
                        dataListView.setItems(items);
                    }
                } catch (SQLException e) {
                    // Handle the exception appropriately

                }
            }
        });

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String getTableName(String selectedYear) {
        switch (selectedYear) {
            case "First Year":
                return "firstyear";
            case "Second Year":
                return "secondyear";
            case "Third Year":
                return "thirdyear";
            case "Fourth Year":
                return "forthyear";
            default:
                throw new IllegalArgumentException("Invalid year: " + selectedYear);
        }
    }
}
