package selfhealingHandler.model;

import java.util.List;

public class PageSnapshot {

    private String pageName;
    private String url;
    private String title;
    private String capturedAt;
    private int elementCount;

    private List<ElementSnapshot> elements;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(String capturedAt) {
        this.capturedAt = capturedAt;
    }


    public int getElementCount() {
        return elementCount;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }


    public List<ElementSnapshot> getElements() {
        return elements;
    }

    public void setElements(List<ElementSnapshot> elements) {
        this.elements = elements;
    }
}