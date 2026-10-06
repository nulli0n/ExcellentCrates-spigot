package su.nightexpress.excellentcrates.preview.inventory;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;
import su.nightexpress.excellentcrates.preview.inventory.codec.InventoryButtonCodec;
import su.nightexpress.excellentcrates.preview.inventory.codec.InventoryMenuSettingsCodec;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryButton;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryMenuCreator;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryMenuSettings;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class InventoryPreviewBootstrapContext {

    private final InventoryProvider provider;

    public InventoryPreviewBootstrapContext(CratesPlugin plugin,
                                            CrateRegistry crateRegistry,
                                            CratePlaceholders cratePlaceholders,
                                            RewardsAPI rewardsAPI) {
        ConfigCodecs.register(InventoryButton.class, InventoryButtonCodec.INSTANCE);
        ConfigCodecs.register(InventoryMenuSettings.class, InventoryMenuSettingsCodec.INSTANCE);

        InventoryMenuCreator menuCreator = new InventoryMenuCreator(
            plugin, crateRegistry, cratePlaceholders, rewardsAPI
        );

        this.provider = new InventoryProvider(menuCreator);
    }

    public InventoryProvider getProvider() {
        return this.provider;
    }
}
