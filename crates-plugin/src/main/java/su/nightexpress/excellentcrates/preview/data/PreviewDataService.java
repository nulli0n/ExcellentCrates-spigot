package su.nightexpress.excellentcrates.preview.data;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.data.io.PreviewIOService;

@NullMarked
public class PreviewDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewDataService.class);

    private final PreviewRegistry  registry;
    private final PreviewIOService ioService;

    public PreviewDataService(PreviewRegistry registry, PreviewIOService ioService) {
        this.registry = registry;
        this.ioService = ioService;
    }

    public void loadPreviews() {
        this.registry.getProviders().forEach(provider -> {
            this.ioService.loadPreviews(provider).forEach(preview -> {
                this.registry.registerPreview(preview);
                LOGGER.info("Loaded preview '{}' from '{}' provider", preview.key(), provider.getId());
            });
        });

        LOGGER.info("Loaded {} preview(s) from {} provider(s)",
            this.registry.getPreviews().size(),
            this.registry.getProviders().size()
        );
    }
}
