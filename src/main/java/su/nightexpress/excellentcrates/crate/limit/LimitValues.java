package su.nightexpress.excellentcrates.crate.limit;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.TimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.UnaryOperator;

public class LimitValues {

    public static final int NEVER_RESET    = -1;
    public static final int MIDNIGHT_RESET = -2;

    private boolean enabled;
    private int     amount;
    private long    resetTime;
    private int     resetStep;

    public LimitValues(boolean enabled, int amount, long resetTime, int resetStep) {
        this.setEnabled(enabled);
        this.setAmount(amount);
        this.setResetTime(resetTime);
        this.setResetStep(resetStep);
    }

    @NotNull
    public static LimitValues unlimited() {
        return new LimitValues(false, -1, 0, 1);
    }

    @NotNull
    public static LimitValues read(@NotNull FileConfig config, @NotNull String path) {
        boolean enabled = ConfigValue.create(path + ".Enabled", false).read(config);
        int amount = ConfigValue.create(path + ".Amount", -1).read(config);
        long cooldown = ConfigValue.create(path + ".Cooldown", 0L).read(config);
        int cooldownStep = ConfigValue.create(path + ".CooldownStep", 1).read(config);

        return new LimitValues(enabled, amount, cooldown, cooldownStep);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Enabled", this.enabled);
        config.set(path + ".Amount", this.amount);
        config.set(path + ".Cooldown", this.resetTime);
        config.set(path + ".CooldownStep", this.resetStep);
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.LIMIT_VALUES.replacer(this);
    }

    public boolean isMidnight() {
        return this.resetTime == MIDNIGHT_RESET;
    }

    public boolean isNeverReset() {
        return this.resetTime == NEVER_RESET || this.resetTime == 0;
    }

    public long generateResetTimestamp() {
        if (this.isNeverReset()) return -1L;

        LocalDateTime now = LocalDateTime.now();
        if (this.isMidnight()) {
            return TimeUtil.toEpochMillis(LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT));
        }
        return TimeUtil.toEpochMillis(now) + this.resetTime * 1000L;
    }

    public boolean isResetStep(int amount) {
        return amount % this.resetStep == 0;
    }

//    public boolean hasResetTime() {
//        return this.resetTime != 0L;// this.resetTime > 0L || this.isMidnight() || this.isNeverReset();
//    }

    public boolean isUnlimitedAmount() {
        return this.amount < 0;
    }

    public boolean isOneTimed() {
        return this.amount == 1;
    }

    public void setMidnightCooldown() {
        this.setResetTime(MIDNIGHT_RESET);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getResetTime() {
        return resetTime;
    }

    public void setResetTime(long resetTime) {
        this.resetTime = resetTime;
    }

    public int getResetStep() {
        return resetStep;
    }

    public void setResetStep(int resetStep) {
        this.resetStep = Math.max(1, resetStep);
    }
}
