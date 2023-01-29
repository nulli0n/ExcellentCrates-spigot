package su.nightexpress.excellentcrates.crate;

import org.bukkit.Material;
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
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nexmedia.engine.api.type.ClickType;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.CrateClickAction;
import su.nightexpress.excellentcrates.config.Config;

import java.util.stream.Stream;

public class CrateListener extends AbstractListener<ExcellentCrates> {

    private final CrateManager crateManager;

    public CrateListener(@NotNull CrateManager crateManager) {
        super(crateManager.plugin());
        this.crateManager = crateManager;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCrateUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        Block block = null;
        Crate crate = null;

        if (item != null && !item.getType().isAir()) {
            crate = this.crateManager.getCrateByItem(item);
        }
        if (crate == null) {
            item = null;
            block = e.getClickedBlock();
            if (block == null || (e.useInteractedBlock() == Event.Result.DENY && block.getType() != Material.BARRIER)) return;

            crate = this.crateManager.getCrateByBlock(block);
        }
        if (crate == null) {
            return;
        }

        e.setUseItemInHand(Event.Result.DENY);
        e.setUseInteractedBlock(Event.Result.DENY);

        if (e.getHand() != EquipmentSlot.HAND) return;

        Action action = e.getAction();
        ClickType clickType = ClickType.from(action, player.isSneaking());
        CrateClickAction clickAction = Config.getCrateClickAction(clickType);
        if (clickAction == null) return;

        this.crateManager.interactCrate(player, crate, clickAction, item, block);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCratePlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (this.crateManager.isCrate(item)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateAnvilStop(PrepareAnvilEvent e) {
        AnvilInventory inventory = e.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if ((first != null && this.crateManager.isCrate(first)) || (second != null && this.crateManager.isCrate(second))) {
            e.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCrateCraftShop(CraftItemEvent e) {
        CraftingInventory inventory = e.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.crateManager.isCrate(item))) {
            e.setCancelled(true);
        }
    }
}
