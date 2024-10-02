package su.nightexpress.excellentcrates.api.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;

public interface OpeningProvider {

    @NotNull Opening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key);
}
