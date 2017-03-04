package ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import web.Parser;
import web.observation.ObservablePage;
import web.observation.PageAction;

import java.io.IOException;

/**
 *
 */
public class ObserversWindowController {

    private Parser parser = Parser.getInstance();
    private ObservablePage currentObservablePage;
    private int observableListSize;


    @FXML
    private Text bottomText;
    @FXML
    private ProgressBar progressBar;


    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;

    @FXML
    private ListView<ObserversListItem> observersListView;
    private ObservableList<ObserversListItem> observersList = FXCollections.observableArrayList();

    @FXML
    private Text titleText;

    @FXML
    private ListView<ActionsListItem> actionsListView;
    private ObservableList<ActionsListItem> actionsList = FXCollections.observableArrayList();


    public ObserversWindowController()
    {

    }


    @FXML
    private void initialize() {
        initializeObserversListView();


        actionsListView.setItems(actionsList);

        if (parser.getObservablePageList().size() > 0) {
            setCurrentObservablePage(parser.getObservablePageList().get(0));
        } else {
            setCurrentObservablePage(null);
        }
        if (observableListSize != 0)
            observersListView.getSelectionModel().select(observersListView.getItems().get(0));


    }


    private void initializeObserversListView()
    {

        for (ObservablePage page : parser.getObservablePageList())
                observersList.add(new ObserversListItem(page));
        observersListView.setItems(observersList);
        observersListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((Integer)newValue != -1)
                    setCurrentObservablePage(observersList.get((Integer) newValue).getPage());
                else
                    setCurrentObservablePage(null);
            }
        });
        observableListSize = observersList.size();

    }

    private void updateObserversListViewIfChanged()
    {
        if(observableListSize != parser.getObservablePageList().size())
        {
            observableListSize = parser.getObservablePageList().size();



            observersListView.getItems().clear();
            observersList.clear();
            initializeObserversListView();
            observersListView.getSelectionModel().select(observersListView.getItems().get(observableListSize - 1));
            observersListView.scrollTo(observersListView.getItems().get(observableListSize - 1));



            if (observableListSize != 0)
                setCurrentObservablePage(parser.getObservablePageList().get(observableListSize - 1));
            else
                setCurrentObservablePage(null);



        }
    }




    private void setCurrentObservablePage(ObservablePage page)
    {
        currentObservablePage = page;
        if (page == null)
        {
            titleText.setText("");
            actionsListView.getItems().clear();
        }
        else
        {
            try {
                titleText.setText(page.getTitle());
            }
            catch (IOException e)
            {
                titleText.setText("");
            }

            actionsListView.getItems().clear();
            actionsList.clear();
            currentObservablePage.getActionList().forEach(pageAction -> actionsList.add(new ActionsListItem(currentObservablePage,pageAction)) );

        }
    }





    @FXML
    private void onAddButtonClick()
    {
        try {
            AddObservablePageDialog addDialog = new AddObservablePageDialog();
            addDialog.initOwner(ObserversWindow.getObserversStage());
            addDialog.setOnCloseRequest(event -> updateObserversListViewIfChanged());//не вызывается при нажатии "принять" или "отмена"
            addDialog.show();
        }
        catch (IOException e)
        {
            bottomText.setText("Can't open observers window");
        }
    }


    @FXML
    private void onExitButtonClick()
    {
        Stage observersStage = ObserversWindow.getObserversStage();
        if (observersStage != null)
        {
            observersStage.close();
        }
    }


    @FXML
    private void onPlusButtonClick()
    {
        onAddButtonClick();
    }



    @FXML
    private void onMinusButtonClick()
    {
        try {
            parser.removePage(observersListView.getSelectionModel().getSelectedItem().getPage());
            observersList.remove(observersListView.getSelectionModel().getSelectedItem());
            observableListSize--;
        }
        catch (IOException e)
        {
            bottomText.setText("Remove failed");
        }
    }


    @FXML
    private void onTagButtonClick()
    {
        PageAction pageAction = new PageAction(PageAction.PageActionType.TAG, "");
        currentObservablePage.getActionList().add(pageAction);
        actionsList.add( new ActionsListItem(currentObservablePage, pageAction));
    }

    @FXML
    private void onClassButtonClick()
    {
        PageAction pageAction = new PageAction(PageAction.PageActionType.CLASS, "");
        currentObservablePage.getActionList().add(pageAction);
        actionsList.add( new ActionsListItem(currentObservablePage, pageAction));
    }

    @FXML
    private void onIdButtonClick()
    {
        PageAction pageAction = new PageAction(PageAction.PageActionType.ID, "");
        currentObservablePage.getActionList().add(pageAction);
        actionsList.add( new ActionsListItem(currentObservablePage, pageAction));
    }





    private class ObserversListItem extends Text
    {
        ObservablePage page;
        public ObserversListItem(ObservablePage page)
        {
            super();

            this.page = page;

            try {
                this.setText(page.getTitle());
            }
            catch (IOException e)
            {
                this.setText("");
            }
        }

        public ObservablePage getPage() {
            return page;
        }
    }





    private class ActionsListItem extends HBox
    {
        PageAction action;
        ObservablePage page;

        public ActionsListItem(ObservablePage page, PageAction action)
        {
            super();
            this.action = action;
            this.page = page;

            Text text;
            text = new Text(action.getType().toString());
            this.getChildren().add(text);


            TextField textField = new TextField();
            textField.setText(action.getTypeValue());
            textField.setOnMouseMoved(
                    event -> page.getActionList().get(page.getActionList().indexOf(action)).setTypeValue(textField.getText()));
            this.getChildren().add(textField);


            ImageView imageView = new ImageView(new javafx.scene.image.Image("fxml\\icons\\delete.png"));
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            Button button = new Button();
            button.setOnAction(new DeleteActionsListItemHandler());
            button.setGraphic(imageView);
            this.getChildren().add(button);
        }

        private class DeleteActionsListItemHandler implements EventHandler<ActionEvent>
        {
            @Override
            public void handle(ActionEvent event) {
                actionsList.remove(ActionsListItem.this);
                page.getActionList().remove(action);

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
