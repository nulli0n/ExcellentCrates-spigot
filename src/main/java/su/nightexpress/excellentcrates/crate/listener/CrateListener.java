package su.nightexpress.excellentcrates.crate.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.ClickType;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.manager.AbstractListener;

import java.util.stream.Stream;

public class CrateListener extends AbstractListener<CratesPlugin> {

    private final CrateManager crateManager;

    public CrateListener(@NotNull CratesPlugin plugin, @NotNull CrateManager crateManager) {
        super(plugin);
        this.crateManager = crateManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateBlockAssign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Dialog editor = Dialog.get(player);
        if (editor == null) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

        Crate crate = CrateUtils.getAssignBlockCrate(player);
        if (crate == null) return;

        crate.getBlockLocations().add(block.getLocation());
        crate.updateHologram();
        crate.saveSettings();
        Dialog.stop(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCrateUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Block block = null;
        Crate crate = null;

        if (item != null && !item.getType().isAir()) {
            crate = this.crateManager.getCrateByItem(item);
        }
        if (crate == null) {
            item = null;
            block = event.getClickedBlock();
            if (block == null) return;

            crate = this.crateManager.getCrateByBlock(block);
        }
        if (crate == null) {
            return;
        }

        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.DENY);

        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        ClickType clickType = ClickType.from(action, player.isSneaking());
        InteractType clickAction = Config.getCrateClickAction(clickType);
        if (clickAction == null) return;

        this.crateManager.interactCrate(player, crate, clickAction, item, block);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCratePlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (this.crateManager.isCrate(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateAnvilStop(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if ((first != null && this.crateManager.isCrate(first)) || (second != null && this.crateManager.isCrate(second))) {
            event.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCrateCraftStop(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.crateManager.isCrate(item))) {
            event.setCancelled(true);
        }
    }
}
