package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
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
import su.nightexpress.excellentcrates.crate.reward.RewardFactory;
import su.nightexpress.excellentcrates.dialog.DialogRegistry;
import su.nightexpress.excellentcrates.crate.reward.RewardDialogs;
import su.nightexpress.excellentcrates.dialog.reward.RewardCreationDialog;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GREEN;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.SOFT_YELLOW;

public class RewardListMenu extends LinkedMenu<CratesPlugin, RewardListMenu.Data> implements Filled<Reward>, LangContainer {

    private static final IconLocale LOCALE_REWARD = LangEntry.iconBuilder("Editor.Button.Rewards.Reward")
        .rawName(REWARD_NAME)
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("ID", REWARD_ID)
        .appendCurrent("Weight", REWARD_WEIGHT + " → " + GREEN.wrap(REWARD_ROLL_CHANCE + "%"))
        .appendCurrent("Rarity", REWARD_RARITY_NAME + " → " + GREEN.wrap(REWARD_RARITY_ROLL_CHANCE + "%"))
        .br()
        .appendClick("Click to edit")
        .build();

    private static final IconLocale LOCALE_CREATION = LangEntry.iconBuilder("Editor.Button.Rewards.Creation")
        .accentColor(GREEN)
        .name("Reward Creation")
        .appendInfo("Drop item on " + GREEN.wrap("this") + " button", "to create a new reward of it.")
        .build();

    private static final IconLocale LOCALE_SORTING = LangEntry.iconBuilder("Editor.Button.Rewards.Sorting")
        .name("Sort Rewards")
        .appendInfo("Automatically sorts rewards in", "certain order.").br()
        .appendClick("Click to open")
        .build();

    private static final IconLocale LOCALE_MASS_MODE = LangEntry.iconBuilder("Editor.Button.Rewards.MassMode")
        .name("Mass Creation Mode")
        .appendCurrent("Status", GENERIC_STATE)
        .appendCurrent("Reward Type", GENERIC_TYPE).br()
        .appendInfo("Allows you to quickly add", "multiple rewards to the crate", "by clicking on items in", "your inventory.").br()
        .appendClick("Click to toggle mode")
        .appendClick("Click to toggle type")
        .build();

