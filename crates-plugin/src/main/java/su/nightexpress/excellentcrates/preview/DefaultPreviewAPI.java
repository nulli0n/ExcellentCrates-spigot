package su.nightexpress.excellentcrates.preview;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewAPI;
import su.nightexpress.excellentcrates.api.preview.PreviewContext;
import su.nightexpress.excellentcrates.api.preview.PreviewProvider;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.view.PreviewViewService;

@NullMarked
public class DefaultPreviewAPI implements PreviewAPI {

    private final PreviewRegistry    registry;
    private final PreviewViewService viewService;

    public DefaultPreviewAPI(PreviewRegistry registry, PreviewViewService viewService) {
        this.registry = registry;
        this.viewService = viewService;
    }

    @Override
    public PreviewRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public ActionResult openPreview(Player player, PreviewContext context) {
        return this.viewService.openPreview(player, context);
    }

    @Override
    public void registerPreview(Preview preview) {
        this.registry.registerPreview(preview);
    }

    @Override
    public void registerProvider(PreviewProvider provider) {
        this.registry.registerProvider(provider);
    }
}
