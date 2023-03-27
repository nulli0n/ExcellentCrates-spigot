package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.editor.EditorLocales;
import su.nightexpress.excellentcrates.key.CrateKey;

public class KeyMainEditor extends EditorMenu<ExcellentCrates, CrateKey> {

    public KeyMainEditor(@NotNull CrateKey crateKey) {
        super(crateKey.plugin(), crateKey, Config.EDITOR_TITLE_KEY.get(), 45);

        this.addReturn(40).setClick((viewer, event) -> {
            this.plugin.runTask(task -> plugin.getEditor().getKeysEditor().open(viewer.getPlayer(), 1));
        });

        this.addItem(Material.NAME_TAG, EditorLocales.KEY_NAME, 20).setClick((viewer, event) -> {
            this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_REWARD_ENTER_DISPLAY_NAME), chat -> {
                crateKey.setName(chat.getMessage());
                return true;
            });
        });

        this.addItem(Material.TRIPWIRE_HOOK, EditorLocales.KEY_ITEM, 22).setClick((viewer, event) -> {
            if (event.isRightClick()) {
                PlayerUtil.addItem(viewer.getPlayer(), crateKey.getItem());
                return;
            }

            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) return;

            crateKey.setItem(cursor);
            event.getView().setCursor(null);
            this.save(viewer);
        }).getOptions().setDisplayModifier(((viewer, item) -> {
            item.setType(crateKey.getItem().getType());
            item.setItemMeta(crateKey.getItem().getItemMeta());
            ItemUtil.mapMeta(item, meta -> {
                meta.setDisplayName(EditorLocales.KEY_ITEM.getLocalizedName());
                meta.setLore(EditorLocales.KEY_ITEM.getLore());
                meta.addItemFlags(ItemFlag.values());
            });
        }));

        this.addItem(Material.ENDER_PEARL, EditorLocales.KEY_VIRTUAL, 24).setClick((viewer, event) -> {
            crateKey.setVirtual(!crateKey.isVirtual());
            this.save(viewer);
        });

        this.getItems().forEach(menuItem -> {
            if (menuItem.getOptions().getDisplayModifier() == null) {
                menuItem.getOptions().setDisplayModifier(((viewer, item) -> ItemUtil.replace(item, crateKey.replacePlaceholders())));
            }
        });
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.save();
        this.plugin.runTask(task -> this.open(viewer.getPlayer(), viewer.getPage()));
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @Nullable ItemStack item, @NotNull SlotType slotType, int slot, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, item, slotType, slot, event);
        if (slotType == SlotType.PLAYER || slotType == SlotType.PLAYER_EMPTY) {
            event.setCancelled(false);
        }
    }
}
