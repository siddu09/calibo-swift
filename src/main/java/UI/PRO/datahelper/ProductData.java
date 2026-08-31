package UI.PRO.datahelper;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {

    private String portfolio;
    private String title;
    private String description;
    private String businessGroup;
    private List<String> phases;
}