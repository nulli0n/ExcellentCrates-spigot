package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenuAuto;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.Crate;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.editor.CrateEditorMenu;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class EditorCrateList extends AbstractEditorMenuAuto<ExcellentCrates, CrateManager, Crate> {

    public EditorCrateList(@NotNull CrateManager crateManager) {
        super(crateManager.plugin(), crateManager, CrateEditorMenu.TITLE_CRATE, 45);

        EditorInput<CrateManager, CrateEditorType> input = (player, crateManager2, type, e) -> {
            if (type == CrateEditorType.CRATE_CREATE) {
                if (!crateManager2.create(EditorManager.fineId(e.getMessage()))) {
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

        this.loadItems(click);
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.RETURN, 39);
        map.put(MenuItemType.PAGE_NEXT, 44);
        map.put(MenuItemType.PAGE_PREVIOUS, 36);
        map.put(CrateEditorType.CRATE_CREATE, 41);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(0, 36).toArray();
    }

    @Override
    @NotNull
    protected List<Crate> getObjects(@NotNull Player player) {
        return new ArrayList<>(plugin.getCrateManager().getCrates().stream()
            .sorted(Comparator.comparing(Crate::getId)).toList());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull Crate crate) {
        ItemStack item = new ItemStack(crate.getItem());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        ItemStack object = CrateEditorType.CRATE_OBJECT.getItem();
        meta.setDisplayName(ItemUtil.getItemName(object));
        meta.setLore(ItemUtil.getLore(object));
        item.setItemMeta(meta);

        ItemUtil.replace(item, crate.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected IMenuClick getObjectClick(@NotNull Player player, @NotNull Crate crate) {
        return (player1, type, e) -> {
            if (e.isShiftClick() && e.isRightClick()) {
                this.parent.delete(crate);
                this.open(player1, this.getPage(player1));
                return;
            }
            crate.getEditor().open(player1, 1);
        };
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
