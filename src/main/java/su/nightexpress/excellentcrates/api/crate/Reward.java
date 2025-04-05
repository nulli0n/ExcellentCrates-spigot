package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;

import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public interface Reward extends Writeable {

    void save();

    @NotNull UnaryOperator<String> replacePlaceholders();

    @NotNull UnaryOperator<String> replaceAllPlaceholders();

    void load(@NotNull FileConfig config, @NotNull String path);

    @NotNull RewardType getType();

    boolean hasProblems();

    boolean hasContent();

    int getAvailableRolls(@NotNull Player player);

    boolean hasGlobalLimit();

    boolean hasPersonalLimit();

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

    @NotNull String getNameTranslated();

    @NotNull List<String> getDescription();

    @NotNull List<String> getDescriptionTranslated();

    double getWeight();

    void setWeight(double weight);

    @NotNull Rarity getRarity();

    void setRarity(@NotNull Rarity rarity);

    boolean isBroadcast();

    void setBroadcast(boolean broadcast);

    void setPlaceholderApply(boolean placeholderApply);

    boolean isPlaceholderApply();

    boolean isOneTimed();

    //@NotNull LimitValues getLimitValues(@NotNull LimitType limitType);

    @NotNull LimitValues getPlayerLimits();

    void setPlayerLimits(@NotNull LimitValues playerLimits);

    @NotNull LimitValues getGlobalLimits();

    void setGlobalLimits(@NotNull LimitValues globalLimits);

    @NotNull ItemStack getPreviewItem();

    @NotNull ItemProvider getPreview();

    void setPreview(@NotNull ItemProvider preview);

    @NotNull Set<String> getIgnoredPermissions();

    void setIgnoredPermissions(@NotNull Set<String> ignoredPermissions);

    @NotNull Set<String> getRequiredPermissions();

    void setRequiredPermissions(@NotNull Set<String> requiredPermissions);
}
