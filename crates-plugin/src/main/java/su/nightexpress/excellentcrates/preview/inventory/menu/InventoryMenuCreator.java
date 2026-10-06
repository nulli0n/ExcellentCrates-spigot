package su.nightexpress.excellentcrates.preview.inventory.menu;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;

@NullMarked
public class InventoryMenuCreator {

    private final CratesPlugin      plugin;
    private final CrateResolver     crateResolver;
    private final CratePlaceholders cratePlaceholders;
    private final RewardsAPI        rewardsAPI;

    public InventoryMenuCreator(CratesPlugin plugin,
                                CrateResolver crateResolver,
                                CratePlaceholders cratePlaceholders,
                                RewardsAPI rewardsAPI) {
        this.plugin = plugin;
        this.crateResolver = crateResolver;
        this.cratePlaceholders = cratePlaceholders;
        this.rewardsAPI = rewardsAPI;
    }

    public InventoryPreviewMenu createMenu(InventoryMenuSettings settings) {
        return new InventoryPreviewMenu(
            this.plugin, this.cratePlaceholders, this.crateResolver, this.rewardsAPI, settings
        );
    }
}
