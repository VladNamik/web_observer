package ui;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import web.Parser;
import web.observation.ObservablePage;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер
 */
public class MainWindowController {

    private Parser parser = Parser.getInstance();

    @FXML
    private ListView<MainListItem> mainList;
    private ObservableList<MainListItem> mainObservableList = FXCollections.observableArrayList();

    @FXML
    private Text bottomText;
    @FXML
    private ProgressBar progressBar;



    public MainWindowController() {
    }



    @FXML
    private void initialize() {
        initializeMainList();
    }

    private void initializeMainList()
    {
        parser.getObservablePageList().forEach((page) -> {if (page.isChanged()) mainObservableList.add(new MainListItem(page));});
        mainList.setItems(mainObservableList);
        MainWindow.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) ->  mainObservableList.forEach((listElement) -> ((Text)listElement.getChildren().get(0)).setWrappingWidth((Double)newValue - 70.0)));
    }



    @FXML
    private void onResetButtonClick()
    {
        beginUpdating();
        new Thread(() ->{
            try {
                parser.checkAllPages();
                Platform.runLater( () -> {
                    updateMainObservableList();
                    endUpdating("All files have been updated");
                    }
                );
            }
            catch (IOException e)
            {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Отсутствует подключение");
                    alert.setHeaderText("Отсутствует подключение");
                    alert.setContentText("Убедитесь в наличии подключения к сети");

                    alert.showAndWait();
                    endUpdating("Updating have been failed");
                });
            }
            catch (NullPointerException e)
            {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ошибка в данных");
                    alert.setHeaderText("Ошибка в данных");
                    alert.setContentText("Убедитесь, что все данные для элементов введены правильно");

                    alert.showAndWait();
                    endUpdating("Updating have been failed");
                });
            }
        }
        ).start();
    }

    private void beginUpdating()
    {
        progressBar.setManaged(true);
        progressBar.setVisible(true);
        bottomText.setText("Updating...");
    }

    private void updateMainObservableList()
    {
        mainObservableList.clear();
        parser.getObservablePageList().forEach((page) -> {if (page.isChanged()) mainObservableList.add(new MainListItem(page));});
    }

    private void endUpdating(String text)
    {
        progressBar.setProgress(1);
        bottomText.setText(text);
        progressBar.setManaged(false);
        progressBar.setVisible(false);
        progressBar.setProgress(0);
    }




    @FXML
    private void onAddButtonClick()
    {
        try {
            ObserversWindow observersWindow = new ObserversWindow();
            observersWindow.setOnCloseRequest((event) ->
            {
                updateMainObservableList();
                try {
                    parser.saveConfigChanges();
                    bottomText.setText("Новые данные сохранены");
                }
                catch (IOException e)
                {
                    bottomText.setText("Не удалось сохранить данные");
                }
            });
            observersWindow.initOwner(MainWindow.getPrimaryStage());
            observersWindow.show();
        }
        catch (IOException e)
        {
            bottomText.setText("Can't open observers window");
            e.printStackTrace();
        }
    }




    @FXML
    private void onSettingsButtonClick()
    {
        //В разработке
    }



    @FXML
    private void onExitButtonClick()
    {
        Platform.exit();
    }





    private class MainListItem extends HBox
    {
        ObservablePage page;
        public MainListItem(ObservablePage page)
        {
            this.page = page;

            Text text;
            try {
                text = new Text(page.getTitle());
            }
            catch (IOException e)
            {
                text = new Text("unknown");
            }
            text.setWrappingWidth(MainWindow.getPrimaryStage().getWidth()-70.0);
            text.setOnMouseClicked((event) ->
            {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                            try {
                                Desktop.getDesktop().browse((new URL(page.getUrl())).toURI());
                            } catch (IOException | URISyntaxException e)
                            {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Неверное URL");
                                alert.setHeaderText("Неверный путь к файлу");
                                alert.setContentText("Убедитесь в том, что url введено правильно");
                                alert.showAndWait();
                            }
                    }
                }
            });
            this.getChildren().add(text);

            ImageView imageView = new ImageView(new Image("fxml\\icons\\delete.png"));
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);


            Button button = new Button();
            button.setOnAction(new DeleteMainListItemHandler());
            button.setGraphic(imageView);
            this.getChildren().add(button);
        }

        private class DeleteMainListItemHandler implements EventHandler<ActionEvent>
        {
            @Override
            public void handle(ActionEvent event) {
                mainObservableList.remove(MainListItem.this);
                page.setChanged(false);
                try {
                    parser.saveConfigChanges();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
