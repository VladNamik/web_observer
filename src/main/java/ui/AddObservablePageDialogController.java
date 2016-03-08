package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.WindowEvent;
import web.Parser;
import web.observation.ObservablePage;

import java.io.IOException;

/**
 *
 */
public class AddObservablePageDialogController {

    @FXML
    private TextField textFieldURL;
    @FXML
    private RadioButton radioButtonStatic;
    @FXML
    private RadioButton radioButtonDynamic;





    public AddObservablePageDialogController()
    {

    }

    @FXML
    private void initialize()
    {
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(radioButtonDynamic, radioButtonStatic);
    }


    @FXML
    private void onCancelButton()
    {
        AddObservablePageDialog.getAddObservablePageStage().fireEvent(new WindowEvent(
                AddObservablePageDialog.getAddObservablePageStage(),
                WindowEvent.WINDOW_CLOSE_REQUEST
        ));
    }



    @FXML
    private void onOKButton()
    {

        ObservablePage page = new ObservablePage(textFieldURL.getText());

        if (radioButtonDynamic.isSelected())
            page.setObservationType(ObservablePage.ObservationType.DYNAMIC);
        else
            page.setObservationType(ObservablePage.ObservationType.STATIC);

        try {
            Parser.getInstance().savePage(page);
            AddObservablePageDialog.getAddObservablePageStage().fireEvent(new WindowEvent(
                    AddObservablePageDialog.getAddObservablePageStage(),
                    WindowEvent.WINDOW_CLOSE_REQUEST
            ));
        }
        catch(IOException | IllegalArgumentException e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Отсутствует подключение");
            alert.setHeaderText("Невозможно загрузить страницу");
            alert.setContentText("Убедитесь в наличии подключения к сети");

            alert.showAndWait();
        }
    }
}
