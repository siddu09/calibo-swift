package selfhealingHandler.model;

import java.util.Map;

public class ElementSnapshot {

    private int index;

    private String tag;
    private String text;
    private String id;
    private String name;
    private String className;
    private String placeholder;
    private String ariaLabel;
    private String role;
    private String dataTestId;
    private String href;
    private String type;

    private boolean visible;
    private boolean disabled;

    private double x;
    private double y;
    private double width;
    private double height;

    private Map<String, String> locatorCandidates;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }


    public String getAriaLabel() {
        return ariaLabel;
    }

    public void setAriaLabel(String ariaLabel) {
        this.ariaLabel = ariaLabel;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getDataTestId() {
        return dataTestId;
    }

    public void setDataTestId(String dataTestId) {
        this.dataTestId = dataTestId;
    }


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }


    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }


    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    public Map<String, String> getLocatorCandidates() {
        return locatorCandidates;
    }

    public void setLocatorCandidates(Map<String, String> locatorCandidates) {
        this.locatorCandidates = locatorCandidates;
    }
}