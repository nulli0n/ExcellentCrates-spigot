package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.AbstractMenuAuto;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;
import su.nightexpress.excellentcrates.key.KeyManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KeyEditorList extends AbstractMenuAuto<ExcellentCrates, ICrateKey> {

    private final String       objectName;
    private final List<String> objectLore;
    private final int[]        objectSlots;

    public KeyEditorList(@NotNull ExcellentCrates plugin) {
        super(plugin, CrateEditorHandler.KEY_LIST, "");

        this.objectName = StringUtil.color(cfg.getString("Object.Name", Placeholders.KEY_ID));
        this.objectLore = StringUtil.color(cfg.getStringList("Object.Lore"));
        this.objectSlots = cfg.getIntArray("Object.Slots");

        EditorInput<KeyManager, CrateEditorType> input = (player, keyManager, type, e) -> {
            if (type == CrateEditorType.KEY_CREATE) {
                if (!keyManager.create(EditorManager.fineId(e.getMessage()))) {
                    EditorManager.error(player, plugin.getMessage(Lang.EDITOR_KEY_ERROR_CREATE_EXIST).getLocalized());
                    return false;
                }
            }
            return true;
        };

        IMenuClick click = (player, type, e) -> {
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

        for (String sId : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(click);
            }
            this.addItem(menuItem);
        }

        for (String sId : cfg.getSection("Editor")) {
            IMenuItem menuItem = cfg.getMenuItem("Editor." + sId, CrateEditorType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(click);
            }
            this.addItem(menuItem);
        }
    }

    @Override
    public int[] getObjectSlots() {
        return objectSlots;
    }

    @Override
    @NotNull
    protected List<ICrateKey> getObjects(@NotNull Player player) {
        return new ArrayList<>(plugin.getKeyManager().getKeys().stream()
            .sorted(Comparator.comparing(ICrateKey::getId)).toList());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull ICrateKey key) {
        ItemStack item = new ItemStack(key.getItem());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setDisplayName(this.objectName);
        meta.setLore(this.objectLore);
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);

        ItemUtil.replace(item, key.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected IMenuClick getObjectClick(@NotNull Player player, @NotNull ICrateKey key) {
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
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
