package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.crate.RewardType;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class RewardOptionsMenu extends LinkedMenu<CratesPlugin, Reward> {

    private static final IconLocale LOCALE_ITEMS = LangEntry.iconBuilder("Editor.Button.Reward.Items").name("Items to Give")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Items", GENERIC_AMOUNT).br()
        .appendInfo("Gives listed items when won.").br()
        .appendClick("Click to open")
        .build();

    private static final IconLocale LOCALE_COMMANDS = LangEntry.iconBuilder("Editor.Button.Reward.Commands").name("Commands to Run")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Commands", GENERIC_AMOUNT).br()
        .appendInfo("Runs listed commands when won.").br()
        .appendClick("Click to edit")
        .build();

    private static final IconLocale LOCALE_PREVIEW_NORMAL = LangEntry.iconBuilder("Editor.Button.Reward.PreviewNormal")
        .name("Preview Item")
        .appendCurrent("Status", GENERIC_INSPECTION).br()
        .appendInfo("Drop an item on " + SOFT_YELLOW.wrap("this") + " button", "to replace the reward preview.")
        .build();

    private static final IconLocale LOCALE_PREVIEW_CUSTOM = LangEntry.iconBuilder("Editor.Button.Reward.PreviewCustom")
        .name("Preview Item")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Use Custom", GENERIC_STATE).br()
        .appendInfo("Drop an item on " + SOFT_YELLOW.wrap("this") + " button", "to replace the reward preview.").br()
        .appendClick("Click to toggle custom usage")
        .build();

    public static final IconLocale LOCALE_RARIRY_WEIGHT = LangEntry.iconBuilder("Editor.Button.Reward.RarityWeight")
        .name("Rarity & Weight")
        .appendCurrent("Rarity", REWARD_RARITY_NAME + " → " + SOFT_GREEN.wrap(REWARD_RARITY_ROLL_CHANCE + "%"))
        .appendCurrent("Weight", REWARD_WEIGHT + " → " + SOFT_GREEN.wrap(REWARD_ROLL_CHANCE + "%")).br()
        .appendClick("Click to change")
        .build();

    public static final IconLocale LOCALE_NAME = LangEntry.iconBuilder("Editor.Button.Reward.Name")
        .name("Display Name")
        .appendCurrent("Current", REWARD_NAME).br()
        .appendClick("Click to change")
        .build();

    public static final IconLocale LOCALE_DESCRIPTION = LangEntry.iconBuilder("Editor.Button.Reward.Description")
        .name("Description")
        .rawLore(REWARD_DESCRIPTION, EMPTY_IF_ABOVE)
        .appendClick("Click to change")
        .build();

    public static final IconLocale LOCALE_BROADCAST = LangEntry.iconBuilder("Editor.Button.Reward.Broadcast")
        .name("Win Broadcast")
        .appendCurrent("State", GENERIC_STATE).br()
        .appendInfo("Restricts player access to the reward", "based on their permissions.").br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale LOCALE_PERMISSIONS = LangEntry.iconBuilder("Editor.Button.Reward.Permissions")
        .name("Permissions")
        .appendCurrent("Total Permissions", GENERIC_AMOUNT).br()
        .appendInfo("Restrict reward access based on", "player's permissions.").br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale LOCALE_LIMITS = LangEntry.iconBuilder("Editor.Button.Reward.Limits")
        .name("Limits")
        .appendCurrent("State", GENERIC_STATE).br()
        .appendInfo("Controls how often and how many", "times this reward can be won.").br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale LOCALE_DELETE = LangEntry.iconBuilder("Editor.Button.Reward.Delete").accentColor(SOFT_RED)
        .name("Delete Reward")
        .appendInfo("Permanently deletes the reward.").br()
        .appendClick("Press " + TagWrappers.KEY.apply("key.drop") + " to delete")
        .build();

    public RewardOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_REWARD_SETTINGS.text());

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardList(viewer.getPlayer(), this.getLink(viewer).getCrate()));
        }));

        this.addItem(MenuItem.background(Material.GLASS_PANE, 19,20,21,22,23,24,25));
        this.addItem(MenuItem.background(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray()));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        Reward reward = this.getLink(player);
        Crate crate = reward.getCrate();
        Runnable flush = () -> this.flush(player);

        viewer.addItem(NightItem.fromItemStack(reward.getPreviewItem())
            .localized(reward.getType() == RewardType.ITEM ? LOCALE_PREVIEW_CUSTOM : LOCALE_PREVIEW_NORMAL)
            .replacement(replacer -> replacer
                .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_REWARD_PREVIEW, reward.getPreview().isValid()))
                .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(reward instanceof ItemReward itemReward && itemReward.isCustomPreview()))
            )
            .toMenuItem().setSlots(11).setHandler((viewer1, event) -> {
                ItemStack cursor = event.getCursor();
                if (cursor == null || cursor.getType().isAir()) {
                    if (reward instanceof ItemReward itemReward) {
                        itemReward.setCustomPreview(!itemReward.isCustomPreview());
                        crate.markDirty();
                        this.runNextTick(flush);
                    }
                    return;
                }

                ItemStack copy = new ItemStack(cursor);
                Players.addItem(player, copy);
                event.getView().setCursor(null);

                if (!ItemHelper.isCustom(copy)) {
                    reward.setPreview(ItemHelper.vanilla(copy));
                    crate.markDirty();
                    this.runNextTick(flush);
                }
                else {
                    CrateDialogs.REWARD_PREVIEW.ifPresent(dialog -> dialog.show(player, reward, copy, flush));
                }
            }).build()
        );

        if (reward instanceof ItemReward itemReward) {
            viewer.addItem(NightItem.fromType(Material.BUNDLE)
                .hideAllComponents()
                .localized(LOCALE_ITEMS)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_REWARD_ITEMS, itemReward.hasContent() && !itemReward.hasInvalidItems()))
                    .replace(GENERIC_AMOUNT, () -> itemReward.hasContent() ? CoreLang.goodEntry(String.valueOf(itemReward.countItems())) : CoreLang.badEntry(Lang.INSPECTIONS_REWARD_NO_ITEMS.text()))
                )
                .toMenuItem().setSlots(10).setHandler((viewer1, event) -> {
                    this.runNextTick(() -> plugin.getEditorManager().openRewardContent(viewer.getPlayer(), itemReward));
                }).build()
            );
        }
        else if (reward instanceof CommandReward commandReward) {
            viewer.addItem(NightItem.fromType(Material.COMMAND_BLOCK)
                .hideAllComponents()
                .localized(LOCALE_COMMANDS)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_REWARD_COMMANDS, commandReward.hasContent() && !commandReward.hasInvalidCommands()))
                    .replace(GENERIC_AMOUNT, () -> commandReward.hasContent() ? CoreLang.goodEntry(String.valueOf(commandReward.countCommands())) : CoreLang.badEntry(Lang.INSPECTIONS_REWARD_NO_COMMANDS.text()))
                )
                .toMenuItem().setSlots(10).setHandler((viewer1, event) -> {
                    CrateDialogs.REWARD_COMMANDS.ifPresent(dialog -> dialog.show(player, commandReward, flush));
                }).build()
            );

            viewer.addItem(NightItem.fromType(Material.NAME_TAG).localized(LOCALE_NAME)
                .replacement(replacer -> replacer.replace(reward.replacePlaceholders()))
                .toMenuItem().setSlots(30).setHandler((viewer1, event) -> {
                    CrateDialogs.REWARD_NAME.ifPresent(dialog -> dialog.show(player, commandReward, flush));
                }).build()
            );

            viewer.addItem(NightItem.fromType(Material.WRITABLE_BOOK).localized(LOCALE_DESCRIPTION)
                .replacement(replacer -> replacer.replace(reward.replacePlaceholders()))
                .toMenuItem().setSlots(32).setHandler((viewer1, event) -> {
                    CrateDialogs.REWARD_DESCRIPTION.ifPresent(dialog -> dialog.show(player, commandReward, flush));
                }).build()
            );
        }

        viewer.addItem(NightItem.fromType(Material.GLISTERING_MELON_SLICE).localized(LOCALE_RARIRY_WEIGHT)
            .replacement(replacer -> replacer.replace(reward.replacePlaceholders()))
            .toMenuItem().setSlots(12).setHandler((viewer1, event) -> {
                CrateDialogs.REWARD_WEIGHT.ifPresent(dialog -> dialog.show(player, reward, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.ENDER_PEARL).localized(LOCALE_BROADCAST)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(reward.isBroadcast())))
            .toMenuItem().setSlots(13).setHandler((viewer1, event) -> {
                reward.setBroadcast(!reward.isBroadcast());
                crate.markDirty();
                this.runNextTick(flush);
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.REDSTONE).localized(LOCALE_PERMISSIONS)
            .replacement(replacer -> replacer.replace(GENERIC_AMOUNT, () -> String.valueOf(reward.getIgnoredPermissions().size() + reward.getRequiredPermissions().size())))
            .toMenuItem().setSlots(14).setHandler((viewer1, event) -> {
                CrateDialogs.REWARD_PERMISSIONS.ifPresent(dialog -> dialog.show(player, reward, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.COMPARATOR).localized(LOCALE_LIMITS)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(reward.getLimits().isEnabled())))
            .toMenuItem().setSlots(15).setHandler((viewer1, event) -> {
                CrateDialogs.REWARD_LIMITS.ifPresent(dialog -> dialog.show(player, reward, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.BARRIER).localized(LOCALE_DELETE)
            .toMenuItem().setSlots(53).setHandler((viewer1, event) -> {
                if (event.getClick() != ClickType.DROP) return;

                crate.removeReward(reward);
                crate.markDirty();
                this.runNextTick(() -> this.plugin.getEditorManager().openRewardList(player, crate));
            }).build()
        );
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory()) {
            event.setCancelled(false);
        }
    }
}
