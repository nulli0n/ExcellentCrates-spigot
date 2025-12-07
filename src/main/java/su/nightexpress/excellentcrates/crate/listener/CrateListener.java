package su.nightexpress.excellentcrates.crate.listener;

import org.bukkit.GameMode;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class CrateListener extends AbstractListener<CratesPlugin> {

    private final CrateManager manager;
    private final Set<UUID> adventureFix;

    public CrateListener(@NotNull CratesPlugin plugin, @NotNull CrateManager manager) {
        super(plugin);
        this.manager = manager;
        this.adventureFix = new HashSet<>();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.manager.removePreviewCooldown(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCrateUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        Crate crate = null;

        if (item != null && !item.getType().isAir()) {
            if (block != null) {
                if (this.manager.handleLinkToolInteraction(player, block, item, event)) return;
            }

            crate = this.manager.getCrateByItem(item);
        }
        if (crate == null) {
            item = null;
            if (block == null) return;

            crate = this.manager.getCrateByBlock(block);
        }
        if (crate == null) {
            return;
        }

        event.setUseItemInHand(Event.Result.DENY);

        // Do not deny left click interactions for adventure gamemode to prevent interaction spam when key is held.
        if (player.getGameMode() != GameMode.ADVENTURE || action == Action.RIGHT_CLICK_BLOCK) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }

        if (event.getHand() != EquipmentSlot.HAND) return;

        boolean isLeftClick = action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
        boolean isRightClick = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;

        InteractType clickAction = isLeftClick ? InteractType.CRATE_PREVIEW : (isRightClick ? InteractType.CRATE_OPEN : null);
        if (clickAction == null) return;
        if (Config.CRATE_REVERSE_CLICK_ACTIONS.get()) clickAction = clickAction.reversed();

        // Uh, adventure gamemode triggers LEFT_CLICK interaction on interactable blocks together with RIGHT_CLICK interaction.
        if (player.getGameMode() == GameMode.ADVENTURE && block != null && block.getType().isInteractable()) {
            if (action == Action.RIGHT_CLICK_BLOCK) {
                this.adventureFix.add(player.getUniqueId());
            }
            else if (action == Action.LEFT_CLICK_BLOCK && this.adventureFix.remove(player.getUniqueId())) {
                return;
            }
        }

        // We don't need cooldown check & apply when previewing crates from GUIs or commands. Only for world interaction.
        if (clickAction == InteractType.CRATE_PREVIEW && crate.isPreviewEnabled()) {
            if (this.manager.hasPreviewCooldown(player)) {
                Lang.CRATE_PREVIEW_ERROR_COOLDOWN.message().send(player, replacer -> replacer
                    .replace(Placeholders.GENERIC_TIME, TimeFormats.formatDuration(this.manager.getPreviewCooldown(player), TimeFormatType.LITERAL))
                );
                return;
            }
            this.manager.setPreviewCooldown(player);
        }

        this.manager.interactCrate(player, crate, clickAction, item, block);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCratePlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (this.manager.isCrate(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateAnvilStop(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if ((first != null && this.manager.isCrate(first)) || (second != null && this.manager.isCrate(second))) {
            event.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCrateCraftStop(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.manager.isCrate(item))) {
            event.setCancelled(true);
        }
    }
}
