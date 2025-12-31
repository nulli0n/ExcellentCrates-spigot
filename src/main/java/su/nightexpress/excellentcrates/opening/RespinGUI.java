package su.nightexpress.excellentcrates.opening;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;

import java.util.List;

public class RespinGUI implements Listener, InventoryHolder {

    private final CratesPlugin plugin;
    private final Inventory inventory;
    private final Player player;
    private final Crate crate;
    private final CrateSource source;
    private final List<Reward> pendingRewards;

    // State to prevent double-processing (e.g. clicking button + closing menu)
    private boolean actionTaken = false;

    // Store slots dynamically loaded from config
    private final int slotClose;
    private final int slotAction;

    public RespinGUI(CratesPlugin plugin, Player player, Crate crate, CrateSource source, List<Reward> rewards) {
        this.plugin = plugin;
        this.player = player;
        this.crate = crate;
        this.source = source;
        this.pendingRewards = rewards;

        boolean isReroll = crate.isRespinRerollMode();

        // 1. Get Title from Config via RespinSettings
        String title = RespinSettings.getTitle(isReroll);

        this.inventory = Bukkit.createInventory(this, 27, title);

        // 2. Load Slots from Config via RespinSettings
        this.slotClose = RespinSettings.getSlot("Close", 11);
        this.slotAction = RespinSettings.getSlot("Action", 15);

        setupItems(isReroll);
    }

