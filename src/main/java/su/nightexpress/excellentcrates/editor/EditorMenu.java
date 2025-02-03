package su.nightexpress.excellentcrates.editor;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.type.NormalMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@SuppressWarnings("UnstableApiUsage")
public class EditorMenu extends NormalMenu<CratesPlugin> {

    private static final String TEXTURE_CRATE = "322d4be1abcf3832c916191d24f9607bf194eff8dfbf3b9520bd97240e7c8";
    private static final String TEXTURE_KEYS = "311790e8005c7f972c469b7b875eab218e0713afe5f2edfd468659910ed622e3";

    public EditorMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X3, Lang.EDITOR_TITLE_MAIN.getString());

        this.addItem(NightItem.asCustomHead(TEXTURE_CRATE).localized(EditorLang.CRATES_EDITOR).toMenuItem().setSlots(11).setHandler((viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openCrateList(viewer.getPlayer()));
        }));

        this.addItem(NightItem.asCustomHead(TEXTURE_KEYS).localized(EditorLang.KEYS_EDITOR).toMenuItem().setSlots(15).setHandler((viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openKeyList(viewer.getPlayer()));
        }));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
