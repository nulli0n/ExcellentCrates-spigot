package su.nightexpress.excellentcrates.opening.inventory.spinner.provider;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.excellentcrates.opening.inventory.spinner.impl.RewardSpinner;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.Lists;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RewardProvider implements SpinnerProvider, Writeable {

    private final Set<String> rarities;

    public RewardProvider(@NotNull Set<String> rarities) {
        this.rarities = new HashSet<>(rarities);
    }

    @NotNull
    public static RewardProvider everything() {
        return new RewardProvider(Lists.newSet(Placeholders.WILDCARD));
    }

    @NotNull
    public static RewardProvider read(@NotNull FileConfig config, @NotNull String path) {
        Set<String> rarities = ConfigValue.create(path + ".Rarities", Set.of(Placeholders.WILDCARD)).read(config);

        return new RewardProvider(rarities);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Rarities", this.rarities);
    }

    @Override
    @NotNull
    public RewardSpinner createSpinner(@NotNull CratesPlugin plugin, @NotNull SpinnerData data, @NotNull InventoryOpening opening) {
        Set<Rarity> rarities = new HashSet<>();
        if (this.rarities.contains(Placeholders.WILDCARD)) {
            rarities.addAll(plugin.getCrateManager().getRarities());
        }
        else {
            rarities.addAll(this.rarities.stream().map(rId -> plugin.getCrateManager().getRarity(rId)).filter(Objects::nonNull).toList());
        }

        return new RewardSpinner(data, opening, rarities);
    }

    @NotNull
    public Set<String> getRarities() {
        return this.rarities;
    }
}
