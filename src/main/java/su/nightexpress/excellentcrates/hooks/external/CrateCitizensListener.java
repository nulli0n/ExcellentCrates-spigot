package su.nightexpress.excellentcrates.hooks.external;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.type.ClickType;
import su.nexmedia.engine.hooks.external.citizens.CitizensListener;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.CrateClickAction;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.config.Config;

public class CrateCitizensListener implements CitizensListener {

    private final ExcellentCrates plugin;

    public CrateCitizensListener(@NotNull ExcellentCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLeftClick(NPCLeftClickEvent e) {
        this.process(e);
    }

    @Override
    public void onRightClick(NPCRightClickEvent e) {
        this.process(e);
    }

    private void process(@NotNull NPCClickEvent e) {
        int id = e.getNPC().getId();
        ICrate crate = this.plugin.getCrateManager().getCrateByNPC(id);
        if (crate == null) return;

        boolean isLeftEvent = e instanceof NPCLeftClickEvent;
        Player player = e.getClicker();
        Action action = isLeftEvent ? Action.LEFT_CLICK_AIR : Action.RIGHT_CLICK_AIR;
        ClickType clickType = ClickType.from(action, player.isSneaking());
        CrateClickAction clickAction = Config.getCrateClickAction(clickType);
        if (clickAction == null) return;

        this.plugin.getCrateManager().interactCrate(player, crate, clickAction, null, null);
    }
}
