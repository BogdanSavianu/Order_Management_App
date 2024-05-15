package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The main class responsible for starting the JavaFX application.
 */
public class Start extends Application {

    /**
     * The entry point of the JavaFX application.
     *
     * @param primaryStage The primary stage of the application where the GUI will be displayed.
     * @throws Exception If an error occurs during the initialization or loading of the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the home page
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/HomePage.fxml")));

        // Set the title of the primary stage
        primaryStage.setTitle("Amazoo");

        // Set the scene with the loaded FXML file and specified dimensions
        primaryStage.setScene(new Scene(root, 600, 400));

        // Display the primary stage
        primaryStage.show();
    }

}
