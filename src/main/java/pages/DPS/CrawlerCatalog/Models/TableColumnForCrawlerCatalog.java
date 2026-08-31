package pages.DPS.CrawlerCatalog.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class TableColumnForCrawlerCatalog {
    private final String name;
    private final String dataType;
}
