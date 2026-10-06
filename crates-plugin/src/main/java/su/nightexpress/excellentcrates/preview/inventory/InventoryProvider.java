package su.nightexpress.excellentcrates.preview.inventory;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewProvider;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryMenuCreator;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryMenuSettings;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryPreviewMenu;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class InventoryProvider implements PreviewProvider {

    private static final Identifier ID = new Identifier("inventory");

    private final InventoryMenuCreator menuCreator;

    public InventoryProvider(InventoryMenuCreator menuCreator) {
        this.menuCreator = menuCreator;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Preview parseConfig(AdaptedKey key, FileConfig config) {
        InventoryMenuSettings settings = config.getOrSet("Settings",
            InventoryMenuSettings.class,
            InventoryMenuSettings.defaults()
        );

        InventoryPreviewMenu menu = this.menuCreator.createMenu(settings);
        menu.load();

        return new InventoryPreview(key, settings.getName(), menu);
    }

    @Override
    public void writeDefaultConfig(FileConfig config) {
        config.set("Settings", InventoryMenuSettings.defaults());
    }
}
