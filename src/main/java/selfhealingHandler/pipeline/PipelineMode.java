package selfhealingHandler.pipeline;

/**
 * Which flavour(s) of Page Object the {@link LocatorPageObjectPipeline} should
 * emit for a given locator snapshot.
 */
public enum PipelineMode {
    /** Crawl (and optionally persist JSON) only - no Page Object is generated. */
    NONE,
    /** Raw Playwright {@code Locator}, single most-stable candidate per element. */
    PLAIN,
    /** Fluent {@code ResilientLocator} fallback-chain per element. */
    SELF_HEALING,
    /** Generate both PLAIN and SELF_HEALING POMs in one run. */
    BOTH
}
