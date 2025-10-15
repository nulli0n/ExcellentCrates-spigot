package su.nightexpress.excellentcrates.opening.world.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.world.impl.DummyOpening;
import su.nightexpress.nightcore.config.FileConfig;

public class DummyProvider implements OpeningProvider {

    private final CratesPlugin plugin;

    public DummyProvider(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getId() {
        return "dummy";
    }

    @Override
    public void load(@NotNull FileConfig config) {

    }

    @Override
    @NotNull
    public DummyOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        return new DummyOpening(this.plugin, player, source, cost);
    }
}
