package su.nightexpress.excellentcrates.preview.view;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewContext;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.api.preview.crate.PreviewComponent;
import su.nightexpress.excellentcrates.preview.lang.PreviewLang;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class PreviewViewService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PreviewViewService.class);

    private final PreviewRegistry registry;

    public PreviewViewService(PreviewRegistry registry) {
        this.registry = registry;
    }

    public ActionResult openPreview(Player player, PreviewContext context) {
        Crate crate = context.crate();

        PreviewComponent previewComponent = crate.getComponentOrNull(CrateComponentKeys.PREVIEW);
        if (previewComponent == null || !previewComponent.isEnabled()) {
            return ActionResult.fail(PreviewLang.ERROR_NO_PREVIEW_COMPONENT);
        }

        AdaptedKey previewKey = previewComponent.getPreviewKey();

        Preview preview = this.registry.getPreviewByKey(previewKey);
        if (preview == null) {
            LOGGER.warn("{} tried to preview '{}' crate, but preview '{}' was not found.",
                player.getName(), crate.id(), previewKey
            );
            return ActionResult.fail(PreviewLang.ERROR_PREVIEW_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, previewKey::asString)
            );
        }

        return preview.show(player, context);
    }
}
