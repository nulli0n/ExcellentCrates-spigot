package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenuAuto;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.editor.CrateEditorMenu;
import su.nightexpress.excellentcrates.editor.CrateEditorType;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class EditorKeyList extends AbstractEditorMenuAuto<ExcellentCrates, KeyManager, CrateKey> {

    public EditorKeyList(@NotNull KeyManager keyManager) {
        super(keyManager.plugin(), keyManager, CrateEditorMenu.TITLE_KEY, 45);

        EditorInput<KeyManager, CrateEditorType> input = (player, keyManager2, type, e) -> {
            if (type == CrateEditorType.KEY_CREATE) {
                if (!keyManager2.create(EditorManager.fineId(e.getMessage()))) {
                    EditorManager.error(player, plugin.getMessage(Lang.EDITOR_KEY_ERROR_CREATE_EXIST).getLocalized());
                    return false;
                }
            }
            return true;
        };

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.RETURN) {
                    plugin.getEditor().open(player, 1);
                }
                else this.onItemClickDefault(player, type2);
            }
            else if (type instanceof CrateEditorType type2) {
                if (type2 == CrateEditorType.KEY_CREATE) {
                    EditorManager.startEdit(player, plugin.getKeyManager(), type2, input);
                    EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_ID).getLocalized());
                    player.closeInventory();
                }
            }
        };

        this.loadItems(click);
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.RETURN, 39);
        map.put(MenuItemType.PAGE_NEXT, 44);
        map.put(MenuItemType.PAGE_PREVIOUS, 36);
        map.put(CrateEditorType.KEY_CREATE, 41);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(0, 36).toArray();
    }

    @Override
    @NotNull
    protected List<CrateKey> getObjects(@NotNull Player player) {
        return new ArrayList<>(plugin.getKeyManager().getKeys().stream()
            .sorted(Comparator.comparing(CrateKey::getId)).toList());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull CrateKey key) {
        ItemStack item = new ItemStack(key.getItem());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        ItemStack object = CrateEditorType.KEY_OBJECT.getItem();
        meta.setDisplayName(ItemUtil.getItemName(object));
        meta.setLore(ItemUtil.getLore(object));
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);

        ItemUtil.replace(item, key.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected MenuClick getObjectClick(@NotNull Player player, @NotNull CrateKey key) {
        return (player1, type, e) -> {
            if (e.isRightClick() && e.isShiftClick()) {
                if (this.plugin.getKeyManager().delete(key)) {
                    this.open(player1, this.getPage(player1));
                }
                return;
            }
            key.getEditor().open(player1, 1);
        };
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
