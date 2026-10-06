package su.nightexpress.excellentcrates.api.preview;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.nightcore.bridge.key.KeyHolder;

@NullMarked
public interface Preview extends KeyHolder {

    String getName();

    ActionResult show(Player player, PreviewContext context);
}
