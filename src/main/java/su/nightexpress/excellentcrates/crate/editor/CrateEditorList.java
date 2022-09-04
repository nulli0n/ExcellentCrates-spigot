package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
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
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CrateEditorList extends AbstractMenuAuto<ExcellentCrates, ICrate> {

    private final int[]        objectSlots;
    private final String       objectName;
    private final List<String> objectLore;

    public CrateEditorList(@NotNull ExcellentCrates plugin) {
        super(plugin, CrateEditorHandler.CRATE_LIST, "");
        JYML cfg = CrateEditorHandler.CRATE_LIST;

        this.objectSlots = cfg.getIntArray("Object.Slots");
        this.objectName = StringUtil.color(cfg.getString("Object.Name", Placeholders.CRATE_ID));
        this.objectLore = StringUtil.color(cfg.getStringList("Object.Lore"));

        EditorInput<CrateManager, CrateEditorType> input = (player, crateManager, type, e) -> {
            if (type == CrateEditorType.CRATE_CREATE) {
                if (!plugin.getCrateManager().create(EditorManager.fineId(e.getMessage()))) {
                    EditorManager.error(player, plugin.getMessage(Lang.EDITOR_CRATE_ERROR_CREATE_EXISTS).getLocalized());
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
                if (type2 == CrateEditorType.CRATE_CREATE) {
                    EditorManager.startEdit(player, plugin.getCrateManager(), type2, input);
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
        return this.objectSlots;
    }

    @Override
    @NotNull
    protected List<ICrate> getObjects(@NotNull Player player) {
        return new ArrayList<>(plugin.getCrateManager().getCrates().stream()
            .sorted(Comparator.comparing(ICrate::getId)).toList());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull ICrate crate) {
        ItemStack item = new ItemStack(crate.getItem());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setDisplayName(this.objectName);
        meta.setLore(this.objectLore);
        item.setItemMeta(meta);

        ItemUtil.replace(item, crate.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected IMenuClick getObjectClick(@NotNull Player player, @NotNull ICrate crate) {
        return (player1, type, e) -> {
            crate.getEditor().open(player1, 1);
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
