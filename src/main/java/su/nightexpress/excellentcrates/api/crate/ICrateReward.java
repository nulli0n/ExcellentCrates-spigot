package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.manager.IEditable;
import su.nexmedia.engine.api.manager.IPlaceholder;

import java.util.List;

public interface ICrateReward extends IEditable, ICleanable, IPlaceholder {

    String PLACEHOLDER_ID                 = "%reward_id%";
    String PLACEHOLDER_NAME               = "%reward_name%";
    String PLACEHOLDER_CHANCE             = "%reward_chance%";
    String PLACEHOLDER_BROADCAST          = "%reward_broadcast%";
    String PLACEHOLDER_PREVIEW_NAME       = "%reward_preview_name%";
    String PLACEHOLDER_PREVIEW_LORE       = "%reward_preview_lore%";
    String PLACEHOLDER_COMMANDS           = "%reward_commands%";
    String PLACEHOLDER_WIN_LIMIT_AMOUNT   = "%reward_win_limit_amount%";
    String PLACEHOLDER_WIN_LIMIT_COOLDOWN = "%reward_win_limit_cooldown%";

    @NotNull String getId();

    @NotNull ICrate getCrate();

    @NotNull String getName();

    void setName(@NotNull String name);

    double getChance();

    void setChance(double chance);

    boolean isBroadcast();

    void setBroadcast(boolean broadcast);

    int getWinLimitAmount();

    void setWinLimitAmount(int winLimitAmount);

    long getWinLimitCooldown();

    void setWinLimitCooldown(long winLimitCooldown);

    default boolean isWinLimitedAmount() {
        return this.getWinLimitAmount() >= 0;
    }

    default boolean isWinLimitedCooldown() {
        return this.getWinLimitCooldown() != 0;
    }

    default boolean isWinLimitedOnce() {
        return this.getWinLimitAmount() == 1 || this.getWinLimitCooldown() < 0;
    }

    boolean canWin(@NotNull Player player);

    @NotNull ItemStack getPreview();

    void setPreview(@NotNull ItemStack preview);

    @NotNull List<String> getCommands();

    void setCommands(@NotNull List<String> commands);

    @NotNull List<ItemStack> getItems();

    void setItems(@NotNull List<ItemStack> items);

    void give(@NotNull Player player);
}
