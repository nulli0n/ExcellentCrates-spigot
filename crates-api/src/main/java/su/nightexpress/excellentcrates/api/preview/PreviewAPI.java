package su.nightexpress.excellentcrates.api.preview;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.service.PluginAPI;

@NullMarked
public interface PreviewAPI extends PluginAPI {

    void registerProvider(PreviewProvider provider);

    void registerPreview(Preview preview);

    PreviewRegistry getRegistry();

    ActionResult openPreview(Player player, PreviewContext context);
}
