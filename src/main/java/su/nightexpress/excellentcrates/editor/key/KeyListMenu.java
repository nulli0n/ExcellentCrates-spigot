package su.nightexpress.excellentcrates.editor.key;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class KeyListMenu extends LinkedMenu<CratesPlugin, KeyManager> implements Filled<CrateKey> {

    public KeyListMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_KEY_LIST.getString());

        this.addItem(MenuItem.buildReturn(this, 39, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openEditor(viewer.getPlayer()));
        }));
        this.addItem(MenuItem.buildNextPage(this, 44));
        this.addItem(MenuItem.buildPreviousPage(this, 36));

        this.addItem(Material.ANVIL, EditorLang.KEY_CREATE, 41, (viewer, event, manager) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_CRATE_ID, input -> {
                if (!manager.create(input.getTextRaw())) {
                    Lang.KEY_CREATE_ERROR_DUPLICATED.getMessage().send(viewer.getPlayer());
                    return false;
                }
                return true;
            }));
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
                .localized(EditorLang.KEY_OBJECT)
                .setHideComponents(true)
                .replacement(replacer -> replacer.replace(key.replacePlaceholders()));
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
