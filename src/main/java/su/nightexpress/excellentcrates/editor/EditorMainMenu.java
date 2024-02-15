package su.nightexpress.excellentcrates.editor;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.editor.CrateListEditor;
import su.nightexpress.excellentcrates.key.editor.KeyListEditor;

public class EditorMainMenu extends EditorMenu<ExcellentCratesPlugin, ExcellentCratesPlugin> {

    private static final String TEXTURE_CRATE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2EzMTMwZDFjMDQ0MzY1ODk1YzU4MjM5ZjdkMDYxNTUwOWZmMzhmM2Y3YTdkZmE3ZjI5YmI5NDJkYjFkZGUzZSJ9fX0=";

    private CrateListEditor crateListEditor;
    private KeyListEditor   keyListEditor;

    public EditorMainMenu(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, plugin, Config.EDITOR_TITLE_CRATE.get(), 27);

        this.addItem(ItemUtil.createCustomHead(TEXTURE_CRATE), EditorLocales.CRATES_EDITOR, 11)
            .setClick((viewer, event) -> {
                this.plugin.runTask(task -> this.getCratesEditor().open(viewer.getPlayer(), 1));
            });
        this.addItem(Material.BLAZE_ROD, EditorLocales.KEYS_EDITOR, 15)
            .setClick((viewer, event) -> {
                this.plugin.runTask(task -> this.getKeysEditor().open(viewer.getPlayer(), 1));
            });
    }

    @Override
    public void clear() {
        if (this.crateListEditor != null) {
            this.crateListEditor.clear();
            this.crateListEditor = null;
        }
        if (this.keyListEditor != null) {
            this.keyListEditor.clear();
            this.keyListEditor = null;
        }
        super.clear();
    }

    @NotNull
    public CrateListEditor getCratesEditor() {
        if (this.crateListEditor == null) {
            this.crateListEditor = new CrateListEditor(this.plugin.getCrateManager());
        }
        return this.crateListEditor;
    }

    @NotNull
    public KeyListEditor getKeysEditor() {
        if (this.keyListEditor == null) {
            this.keyListEditor = new KeyListEditor(this.plugin.getKeyManager());
        }
        return this.keyListEditor;
    }
}
