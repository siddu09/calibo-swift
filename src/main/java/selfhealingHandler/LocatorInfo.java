package selfhealingHandler;

public class LocatorInfo {

    private String primary;
    private String secondary;
    private String tertiary;

    public LocatorInfo(
            String primary,
            String secondary,
            String tertiary) {

        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
    }

    public String getPrimary() {
        return primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public String getTertiary() {
        return tertiary;
    }
}