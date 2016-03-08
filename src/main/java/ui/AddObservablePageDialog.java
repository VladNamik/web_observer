package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 */
public class AddObservablePageDialog extends Stage {
    private static Stage addObservablePageStage;

    public AddObservablePageDialog() throws IOException
    {
        super();
        VBox root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml\\add_observable_page_dialog.fxml"));
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("ObservablePage");
        addObservablePageStage = this;
    }

    public static Stage getAddObservablePageStage() {
        return addObservablePageStage;
    }
}
