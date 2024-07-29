package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemUtil;

public class CratesEditorMenu extends EditorMenu<CratesPlugin, CratesPlugin> {

    private static final String TEXTURE_CRATE = "322d4be1abcf3832c916191d24f9607bf194eff8dfbf3b9520bd97240e7c8";
    private static final String TEXTURE_KEYS = "311790e8005c7f972c469b7b875eab218e0713afe5f2edfd468659910ed622e3";

    public CratesEditorMenu(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_MAIN.getString(), MenuSize.CHEST_27);

        this.addItem(ItemUtil.getSkinHead(TEXTURE_CRATE), EditorLang.CRATES_EDITOR, 11, (viewer, event, plugin1) -> {
            this.runNextTick(() -> plugin.getEditorManager().openCrateList(viewer.getPlayer()));
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_KEYS), EditorLang.KEYS_EDITOR, 15, (viewer, event, plugin1) -> {
            this.runNextTick(() -> plugin.getEditorManager().openKeyList(viewer.getPlayer()));
        });
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
