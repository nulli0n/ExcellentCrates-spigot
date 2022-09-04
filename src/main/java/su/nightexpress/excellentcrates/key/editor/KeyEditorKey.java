package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

public class KeyEditorKey extends AbstractMenu<ExcellentCrates> {

    private final ICrateKey crateKey;

    public KeyEditorKey(@NotNull ExcellentCrates plugin, @NotNull ICrateKey crateKey) {
        super(plugin, CrateEditorHandler.KEY_MAIN, "");
        this.crateKey = crateKey;

        EditorInput<ICrateKey, CrateEditorType> input = (player, key, type, e) -> {
            if (type == CrateEditorType.KEY_CHANGE_NAME) {
                key.setName(e.getMessage());
            }
            key.save();
            return true;
        };

        IMenuClick clickHandler = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.RETURN) {
                    this.plugin.getEditor().getKeysEditor().open(player, 1);
                }
            }
            else if (type instanceof CrateEditorType type2) {
                switch (type2) {
                    case KEY_CHANGE_ITEM -> {
                        if (e.isRightClick()) {
                            PlayerUtil.addItem(player, crateKey.getItem());
                            return;
                        }

                        ItemStack cursor = e.getCursor();
                        if (cursor == null || cursor.getType().isAir()) return;
                        crateKey.setItem(cursor);
                        e.getView().setCursor(null);
                    }
                    case KEY_CHANGE_VIRTUAL -> crateKey.setVirtual(!crateKey.isVirtual());
                    case KEY_CHANGE_NAME -> {
                        EditorManager.startEdit(player, crateKey, type2, input);
                        EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_DISPLAY_NAME).getLocalized());
                        player.closeInventory();
                        return;
                    }
                    default -> {
                        return;
                    }
                }
                crateKey.save();
                this.open(player, 1);
            }
        };

        for (String sId : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(clickHandler);
            }
            this.addItem(menuItem);
        }

        for (String sId : cfg.getSection("Editor")) {
            IMenuItem menuItem = cfg.getMenuItem("Editor." + sId, CrateEditorType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(clickHandler);
            }
            this.addItem(menuItem);
        }
    }

    @Override
    public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        if (menuItem.getType() == CrateEditorType.KEY_CHANGE_ITEM) {
            item.setType(this.crateKey.getItem().getType());
        }

        ItemUtil.replace(item, this.crateKey.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return slotType != SlotType.EMPTY_PLAYER && slotType != SlotType.PLAYER;
    }
}
