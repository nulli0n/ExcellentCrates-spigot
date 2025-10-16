package su.nightexpress.excellentcrates.api.cost;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public interface CostEntry {

    @NotNull NightItem getEditorIcon();

    @NotNull String format();

    boolean isValid();

    void openEditor(@NotNull Player player, @Nullable Runnable callback);

    int countPossibleOpenings(@NotNull Player player);

    boolean hasEnough(@NotNull Player player);

    void take(@NotNull Player player);

    void refund(@NotNull Player player);

    @NotNull CostType getType();
}
