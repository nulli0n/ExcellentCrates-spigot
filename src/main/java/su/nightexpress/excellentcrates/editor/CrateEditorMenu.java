package su.nightexpress.excellentcrates.editor;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenu;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.crate.editor.EditorCrateList;
import su.nightexpress.excellentcrates.key.editor.EditorKeyList;

import java.util.Map;

public class CrateEditorMenu extends AbstractEditorMenu<ExcellentCrates, ExcellentCrates> {

    public static final String TITLE_CRATE = "Crate Editor";
    public static final String TITLE_KEY = "Key Editor";

    private EditorCrateList editorCrateList;
    private EditorKeyList   editorKeyList;

    public CrateEditorMenu(@NotNull ExcellentCrates plugin) {
        super(plugin, plugin, "ExcellentCrates Editor", 27);

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                this.onItemClickDefault(player, type2);
            }
            else if (type instanceof CrateEditorType type2) {
                if (type2 == CrateEditorType.EDITOR_CRATES) {
                    this.getCratesEditor().open(player, 1);
                    return;
                }
                if (type2 == CrateEditorType.EDITOR_KEYS) {
                    this.getKeysEditor().open(player, 1);
                }
            }
        };

        this.loadItems(click);
    }

    @Override
    public void clear() {
        if (this.editorCrateList != null) {
            this.editorCrateList.clear();
            this.editorCrateList = null;
        }
        if (this.editorKeyList != null) {
            this.editorKeyList.clear();
            this.editorKeyList = null;
        }
        super.clear();
    }

    @NotNull
    public EditorCrateList getCratesEditor() {
        if (this.editorCrateList == null) {
            this.editorCrateList = new EditorCrateList(this.plugin.getCrateManager());
        }
        return this.editorCrateList;
    }

    @NotNull
    public EditorKeyList getKeysEditor() {
        if (this.editorKeyList == null) {
            this.editorKeyList = new EditorKeyList(this.plugin.getKeyManager());
        }
        return this.editorKeyList;
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.CLOSE, 31);
        map.put(CrateEditorType.EDITOR_CRATES, 11);
        map.put(CrateEditorType.EDITOR_KEYS, 15);
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
