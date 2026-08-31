package UI.PRO.datahelper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseTrainData {

    @JsonProperty("releaseTrain")
    private ReleaseTrain releaseTrain;

    public static ReleaseTrainData fromFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), ReleaseTrainData.class);
    }

    public String getName() {
        return releaseTrain != null ? releaseTrain.getName() : null;
    }

    public String getDescription() {
        return releaseTrain != null ? releaseTrain.getDescription() : null;
    }

    public List<Release> getReleases() {
        return releaseTrain != null ? releaseTrain.getReleases() : null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseTrain {
        private String name;
        private String description;
        private List<Release> releases;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Release {
        @JsonProperty("releaseName")
        private String releaseName;
        private String version;
        private String releaseId;
        private String releaseObjective;
        private String releaseManager;
        private String releaseType;
        private String impact;
        private String risk;
    }
}