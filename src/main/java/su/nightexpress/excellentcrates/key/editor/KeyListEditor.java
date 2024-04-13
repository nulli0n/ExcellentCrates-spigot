package su.nightexpress.excellentcrates.key.editor;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.Comparator;
import java.util.stream.IntStream;

public class KeyListEditor extends EditorMenu<CratesPlugin, KeyManager> implements AutoFilled<CrateKey> {

    public KeyListEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_KEYS.getString(), 45);

        this.addReturn(39, (viewer, event, keyManager) -> {
            this.runNextTick(() -> this.plugin.getCrateManager().openEditor(viewer.getPlayer()));
        });
        this.addNextPage(44);
        this.addPreviousPage(36);

        this.addCreation(EditorLang.KEY_CREATE, 41, (viewer, event, keyManager) -> {
            this.handleInput(viewer, Lang.EDITOR_ENTER_CRATE_ID, (dialog, input) -> {
                if (!keyManager.create(StringUtil.lowerCaseUnderscore(input.getTextRaw()))) {
                    dialog.error(Lang.ERROR_DUPLICATED_KEY.getMessage());
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
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<CrateKey> autoFill) {
        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(plugin.getKeyManager().getKeys().stream().sorted(Comparator.comparing(CrateKey::getId)).toList());
        autoFill.setItemCreator(key -> {
            ItemStack item = new ItemStack(key.getItem());
            ItemReplacer.create(item).readLocale(EditorLang.KEY_OBJECT).hideFlags().trimmed()
                .replace(key.replacePlaceholders())
                .writeMeta();
            return item;
        });
        autoFill.setClickAction(key -> (viewer1, event) -> {
            if (event.isRightClick() && event.isShiftClick()) {
                if (this.plugin.getKeyManager().delete(key)) {
                    this.flush(viewer1);
                }
                return;
            }
            this.runNextTick(() -> plugin.getKeyManager().openKeyEditor(viewer1.getPlayer(), key));
        });
    }
}
