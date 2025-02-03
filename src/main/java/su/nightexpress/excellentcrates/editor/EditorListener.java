package su.nightexpress.excellentcrates.editor;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.dialog.DialogManager;

public class EditorListener extends AbstractListener<CratesPlugin> {

    public EditorListener(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateBlockAssign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Dialog editor = DialogManager.getDialog(player);
        if (editor == null) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

        Crate crate = CrateUtils.getAssignBlockCrate(player);
        if (crate == null) return;

        crate.addBlockPosition(block.getLocation());
        crate.saveSettings();
        crate.recreateHologram();

        DialogManager.stopDialog(player);
    }
}
