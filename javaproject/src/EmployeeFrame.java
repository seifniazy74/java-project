import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EmployeeFrame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Frame");

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(15);

        // Set background color
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, null, null);
        gridPane.setBackground(new Background(backgroundFill));

        // Add "Add New Student" button
        Button addStudentButton = createButton("Add New Student");
        GridPane.setConstraints(addStudentButton, 0, 0);

        // Add "Add Section" button
        Button addSectionButton = createButton("Add Section");
        GridPane.setConstraints(addSectionButton, 0, 1);

        // Add "Add Subject" button
        Button addSubjectButton = createButton("Add Subject");
        GridPane.setConstraints(addSubjectButton, 0, 2);

        // Add "Show Data" button
        Button showDataButton = createButton("Show Data");
        GridPane.setConstraints(showDataButton, 0, 3);

        // Add "Generate" button
        Button generateButton = createButton("Generate");
        GridPane.setConstraints(generateButton, 0, 4);

        // Add buttons to the grid
        gridPane.getChildren().addAll(addStudentButton, addSectionButton, addSubjectButton, showDataButton, generateButton);

        // Create scene and set stage
        Scene scene = new Scene(gridPane, 400, 400); // Larger size
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setOnAction((ActionEvent e) -> {
            // Handle the action for the button
            switch (text) {
                case "Add New Student":
                    Stage studentDataEntryStage = new Stage();
                    openStudentDataEntryFrame(studentDataEntryStage);
                    break;
                case "Add Section":
                    Stage sectionDataEntryStage = new Stage();
                    openSectionDataEntryFrame(sectionDataEntryStage);
                    break;
                case "Add Subject":
                    Stage dataEntryStage = new Stage();
                    openDataEntryFrame(dataEntryStage);
                    break;
                case "Generate":
                    // Open SubjectGeneration frame when Generate button is clicked
                    Stage subjectGenerationStage = new Stage();
                    openSubjectGenerationFrame(subjectGenerationStage);
                    break;
                case "Show Data":
                    // Open YearSelectionFrame when Show Data button is clicked
                    Stage yearSelectionStage = new Stage();
                    openYearSelectionFrame(yearSelectionStage);
                    break;
                default:
                    System.out.println("Button pressed: " + text);
                    break;
            }
        });
        button.setPrefWidth(250); // Set preferred width
        return button;
    }

    private void openStudentDataEntryFrame(Stage primaryStage) {
        StudentDataEntryFrame studentDataEntryFrame = new StudentDataEntryFrame();
        studentDataEntryFrame.start(primaryStage);
    }

    private void openSectionDataEntryFrame(Stage primaryStage) {
        SectionDataEntryFrame sectionDataEntryFrame = new SectionDataEntryFrame();
        sectionDataEntryFrame.start(primaryStage);
    }

    private void openDataEntryFrame(Stage primaryStage) {
        DataEntryFrame dataEntryFrame = new DataEntryFrame();
        dataEntryFrame.start(primaryStage);
    }

    private void openSubjectGenerationFrame(Stage primaryStage) {
        Subjectgeneration subjectGenerationFrame = new Subjectgeneration();
        subjectGenerationFrame.start(primaryStage);
    }

    private void openYearSelectionFrame(Stage primaryStage) {
        YearSelectionFrame yearSelectionFrame = new YearSelectionFrame();
        yearSelectionFrame.start(primaryStage);
    }
}