    private static final IconLocale LOCALE_ARRANGE_MODE = LangEntry.iconBuilder("Editor.Button.Rewards.ArrangeMode")
        .name("Arrange Mode")
        .appendCurrent("Status", GENERIC_STATE).br()
        .appendInfo("Allows you to arrange rewards by", "shifting them left or right.")
        .br()
        .appendInfo("Use the " + SOFT_YELLOW.wrap("Left-Click") + " to shift left,")
        .appendInfo("and the " + SOFT_YELLOW.wrap("Right-Click") + " to shift right.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public record Data(@NotNull Crate crate, @NotNull RewardType massModeType, boolean massMode, boolean arrangeMode) {}

    public boolean open(@NotNull Player player, @NotNull Crate crate) {
        return this.open(player, crate, RewardType.ITEM, false, false);
    }

    private boolean open(@NotNull Player player, @NotNull Crate crate, @NotNull RewardType massModeType, boolean massMode, boolean arrangeMode) {
        return this.open(player, new Data(crate, massModeType, massMode, arrangeMode));
    }

    private final DialogRegistry dialogs;

    public RewardListMenu(@NotNull CratesPlugin plugin, @NotNull DialogRegistry dialogs) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_LIST.text());
        this.dialogs = dialogs;
        this.plugin.injectLang(this);

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer).crate));
        }));

        this.addItem(MenuItem.buildNextPage(this, 41));
        this.addItem(MenuItem.buildPreviousPage(this, 39));

        this.addItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setSlots(IntStream.range(0, 36).toArray())
            .setPriority(-1)
        );

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setSlots(IntStream.range(36, 45).toArray())
            .setPriority(-1)
        );

        this.addItem(Material.ANVIL, LOCALE_CREATION, 42, (viewer, event, data) -> {
            Player player = viewer.getPlayer();
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) return;

            ItemStack copy = new ItemStack(cursor);
            event.getView().setCursor(null);
            Players.addItem(player, copy);

            this.dialogs.show(player, RewardDialogs.CREATION, new RewardCreationDialog.Data(data.crate, copy), () -> this.flush(player));
        });

        this.addItem(Material.COMPARATOR, LOCALE_SORTING, 38, (viewer, event, data) -> {
            Player player = viewer.getPlayer();
            this.dialogs.show(player, RewardDialogs.SORTING, data.crate, () -> this.flush(player));
        });
    }

    @Override
    @NotNull
    public MenuFiller<Reward> createFiller(@NotNull MenuViewer viewer) {
        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(this.getLink(viewer).crate.getRewards());
        autoFill.setItemCreator(reward -> {
            return NightItem.fromItemStack(reward.getPreviewItem())
                .hideAllComponents()
                .localized(LOCALE_REWARD)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_GENERIC_OVERVIEW, !reward.hasProblems()))
                    .replace(reward.replacePlaceholders())
                );
        });

        autoFill.setItemClick(reward -> (viewer1, event) -> {
            Data data = this.getLink(viewer1);
            Crate crate = data.crate;

            if (data.arrangeMode) {
                if (!event.isLeftClick() && !event.isRightClick()) return;

                // Reward position move.
                List<Reward> all = new ArrayList<>(crate.getRewards());
                int index = all.indexOf(reward);
                int allSize = all.size();

                if (event.isLeftClick()) {
                    if (index + 1 >= allSize) return;

                    all.remove(index);
                    all.add(index + 1, reward);
                }
                else if (event.isRightClick()) {
                    if (index == 0) return;

                    all.remove(index);
                    all.add(index - 1, reward);
                }
                crate.setRewards(all);
                crate.markDirty();
                this.runNextTick(() -> this.flush(viewer));
                return;
            }

            if (event.isLeftClick()) {
                this.runNextTick(() -> plugin.getEditorManager().openRewardOptions(viewer1.getPlayer(), reward));
            }
        });

        return autoFill.build();
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Data data = this.getLink(viewer);

        viewer.addItem(NightItem.fromType(Material.DROPPER)
            .localized(LOCALE_MASS_MODE)
            .replacement(replacer -> replacer
                .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(data.massMode))
                .replace(GENERIC_TYPE, () -> Lang.REWARD_TYPE.getLocalized(data.massModeType))
            )
            .toMenuItem().setSlots(36).setHandler((viewer1, event) -> {
                if (event.isLeftClick()) {
                    this.runNextTick(() -> this.open(viewer.getPlayer(), data.crate, data.massModeType, !data.massMode, data.arrangeMode));
                }
                else if (event.isRightClick()) {
                    this.runNextTick(() -> this.open(viewer.getPlayer(), data.crate, Lists.next(data.massModeType), data.massMode, data.arrangeMode));
                }
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.PISTON)
            .localized(LOCALE_ARRANGE_MODE)
            .replacement(replacer -> replacer
                .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(data.arrangeMode))
            )
            .toMenuItem().setSlots(44).setHandler((viewer1, event) -> {
                this.runNextTick(() -> this.open(viewer.getPlayer(), data.crate, data.massModeType, data.massMode, !data.arrangeMode));
            }).build()
        );

        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);

        if (!result.isInventory()) return;

        Data data = this.getLink(viewer);
        if (!data.massMode) {
            event.setCancelled(false);
            return;
        }

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().isAir()) return;

        AdaptedItem adapt = ItemHelper.adapt(itemStack, true);
        Reward reward = RewardFactory.wizardCreation(this.plugin, data.crate, itemStack, data.massModeType, adapt);
        data.crate.addReward(reward);
        data.crate.markDirty();
        this.runNextTick(() -> this.flush(viewer));
    }
}
