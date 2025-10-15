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
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;
import su.nightexpress.nightcore.util.time.TimeFormats;

import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateOptionsMenu extends LinkedMenu<CratesPlugin, Crate> implements LangContainer {

    private static final IconLocale LOCALE_DELETE = LangEntry.iconBuilder("Editor.Button.Crate.Delete").accentColor(RED).name("Delete Crate")
        .appendInfo("Permanently deletes the crate.").br()
        .appendClick("Press [" + TagWrappers.KEY.apply("key.drop") + "] to delete")
        .build();

    private static final IconLocale LOCALE_NAME = LangEntry.iconBuilder("Editor.Button.Crate.DisplayName").name("Name")
        .appendCurrent("Current", CRATE_NAME).br()
        .appendInfo("Sets crate display name.").br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_DESCRIPTION = LangEntry.iconBuilder("Editor.Button.Crate.Description").name("Description")
        .rawLore(CRATE_DESCRIPTION, EMPTY_IF_ABOVE)
        .appendInfo("Sets crate description.").br()
        .appendClick("Click to edit")
        .build();

    private static final IconLocale LOCALE_ITEM = LangEntry.iconBuilder("Editor.Button.Crate.Item").name("Crate Item")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Stackable", GENERIC_STATE).br()
        .appendInfo("Drop an item on " + SOFT_YELLOW.wrap("this") + " button", "to replace the crate's item.").br()
        .appendClick("Click to toggle stacking")
        .build();

    private static final IconLocale LOCALE_PREVIEW_SET = LangEntry.iconBuilder("Editor.Button.Crate.Preview.Set").name("Preview GUI")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Preview ID", GENERIC_VALUE).br()
        .appendInfo("Sets preview GUI for the crate.").br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_PREVIEW_UNSET = LangEntry.iconBuilder("Editor.Button.Crate.Preview.Unset").name("Preview GUI")
        .appendCurrent("Status", RED.wrap("Disabled")).br()
        .appendInfo("Sets preview GUI for this crate.").br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_OPENING_SET = LangEntry.iconBuilder("Editor.Button.Crate.Opening.Set").name("Opening Animation")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Preview ID", GENERIC_VALUE).br()
        .appendInfo("Sets opening animation for the crate.").br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_OPENING_UNSET = LangEntry.iconBuilder("Editor.Button.Crate.Opening.Unset").name("Opening Animation")
        .appendCurrent("Status", RED.wrap("Disabled")).br()
        .appendInfo("Sets opening animation for the crate.").br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_LINKED_BLOCKS = LangEntry.iconBuilder("Editor.Button.Crate.LinkedBlocks")
        .name("Linked Block")
        .rawLore(DARK_GRAY.wrap("Press " + GOLD.wrap("[" + TagWrappers.KEY.apply("key.drop") + "]") + " to unlink.")).br()
        .appendCurrent("Linked", GENERIC_STATE).br()
        .appendInfo("Link the crate to a block by", "using the link tool.").br()
        .appendInfo("Interacting with the linked block", "will preview and open the crate.").br()
        .appendClick("Click to get link tool")
        .build();

    private static final IconLocale LOCALE_BLOCK_PUSHBACK = LangEntry.iconBuilder("Editor.Button.Crate.BlockPushback")
        .name("Block Pushback")
        .appendCurrent("Status", GENERIC_STATE).br()
        .appendInfo("Pushes players back from the crate", "if they don't met the requirements.").br()
        .appendClick("Click to toggle")
        .build();

    private static final IconLocale LOCALE_COST_OPTIONS = LangEntry.iconBuilder("Editor.Button.Crate.CostOptions").name("Cost Options")
        .appendInfo("Here you can set the " + SOFT_YELLOW.wrap("'cost'"), "required to open the crate - ", "it can be " + SOFT_YELLOW.wrap("keys") + ", " + SOFT_YELLOW.wrap("currency") + ", or both.").br()
        .appendInfo("You can add multiple cost options,", "allowing players to choose how", "they want to open the crate.").br()
        .appendClick("Click to open")
        .build();

    private static final IconLocale LOCALE_PERMISSION_REQUIREMENT = LangEntry.iconBuilder("Editor.Button.Crate.Permission").name("Permission Requirement")
        .appendCurrent("Status", GENERIC_STATE)
        .appendCurrent("Permission", GENERIC_VALUE).br()
        .appendInfo("Controls whether permission is", "required to open the crate.").br()
        .appendClick("Click to toggle")
        .build();

    private static final IconLocale LOCALE_OPENING_COOLDOWN = LangEntry.iconBuilder("Editor.Button.Crate.OpeningCooldown").name("Opening Cooldown")
        .appendCurrent("Enabled", GENERIC_STATE)
        .appendCurrent("Current", GENERIC_VALUE).br()
        .appendInfo("Sets the crate's cooldown time.").br()
        .appendClick("Click to edit")
        .build();

    private static final IconLocale LOCALE_EFFECT = LangEntry.iconBuilder("Editor.Button.Crate.Effect").name("Crate Effect")
        .appendCurrent("Model", GENERIC_TYPE)
        .appendCurrent("Particle", GENERIC_VALUE).br()
        .appendInfo("Sets effect for the crate block(s).").br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale LOCALE_HOLOGRAM = LangEntry.iconBuilder("Editor.Button.Crate.Hologram").name("Crate Hologram")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("State", GENERIC_STATE)
        .appendCurrent("Template", GENERIC_VALUE).br()
        .appendInfo("Auto-manageable hologram above", "the linked crate block.").br()
        .appendClick("Click to edit")
        .build();

    private static final IconLocale LOCALE_REWARDS = LangEntry.iconBuilder("Editor.Button.Crate.Rewards").name("Rewards")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Rewards", GENERIC_AMOUNT).br()
        .appendInfo("Add and manage crate's rewards!").br()
        .appendClick("Click to open")
        .build();

    private static final IconLocale LOCALE_MILESTONES = LangEntry.iconBuilder("Editor.Button.Crate.Milestones").name("Milestones")
        .appendCurrent("Milestones", GENERIC_AMOUNT).br()
        .appendInfo("Create custom milestones with", "rewards for the crate!").br()
        .appendClick("Click to open")
        .build();

    public CrateOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_CRATE_SETTINGS.text());
        this.plugin.injectLang(this);

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openCrateList(viewer.getPlayer()));
        }));

        this.addItem(MenuItem.background(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray()));
        this.addItem(MenuItem.background(Material.GLASS_PANE, IntStream.range(19, 26).toArray()));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        Crate crate = this.getLink(player);
        Runnable flush = () -> this.flush(player);

        viewer.addItem(NightItem.fromType(Material.NAME_TAG)
            .localized(LOCALE_NAME)
            .replacement(replacer -> replacer.replace(crate.replacePlaceholders()))
            .toMenuItem().setSlots(10).setHandler((viewer1, event) -> {
                CrateDialogs.CRATE_NAME.ifPresent(dialog -> dialog.show(player, crate, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.WRITABLE_BOOK)
            .localized(LOCALE_DESCRIPTION)
            .replacement(replacer -> replacer.replace(crate.replacePlaceholders()))
            .toMenuItem().setSlots(11).setHandler((viewer1, event) -> {
                CrateDialogs.CRATE_DESCRIPTION.ifPresent(dialog -> dialog.show(player, crate, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromItemStack(crate.getItemStack())
            .localized(LOCALE_ITEM)
            .replacement(replacer -> replacer
                .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_GENERIC_ITEM, crate.getItem().isValid()))
                .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(crate.isItemStackable()))
            )
            .toMenuItem().setSlots(12).setHandler((viewer1, event) -> {
                ItemStack cursor = event.getCursor();
                if (cursor == null || cursor.getType().isAir()) {
                    if (event.isLeftClick()) {
                        crate.setItemStackable(!crate.isItemStackable());
                        crate.markDirty();
                        this.runNextTick(flush);
                    }
                    return;
                }

                // Remove crate tags to avoid infinite recursion in ItemProvider.
                ItemStack clean = CrateUtils.removeCrateTags(new ItemStack(cursor));
                Players.addItem(player, cursor);
                event.getView().setCursor(null);
                CrateDialogs.CRATE_ITEM.ifPresent(dialog -> dialog.show(player, crate, clean, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.PAINTING)
            .localized(crate.isPreviewEnabled() ? LOCALE_PREVIEW_SET : LOCALE_PREVIEW_UNSET)
            .replacement(replacer -> replacer
                .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_CRATE_PREVIEW, crate.isPreviewValid()))
                .replace(GENERIC_VALUE, crate::getPreviewId)
            )
            .toMenuItem().setSlots(13).setHandler((viewer1, event) -> {
                CrateDialogs.CRATE_PREVIEW.ifPresent(dialog -> dialog.show(player, crate, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.GLOW_ITEM_FRAME)
            .localized(crate.isOpeningEnabled() ? LOCALE_OPENING_SET : LOCALE_OPENING_UNSET)
            .replacement(replacer -> replacer
                .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_CRATE_OPENING, crate.isOpeningValid()))
                .replace(GENERIC_VALUE, crate::getOpeningId)
            )
            .toMenuItem().setSlots(14).setHandler((viewer1, event) -> {
                CrateDialogs.CRATE_OPENING.ifPresent(dialog -> dialog.show(player, crate, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.BEACON)
            .localized(LOCALE_LINKED_BLOCKS)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_YES_NO.get(!crate.getBlockPositions().isEmpty())))
            .toMenuItem().setSlots(15).setHandler((viewer1, event) -> {
                if (event.getClick() == ClickType.DROP) {
                    crate.removeHologram();
                    crate.clearBlockPositions();
                    crate.markDirty();
                    this.runNextTick(flush);
                    return;
                }

                this.plugin.getCrateManager().giveLinkTool(player, crate);
                this.runNextTick(player::closeInventory);
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.SLIME_BLOCK)
            .localized(LOCALE_BLOCK_PUSHBACK)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(crate.isPushbackEnabled())))
            .toMenuItem().setSlots(16).setHandler((viewer1, event) -> {
                crate.setPushbackEnabled(!crate.isPushbackEnabled());
                crate.markDirty();
                this.runNextTick(flush);
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.TRIAL_KEY)
            .localized(LOCALE_COST_OPTIONS)
            .toMenuItem().setSlots(28).setHandler((viewer1, event) -> {
                this.runNextTick(() -> plugin.getEditorManager().openCosts(viewer.getPlayer(), crate));
            }).build()
        );

        viewer.addItem(NightItem.fromType(crate.isPermissionRequired() ? Material.REDSTONE : Material.GUNPOWDER)
            .localized(LOCALE_PERMISSION_REQUIREMENT)
            .replacement(replacer -> replacer
                .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(crate.isPermissionRequired()))
                .replace(GENERIC_VALUE, crate::getPermission)
            )
            .toMenuItem().setSlots(29).setHandler((viewer1, event) -> {
                crate.setPermissionRequired(!crate.isPermissionRequired());
                crate.markDirty();
                this.runNextTick(flush);
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.CLOCK)
            .localized(LOCALE_OPENING_COOLDOWN)
            .replacement(replacer -> replacer
                .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(crate.isOpeningCooldownEnabled()))
                .replace(GENERIC_VALUE, () -> {
                    if (crate.getOpeningCooldownTime() < 0L) return CoreLang.OTHER_ONE_TIMED.text();

                    return TimeFormats.toLiteral(crate.getOpeningCooldownTime() * 1000L);
                }))
            .toMenuItem().setSlots(30).setHandler((viewer1, event) -> {
                CrateDialogs.CRATE_COOLDOWN.ifPresent(dialog -> dialog.show(player, crate, flush));
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.BLAZE_POWDER)
            .localized(LOCALE_EFFECT)
            .replacement(replacer -> replacer
                .replace(GENERIC_TYPE, () -> StringUtil.capitalizeUnderscored(crate.getEffectType()))
                .replace(GENERIC_VALUE, () -> Lang.PARTICLE.getLocalized(crate.getEffectParticle().getParticle())))
            .toMenuItem().setSlots(31).setHandler((viewer1, event) -> {
                CrateDialogs.CRATE_EFFECT.ifPresent(dialog -> dialog.show(player, crate, flush));
            }).build()
        );

        if (this.plugin.hasHolograms()) {
            viewer.addItem(NightItem.fromType(Material.ARMOR_STAND)
                .localized(LOCALE_HOLOGRAM)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_CRATE_HOLOGRAM, crate.isHologramTemplateValid()))
                    .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(crate.isHologramEnabled()))
                    .replace(GENERIC_VALUE, crate::getHologramTemplateId)
                )
                .toMenuItem().setSlots(32).setHandler((viewer1, event) -> {
                    CrateDialogs.CRATE_HOLOGRAM.ifPresent(dialog -> dialog.show(player, crate, flush));
                }).build()
            );
        }

        viewer.addItem(NightItem.fromType(Material.VAULT)
            .localized(LOCALE_REWARDS)
            .replacement(replacer -> replacer
                .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_GENERIC_OVERVIEW, crate.getRewards().stream().noneMatch(Reward::hasProblems)))
                .replace(GENERIC_AMOUNT, () -> CoreLang.formatEntry(String.valueOf(crate.countRewards()), crate.countRewards() > 0))
            )
            .toMenuItem().setSlots(33).setHandler((viewer1, event) -> {
                this.runNextTick(() -> this.plugin.getEditorManager().openRewardList(viewer.getPlayer(), crate));
            }).build()
        );

        if (Config.isMilestonesEnabled()) {
            viewer.addItem(NightItem.fromType(Material.CAMPFIRE)
                .localized(LOCALE_MILESTONES)
                .replacement(replacer -> replacer
                    .replace(GENERIC_AMOUNT, () -> String.valueOf(crate.countMilestones()))
                )
                .toMenuItem().setSlots(34).setHandler((viewer1, event) -> {
                    // TODO crate.setMilestonesRepeatable(!crate.isMilestonesRepeatable());
                    this.runNextTick(() -> this.plugin.getEditorManager().openMilestones(viewer.getPlayer(), crate));
                }).build()
            );
        }

        viewer.addItem(NightItem.fromType(Material.BARRIER)
            .localized(LOCALE_DELETE)
            .toMenuItem().setSlots(53).setHandler((viewer1, event) -> {
                if (event.getClick() != ClickType.DROP) return;

                this.plugin.getCrateManager().delete(crate);
                this.runNextTick(() -> this.plugin.getEditorManager().openCrateList(player));
            }).build()
        );
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }
}
