package su.nightexpress.excellentcrates.editor.key;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GREEN;

public class KeyListMenu extends LinkedMenu<CratesPlugin, KeyManager> implements Filled<CrateKey>, LangContainer {

    private static final IconLocale LOCALE_CREATION = LangEntry.iconBuilder("Editor.Button.Keys.Create")
        .accentColor(GREEN)
        .name("New Key")
        .appendInfo("Use this button to create", "brand new keys!").br()
        .appendClick("Click to create")
        .build();

    private static final IconLocale LOCALE_KEY = LangEntry.iconBuilder("Editor.Button.Keys.Key")
        .rawName(KEY_NAME)
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("ID", KEY_ID)
        .appendCurrent("Virtual", GENERIC_STATE)
        .br()
        .appendClick("Click to edit")
        .build();

    public KeyListMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_KEY_LIST.text());
        this.plugin.injectLang(this);

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openEditor(viewer.getPlayer()));
        }));
        this.addItem(MenuItem.buildNextPage(this, 44));
        this.addItem(MenuItem.buildPreviousPage(this, 36));
        this.addItem(MenuItem.background(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray()));
        this.addItem(MenuItem.background(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray()));

        this.addItem(Material.ANVIL, LOCALE_CREATION, 42, (viewer, event, manager) -> {
            Player player = viewer.getPlayer();
            CrateDialogs.KEY_CREATION.ifPresent(dialog -> dialog.show(player, manager, () -> this.flush(player)));
        });
    }

    @Override
    @NotNull
    public MenuFiller<CrateKey> createFiller(@NotNull MenuViewer viewer) {
        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(plugin.getKeyManager().getKeys().stream().sorted(Comparator.comparing(CrateKey::getId)).toList());
        autoFill.setItemCreator(key -> {
            return NightItem.fromItemStack(key.getRawItem())
                .hideAllComponents()
                .localized(LOCALE_KEY)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_GENERIC_OVERVIEW, !key.hasProblems()))
                    .replace(GENERIC_STATE, () -> CoreLang.STATE_YES_NO.get(key.isVirtual()))
                    .replace(key.replacePlaceholders())
                );
        });
        autoFill.setItemClick(key -> (viewer1, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openKeyOptions(viewer1.getPlayer(), key));
        });

        return autoFill.build();
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
