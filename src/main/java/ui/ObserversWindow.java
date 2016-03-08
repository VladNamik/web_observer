package ui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 */
public class ObserversWindow extends Stage {
    private static Stage observersStage;

    public ObserversWindow() throws IOException
    {
        super();
        BorderPane root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml\\observers_window.fxml"));
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("WebObserver");
        observersStage = this;
    }

    public static Stage getObserversStage()
    {
        return observersStage;
    }
}
