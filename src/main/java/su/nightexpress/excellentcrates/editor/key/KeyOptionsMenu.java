package su.nightexpress.excellentcrates.editor.key;

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
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.key.CrateKey;
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
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.RED;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.SOFT_YELLOW;

public class KeyOptionsMenu extends LinkedMenu<CratesPlugin, CrateKey> implements LangContainer {

    private static final IconLocale LOCALE_DELETE = LangEntry.iconBuilder("Editor.Button.Key.Delete")
        .accentColor(RED)
        .name("Delete Key")
        .appendInfo("Permanently deletes the key.").br()
        .appendClick("Press [" + TagWrappers.KEY.apply("key.drop") + "] to delete")
        .build();

    private static final IconLocale LOCALE_NAME = LangEntry.iconBuilder("Editor.Button.Key.Name")
        .name("Display Name")
        .appendCurrent("Current", KEY_NAME).br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_ITEM = LangEntry.iconBuilder("Editor.Button.Key.Item").name("Key Item")
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("Stackable", GENERIC_STATE).br()
        .appendInfo("Drop an item on " + SOFT_YELLOW.wrap("this") + " button", "to replace the key's item.").br()
        .appendClick("Click to toggle stacking")
        .build();

    private static final IconLocale LOCALE_VIRTUAL = LangEntry.iconBuilder("Editor.Button.Key.Virtual")
        .name("Virtual")
        .appendCurrent("State", GENERIC_STATE).br()
        .appendInfo("Controls whether the key is virtual.").br()
        .appendClick("Click to toggle")
        .build();

    public KeyOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_KEY_LIST.text());
        this.plugin.injectLang(this);

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openKeyList(viewer.getPlayer()));
        }));

        this.addItem(MenuItem.background(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray()));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        CrateKey key = this.getLink(player);
        Runnable flush = () -> this.flush(player);

        viewer.addItem(NightItem.fromType(Material.NAME_TAG).localized(LOCALE_NAME)
            .replacement(replacer -> replacer.replace(key.replacePlaceholders()))
            .toMenuItem().setSlots(11).setHandler((viewer1, event) -> {
                CrateDialogs.KEY_NAME.ifPresent(dialog -> dialog.show(player, key, flush));
            }).build()
        );

        if (!key.isVirtual()) {
            viewer.addItem(NightItem.fromItemStack(key.getItemStack())
                .localized(LOCALE_ITEM)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_GENERIC_ITEM, key.getItem().isValid()))
                    .replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(key.isItemStackable()))
                )
                .toMenuItem().setSlots(13).setHandler((viewer1, event) -> {
                    ItemStack cursor = event.getCursor();
                    if (cursor == null || cursor.getType().isAir()) {
                        if (event.isLeftClick()) {
                            key.setItemStackable(!key.isItemStackable());
                            key.markDirty();
                            this.runNextTick(flush);
                        }
                        return;
                    }

                    // Remove crate tags to avoid infinite recursion in ItemProvider.
                    ItemStack clean = CrateUtils.removeCrateTags(new ItemStack(cursor));
                    Players.addItem(player, cursor);
                    event.getView().setCursor(null);

                    CrateDialogs.KEY_ITEM.ifPresent(dialog -> dialog.show(player, key, clean, flush));
                }).build()
            );
        }

        viewer.addItem(NightItem.fromType(Material.ENDER_PEARL).localized(LOCALE_VIRTUAL)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_YES_NO.get(key.isVirtual())))
            .toMenuItem().setSlots(15).setHandler((viewer1, event) -> {
                key.setVirtual(!key.isVirtual());
                key.markDirty();
                this.runNextTick(flush);
            }).build()
        );

        viewer.addItem(NightItem.fromType(Material.BARRIER).localized(LOCALE_DELETE)
            .toMenuItem().setSlots(44).setHandler((viewer1, event) -> {
                if (event.getClick() != ClickType.DROP) return;

                this.plugin.getKeyManager().delete(key);
                this.runNextTick(() -> this.plugin.getEditorManager().openKeyList(player));
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
