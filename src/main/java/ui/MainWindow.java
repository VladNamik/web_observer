package ui;

/**
 **
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainWindow extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        BorderPane root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml\\main_window.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("WebObserver");
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