    private void setupItems(boolean isReroll) {
        double cost = crate.getRespinCost();
        String currency = crate.getRespinCurrency().toUpperCase();
        String costString = (cost > 0) ? "§6" + cost + " " + currency : "§aFREE";

        // --- CALCULATE LIMITS ---
        int limit = crate.getRespinLimit();
        int currentUsed = 0;
        if (player.hasMetadata("excellent_reroll_count")) {
            currentUsed = player.getMetadata("excellent_reroll_count").get(0).asInt();
        }
        int remaining = Math.max(0, limit - currentUsed);
        // ------------------------

        // 1. BACKGROUND FILLER
        ItemStack filler = RespinSettings.getItem("Filler", null);
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, filler);
        }

        // 2. REWARD DISPLAY
        if (!pendingRewards.isEmpty()) {
            Reward mainReward = pendingRewards.get(0);
            ItemStack winIcon = mainReward.getPreviewItem().clone();
            ItemMeta winMeta = winIcon.getItemMeta();

            if (winMeta != null) {
                String displayName;
                String rewardName = mainReward.getName();

                if (rewardName != null && !rewardName.contains("<lang")) {
                    displayName = rewardName;
                } else if (winMeta.hasDisplayName() && !winMeta.getDisplayName().contains("<lang")) {
                    displayName = winMeta.getDisplayName();
                } else {
                    displayName = formatMaterialName(winIcon.getType());
                }

                winMeta.setDisplayName("§a§lCurrent Prize: §f" + displayName);

                if (pendingRewards.size() > 1) {
                    List<String> lore = winMeta.hasLore() ? winMeta.getLore() : new java.util.ArrayList<>();
                    lore.add("");
                    lore.add("§e§l+ " + (pendingRewards.size() - 1) + " other rewards!");
                    winMeta.setLore(lore);
                }

                winMeta.addEnchant(Enchantment.LURE, 1, true);
                winMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                winIcon.setItemMeta(winMeta);
            }
            this.inventory.setItem(13, winIcon);
        }

        // 3. CLAIM / CLOSE BUTTON
        String keyClose = isReroll ? "Keep" : "Close";
        ItemStack closeBtn = RespinSettings.getItem(keyClose, null);
        this.inventory.setItem(slotClose, closeBtn);

        // 4. ACTION BUTTON
        String keyAction = isReroll ? "Reroll" : "Rebuy";
        ItemStack actionBtn = RespinSettings.getItem(keyAction, costString);

        // --- REPLACE LIMIT PLACEHOLDERS ---
        ItemMeta meta = actionBtn.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            List<String> newLore = new java.util.ArrayList<>();
            for (String line : lore) {
                newLore.add(line.replace("%remaining%", String.valueOf(remaining))
                        .replace("%limit%", String.valueOf(limit)));
            }
            meta.setLore(newLore);
            actionBtn.setItemMeta(meta);
        }
        // ----------------------------------

        this.inventory.setItem(slotAction, actionBtn);
    }

    // Helper to format "DIAMOND_SWORD" -> "Diamond Sword"
    private String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace("_", " ");
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)));
            sb.append(word.substring(1));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public void open() {
        player.openInventory(this.inventory);
    }

    /**
     * Called when the menu closes or the player clicks "Claim".
     * If in Reroll Mode, this gives the items (because we held them back).
     */
    public void givePendingRewards() {
        if (actionTaken) return;
        actionTaken = true;

        // If Reroll Mode: We haven't given items yet. Give them now.
        // If Rebuy Mode: We already gave items in AbstractOpening. Do nothing.
        if (crate.isRespinRerollMode()) {
            pendingRewards.forEach(r -> r.give(player));
            player.sendMessage("§aYou kept your prize.");
            player.removeMetadata("excellent_reroll_count", plugin);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Handles the "Spin Again" / "Reroll" logic
     */
    private void processAction() {
        if (actionTaken) return;

        double costVal = crate.getRespinCost();
        String currencyId = crate.getRespinCurrency();
        Currency currency = EconomyBridge.getCurrency(currencyId);

        // 1. Check Money
        if (costVal > 0) {
            if (currency == null || currency.getBalance(player) < costVal) {
                player.sendMessage("§c§l✘ §cNot enough funds!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                return;
            }
        }

        // 2. Handle Logic Split
        if (crate.isRespinRerollMode()) {
            // REROLL MODE: We set actionTaken = true, so givePendingRewards() is NEVER called.
            // This effectively "Trashes" the item.
            actionTaken = true;
            player.sendMessage("§cPrize discarded! §eRerolling...");
        } else {
            // REBUY MODE: Player already has the item. We just start a new spin.
            actionTaken = true;
        }

        // 3. Take Money
        if (costVal > 0 && currency != null) {
            currency.take(player, costVal);
        }

        // 4. Update Metadata Count
        int currentCount = 0;
        if (player.hasMetadata("excellent_reroll_count")) {
            currentCount = player.getMetadata("excellent_reroll_count").get(0).asInt();
        }
        player.setMetadata("excellent_reroll_count", new FixedMetadataValue(plugin, currentCount + 1));

        // 5. Close & Sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
        player.closeInventory();

        // 6. Start New Opening
        // We use a 2-tick delay to ensure the old inventory is fully closed on the client side
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                // Create a new opening session (null cost = bypass key check)
                Opening opening = plugin.getOpeningManager().createOpening(player, source, null);
                // Register it to start ticking
                plugin.getOpeningManager().startOpening(player, opening, false);
            }
        }, 2L);
    }

    // --- Static Listener ---
    // Registered in CratesPlugin.onEnable()
    public static class RespinListener implements Listener {

        @EventHandler
        public void onClick(InventoryClickEvent event) {
            if (!(event.getInventory().getHolder() instanceof RespinGUI)) return;
            event.setCancelled(true); // Prevent taking items

            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();
            RespinGUI gui = (RespinGUI) event.getInventory().getHolder();

            // Check dynamic slots
            if (event.getRawSlot() == gui.slotAction) {
                gui.processAction();
            } else if (event.getRawSlot() == gui.slotClose) {
                player.closeInventory(); // This triggers onClose
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent event) {
            if (event.getInventory().getHolder() instanceof RespinGUI) {
                RespinGUI gui = (RespinGUI) event.getInventory().getHolder();
                // Failsafe: If they press ESC, give them the item (if pending)
                gui.givePendingRewards();
            }
        }
    }
}