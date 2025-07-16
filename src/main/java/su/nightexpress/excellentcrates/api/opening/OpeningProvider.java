package su.nightexpress.excellentcrates.api.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.config.FileConfig;

public interface OpeningProvider {

    void load(@NotNull FileConfig config);

    @NotNull String getId();

    @NotNull Opening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key);
}
