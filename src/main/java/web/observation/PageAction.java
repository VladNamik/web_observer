package web.observation;

import org.jsoup.nodes.Element;

/**
 * Описывает действие, которое можно сделать со страницей
 */
public class PageAction
{
    public enum PageActionType
    {TAG, CLASS, ID, BUTTON_CLICK}

    private PageActionType type;
    private String typeValue;

    public PageAction( PageActionType type, String typeValue)
    {
        this.type = type;
        this.typeValue = typeValue;
    }

    Element action(Element element) throws NullPointerException
    {
        switch(type)
        {
            case TAG: return element.getElementsByTag(typeValue).first();

            case CLASS: return element.getElementsByClass(typeValue).first();

            case ID: return element.getElementById(typeValue);

            case BUTTON_CLICK:
                //здесь должна быть реализация
                return element;
            default:
                return element;
        }
    }

    public PageActionType getType() {
        return type;
    }


    public void setTypeValue(String typeValue)
    {
        this.typeValue = typeValue;
    }
    public String getTypeValue() {
        return typeValue;
    }
}
