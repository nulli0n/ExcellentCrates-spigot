package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenu;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.editor.CrateEditorMenu;
import su.nightexpress.excellentcrates.editor.CrateEditorType;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.Map;

public class EditorKeyMain extends AbstractEditorMenu<ExcellentCrates, CrateKey> {

    public EditorKeyMain(@NotNull CrateKey crateKey) {
        super(crateKey.plugin(), crateKey, CrateEditorMenu.TITLE_KEY, 45);

        EditorInput<CrateKey, CrateEditorType> input = (player, key, type, e) -> {
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

        this.loadItems(clickHandler);
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.RETURN, 40);
        map.put(CrateEditorType.KEY_CHANGE_NAME, 20);
        map.put(CrateEditorType.KEY_CHANGE_ITEM, 22);
        map.put(CrateEditorType.KEY_CHANGE_VIRTUAL, 24);
    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        if (menuItem.getType() == CrateEditorType.KEY_CHANGE_ITEM) {
            item.setType(this.object.getItem().getType());
        }

        ItemUtil.replace(item, this.object.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return slotType != SlotType.EMPTY_PLAYER && slotType != SlotType.PLAYER;
    }
}
