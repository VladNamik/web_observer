package web;

import io.FSHelper;
import web.observation.ObservablePage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Связь страниц и файлов
 */
public class Parser {
    private List<ObservablePage> observablePageList;
    private FSHelper fsHelper;
    private static Parser parser;

    private Parser()
    {
        fsHelper = new FSHelper();
        try {
            observablePageList = fsHelper.getConfigInfo();
        }
        catch (IOException e)
        {
            observablePageList = new LinkedList<>();
        }
    }

    public static synchronized Parser getInstance()
    {
        if (parser == null)
        {
            parser = new Parser();
        }
        return parser;
    }

    public List<ObservablePage> getObservablePageList() {
        return observablePageList;
    }

    public void saveConfigChanges() throws IOException
    {
        fsHelper.saveConfigInfo(observablePageList);
    }

    public void checkAllPages() throws IOException
    {
        checkPages(observablePageList);
    }

    public void checkPages(List<ObservablePage> pages) throws IOException
    {
        try {
            for (ObservablePage page : pages) {
                String observableHTML = page.getObservableHTML();
                String loadString = fsHelper.load(page.getUrl());
                if (!observableHTML.equals(loadString)) {
                    page.setChanged(true);
                    savePage(page, observableHTML);
                }

            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void savePage(ObservablePage page) throws IOException
    {
        try {
            savePage(page, page.getObservableHTML());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void savePage(ObservablePage page, String info) throws IOException
    {
        fsHelper.save(info, page.getUrl());
        if (!observablePageList.contains(page)) {
            observablePageList.add(page);
        }
        saveConfigChanges();
    }

    public void removePage(ObservablePage page) throws IOException
    {
        if (observablePageList.contains(page))
            observablePageList.remove(page);
        fsHelper.delete(page.getTitle());
        saveConfigChanges();
    }
}
