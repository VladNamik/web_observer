package web.observation;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Страница, за которой ведётся наблюдение
 */
public class ObservablePage {
    public enum ObservationType // статически или динамически парсить
    {STATIC, DYNAMIC}

    private String url;//полный путь к странице
    private String title;
    private int sleepTime;
    private boolean changed;
    private ObservationType observationType = ObservationType.STATIC;
    private List<PageAction> pageActionList = new LinkedList<>();

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean isChanged) {
        this.changed = isChanged;
    }

    public ObservablePage(String url)
    {
        this.url = url;
    }

    public void addAction(PageAction action)
    {
        pageActionList.add(action);
    }

    public List<PageAction> getActionList()
    {
       return pageActionList;
    }

    public String getObservableHTML() throws IOException, NullPointerException, InterruptedException
    {
        Document document;
        if (observationType == ObservationType.DYNAMIC)
        {
            WebDriver driver = new FirefoxDriver();
            driver.get(url);
            Thread.sleep(sleepTime);
            document = Jsoup.parse(driver.getPageSource());
            //дописать возможность нажатия на кнопки
            driver.close();
        }
        else
        {
            document = Jsoup.connect(url).timeout(sleepTime).get();
        }
        title = document.title();

        Element element = document.getElementsByTag("html").first();
        for (PageAction action : pageActionList)
            element = action.action(element);
        return element.html();
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() throws IOException
    {
        if (title == null)
        {
            Document document;
            if (observationType == ObservationType.DYNAMIC)
            {
                WebDriver driver = new FirefoxDriver();
                driver.get(url);
                document = Jsoup.parse(driver.getPageSource());
                driver.close();
            }
            else
            {
                document = Jsoup.connect(url).timeout(sleepTime).get();
            }
            title = document.title();
        }
        return title;
    }

    public ObservationType getObservationType() {
        return observationType;
    }

    public void setObservationType(ObservationType observationType) {
        this.observationType = observationType;
    }

    public void setWaitingTime(int waitingTime)
    {
        sleepTime = waitingTime;
    }

    @Override
    public String toString() {
        return "URL = " + url + "\nType = " + observationType + "\nList=" + pageActionList.get(0).getType() + pageActionList.get(0).getTypeValue();
    }
}
