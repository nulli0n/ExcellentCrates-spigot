package su.nightexpress.excellentcrates.api.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.Collection;

public interface Opening extends Tickable {

    @NotNull Player getPlayer();

    @NotNull CrateSource getSource();

    @NotNull Crate getCrate();

    @Nullable CrateKey getKey();

    @NotNull Collection<Spinner> getSpinners();

    void instaRoll();

    boolean isRefundable();

    void setRefundable(boolean refundable);

    boolean hasRewardAttempts();

    void setHasRewardAttempts(boolean hadRewardAttempts);

    boolean isSaveData();

    void setSaveData(boolean saveData);
}
