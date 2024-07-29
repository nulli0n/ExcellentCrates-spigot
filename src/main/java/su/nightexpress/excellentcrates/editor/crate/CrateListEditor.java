package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.editor.type.CreationResult;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;

import java.util.Comparator;
import java.util.stream.IntStream;

public class CrateListEditor extends EditorMenu<CratesPlugin, CrateManager> implements AutoFilled<Crate> {

    public CrateListEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_CRATE_LIST.getString(), MenuSize.CHEST_45);

        this.addReturn(39, (viewer, event, manager) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openEditor(viewer.getPlayer()));
        });
        this.addNextPage(44);
        this.addPreviousPage(36);

        this.addCreation(EditorLang.CRATE_CREATE, 41, (viewer, event, manager) -> {
            this.handleInput(viewer, Lang.EDITOR_ENTER_CRATE_ID, (dialog, input) -> {
                CreationResult result = manager.create(input.getTextRaw());
                if (result == CreationResult.ERROR_NAME) {
                    dialog.error(Lang.EDITOR_ERROR_BAD_NAME.getMessage());
                    return false;
                }
                if (result == CreationResult.ERROR_DUPLICATE) {
                    dialog.error(Lang.EDITOR_ERROR_DUPLICATED_CRATE.getMessage());
                    return false;
                }
                return true;
            });
        });
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Crate> autoFill) {
        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(this.getLink(viewer).getCrates().stream().sorted(Comparator.comparing(Crate::getId)).toList());
        autoFill.setItemCreator(crate -> {
            ItemStack item = crate.getRawItem();
            ItemReplacer.create(item).trimmed().hideFlags()
                .readLocale(EditorLang.CRATE_OBJECT)
                .replace(crate.getAllPlaceholders())
                .writeMeta();
            return item;
        });
        autoFill.setClickAction(crate -> (viewer1, event) -> {
            if (event.isShiftClick() && event.isRightClick()) {
                this.plugin.getCrateManager().delete(crate);
                this.runNextTick(() -> this.flush(viewer1));
                return;
            }
            this.runNextTick(() -> this.plugin.getEditorManager().openCrate(viewer1.getPlayer(), crate));
        });
    }
}
