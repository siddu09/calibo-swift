package selfhealingHandler.pipeline;

import java.nio.file.Path;

/**
 * Outcome of a single {@link LocatorPageObjectPipeline} run for one page.
 *
 * @param pageName          logical page name used to derive file/class names
 * @param jsonPath          path to the persisted locator snapshot JSON, or {@code null} if not written
 * @param plainPomPath      path to the generated PLAIN Page Object, or {@code null} if not requested
 * @param selfHealingPomPath path to the generated SELF_HEALING Page Object, or {@code null} if not requested
 */
public record PipelineResult(
        String pageName,
        Path jsonPath,
        Path plainPomPath,
        Path selfHealingPomPath) {
}
