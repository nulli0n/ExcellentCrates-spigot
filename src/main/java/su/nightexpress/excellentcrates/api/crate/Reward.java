package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.problem.ProblemReporter;

import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public interface Reward extends Writeable {

    @NotNull UnaryOperator<String> replacePlaceholders();

    void load(@NotNull FileConfig config, @NotNull String path);

    @NotNull RewardType getType();

    @NotNull ProblemReporter collectProblems();

    boolean hasProblems();

    boolean hasContent();

    int getAvailableRolls(@NotNull Player player);

    boolean isOnCooldown(@NotNull Player player);

    boolean isRollable();

    boolean hasBadPermissions(@NotNull Player player);

    boolean hasRequiredPermissions(@NotNull Player player);

    boolean fitRequirements(@NotNull Player player);

    boolean canWin(@NotNull Player player);

    void giveContent(@NotNull Player player);

    void give(@NotNull Player player);

    double getRollChance();

    @NotNull String getId();

    @NotNull Crate getCrate();

    @NotNull String getName();

    @NotNull List<String> getDescription();

    double getWeight();

    void setWeight(double weight);

    @NotNull Rarity getRarity();

    void setRarity(@NotNull Rarity rarity);

    boolean isBroadcast();

    void setBroadcast(boolean broadcast);

    @NotNull LimitValues getLimits();

    void setLimits(@NotNull LimitValues limitValues);

    @NotNull ItemStack getPreviewItem();

    @NotNull AdaptedItem getPreview();

    void setPreview(@NotNull AdaptedItem preview);

    @NotNull Set<String> getIgnoredPermissions();

    void setIgnoredPermissions(@NotNull Set<String> ignoredPermissions);

    @NotNull Set<String> getRequiredPermissions();

    void setRequiredPermissions(@NotNull Set<String> requiredPermissions);
}
