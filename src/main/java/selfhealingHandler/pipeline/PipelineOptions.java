package selfhealingHandler.pipeline;

/**
 * Options controlling a single {@link LocatorPageObjectPipeline} run.
 *
 * <p>Immutable; build via {@link #builder()}.</p>
 */
public final class PipelineOptions {

    private final String jsonOutputDir;
    private final String pomOutputDir;
    private final String selfHealingPomOutputDir;
    private final PipelineMode mode;

    private PipelineOptions(Builder b) {
        this.jsonOutputDir = b.jsonOutputDir;
        this.pomOutputDir = b.pomOutputDir;
        this.mode = b.mode;

        // In BOTH mode, PLAIN and SELF_HEALING generate the same class/file
        // name (e.g. "DemoPage.java"); writing both to the same directory
        // would silently overwrite one with the other. Default the
        // self-healing output to a "selfhealing" sub-directory unless the
        // caller explicitly overrides it.
        if (b.selfHealingPomOutputDir != null) {
            this.selfHealingPomOutputDir = b.selfHealingPomOutputDir;
        } else if (b.mode == PipelineMode.BOTH && b.pomOutputDir != null) {
            this.selfHealingPomOutputDir = b.pomOutputDir + "/selfhealing";
        } else {
            this.selfHealingPomOutputDir = b.pomOutputDir;
        }
    }

    /** Directory the crawled {@code PageSnapshot} JSON is persisted to. Null = skip persisting. */
    public String getJsonOutputDir() {
        return jsonOutputDir;
    }

    /** Directory the PLAIN Page Object {@code .java} file is written to. */
    public String getPomOutputDir() {
        return pomOutputDir;
    }

    /**
     * Directory the SELF_HEALING Page Object {@code .java} file is written to.
     * Defaults to {@code pomOutputDir + "/selfhealing"} when {@link #getMode()}
     * is {@link PipelineMode#BOTH} (to avoid overwriting the PLAIN file of the
     * same name), otherwise defaults to {@code pomOutputDir}.
     */
    public String getSelfHealingPomOutputDir() {
        return selfHealingPomOutputDir;
    }

    public PipelineMode getMode() {
        return mode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String jsonOutputDir = "src/main/java/selfhealing/repository";
        private String pomOutputDir = "src/main/java/pages/generated";
        private String selfHealingPomOutputDir;
        private PipelineMode mode = PipelineMode.BOTH;

        private Builder() {
        }

        /** Set to {@code null} to skip writing the JSON locator snapshot entirely. */
        public Builder jsonOutputDir(String jsonOutputDir) {
            this.jsonOutputDir = jsonOutputDir;
            return this;
        }

        public Builder pomOutputDir(String pomOutputDir) {
            this.pomOutputDir = pomOutputDir;
            return this;
        }

        /** Explicit override for where the SELF_HEALING POM is written (see {@link #getSelfHealingPomOutputDir()}). */
        public Builder selfHealingPomOutputDir(String selfHealingPomOutputDir) {
            this.selfHealingPomOutputDir = selfHealingPomOutputDir;
            return this;
        }

        public Builder mode(PipelineMode mode) {
            this.mode = mode;
            return this;
        }

        public PipelineOptions build() {
            if (mode != PipelineMode.NONE && (pomOutputDir == null || pomOutputDir.isBlank())) {
                throw new IllegalStateException("pomOutputDir must be set unless mode is NONE");
            }
            if (mode == null) {
                throw new IllegalStateException("mode must be set");
            }
            return new PipelineOptions(this);
        }
    }
}
