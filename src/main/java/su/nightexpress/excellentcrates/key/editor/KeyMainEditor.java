package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Players;

public class KeyMainEditor extends EditorMenu<CratesPlugin, CrateKey> {

    public KeyMainEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_KEYS.getString(), 45);

        this.addReturn(40, (viewer, event, keyManager) -> {
            this.runNextTick(() -> plugin.getKeyManager().openKeysEditor(viewer.getPlayer()));
        });

        this.addItem(Material.NAME_TAG, EditorLang.KEY_DISPLAY_NAME, 20, (viewer, event, crateKey) -> {
            this.handleInput(viewer, Lang.EDITOR_ENTER_DISPLAY_NAME, (dialog, input) -> {
                crateKey.setName(input.getText());
                crateKey.save();
                return true;
            });
        });

        this.addItem(Material.TRIPWIRE_HOOK, EditorLang.KEY_ITEM, 22, (viewer, event, crateKey) -> {
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) {
                if (event.isLeftClick()) {
                    Players.addItem(viewer.getPlayer(), crateKey.getItem());
                }
                else if (event.isRightClick()) {
                    Players.addItem(viewer.getPlayer(), crateKey.getRawItem());
                }
                return;
            }

            crateKey.setItem(cursor);
            event.getView().setCursor(null);
            this.save(viewer);
        }).getOptions().setDisplayModifier(((viewer, item) -> {
            CrateKey crateKey = this.getObject(viewer);
            item.setType(crateKey.getRawItem().getType());
            item.setItemMeta(crateKey.getRawItem().getItemMeta());
            ItemUtil.editMeta(item, meta -> {
                meta.setDisplayName(EditorLang.KEY_ITEM.getLocalizedName());
                meta.setLore(EditorLang.KEY_ITEM.getLocalizedLore());
                meta.addItemFlags(ItemFlag.values());
            });
        })).setVisibilityPolicy(viewer -> !this.getObject(viewer).isVirtual());

        this.addItem(Material.ENDER_PEARL, EditorLang.KEY_VIRTUAL, 24, (viewer, event, crateKey) -> {
            crateKey.setVirtual(!crateKey.isVirtual());
            this.save(viewer);
        });

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.replace(item, this.getObject(viewer).replacePlaceholders());
        }));
    }

    private void save(@NotNull MenuViewer viewer) {
        this.getObject(viewer).save();
        this.runNextTick(() -> this.flush(viewer));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

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
