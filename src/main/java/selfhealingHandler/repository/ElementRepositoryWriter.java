package selfhealingHandler.repository;

import selfhealingHandler.model.PageSnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;

/**
 * Serializes a {@link PageSnapshot} (captured locator candidates) to a
 * pretty-printed {@code <PageName>.json} file.
 *
 * <p><b>Internal collaborator:</b> most callers should use
 * {@link selfhealingHandler.pipeline.LocatorPageObjectPipeline#run} instead of
 * calling this class directly - it wires crawling, JSON persistence, and
 * Page Object generation together in one call.</p>
 */
public class ElementRepositoryWriter {

    public void write(PageSnapshot pageSnapshot) {
        write(pageSnapshot, "src/main/java/selfhealing/repository");
    }

    public void write(PageSnapshot pageSnapshot, String directoryPath) {
        try {
            File directory = new File(directoryPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File outputFile = new File(
                    directory,
                    pageSnapshot.getPageName() + ".json"
            );

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            mapper.writeValue(outputFile, pageSnapshot);

            System.out.println("Locator repository generated: "
                    + outputFile.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to write locator repository for page: "
                            + pageSnapshot.getPageName(),
                    e
            );
        }
    }
}