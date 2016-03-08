package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainWindow;
import web.Parser;
import web.observation.ObservablePage;
import web.observation.PageAction;

import java.io.IOException;

public class Main{

    public static void main(String[] args) throws IOException, InterruptedException{
        Parser parser = Parser.getInstance();
//        String[] strings = {"http://amovies.org/serials/3717-legendy-zavtrashnego-dnya.html",
//                "http://amovies.org/serials/3498-lyuter.html",
//                "http://amovies.org/serials/3184-flesh.html",
//                "http://amovies.org/serials/3317-grimm.html",
//                "http://amovies.org/serials/3851-11-22-63.html",
//                "http://amovies.org/serials/3033-oblasti-tmy.html",
//                "http://amovies.org/serials/3628-lyucifer.html",
//                "http://amovies.org/serials/2487-superdevushka.html",
//                "http://amovies.org/serials/3008-gotem.html",
//                "http://amovies.org/serials/3212-strela.html",
//                "http://amovies.org/serials/2637-oboroten.html",
//                "http://amovies.org/serials/3210-sverhestestvennoe.html",
//                "http://amovies.org/serials/2611-fors-mazhory.html"};
//        for (String s : strings) {
//            ObservablePage page = new ObservablePage(s);
//            page.setObservationType(ObservablePage.ObservationType.STATIC);
//            page.addAction(new PageAction(PageAction.PageActionType.CLASS, "arhive_news"));
//            parser.savePage(page);
//        }
         parser.checkAllPages();
        for (ObservablePage page : parser.getObservablePageList())
            if (page.isChanged())
                System.out.println(page.getUrl());
    }
}
