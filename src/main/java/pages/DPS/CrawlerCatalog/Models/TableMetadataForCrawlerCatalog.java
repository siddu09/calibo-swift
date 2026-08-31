package pages.DPS.CrawlerCatalog.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class TableMetadataForCrawlerCatalog {
    private final String tableName;
    private final List<TableColumnForCrawlerCatalog> columns;
}
