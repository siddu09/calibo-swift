package UI.PRO.datahelper;

import lombok.Data;

import java.util.List;

@Data
public class FeatureData {

    private String product;
    private String title;
    private String description;
    private String status;
    private List<String> phases;
}