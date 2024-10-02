package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class LimitValues implements Placeholder {

    public static final int MIDNIGHT_VALUE = -2;

    private final PlaceholderMap placeholders;

    private boolean enabled;
    private int     amount;
    private long    cooldown;
    private int     cooldownStep;

    public LimitValues(boolean enabled, int amount, long cooldown, int cooldownStep) {
        this.setEnabled(enabled);
        this.setAmount(amount);
        this.setCooldown(cooldown);
        this.setCooldownStep(cooldownStep);

        this.placeholders = Placeholders.forLimit(this);
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
        config.set(path + ".Enabled", this.isEnabled());
        config.set(path + ".Amount", this.getAmount());
        config.set(path + ".Cooldown", this.getCooldown());
        config.set(path + ".CooldownStep", this.getCooldownStep());
    }

    @NotNull
    @Override
    public PlaceholderMap getPlaceholders() {
        return this.placeholders;
    }

    public boolean isMidnight() {
        return this.getCooldown() == MIDNIGHT_VALUE;
    }

    public long generateCooldownTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        if (this.isMidnight()) {
            return TimeUtil.toEpochMillis(LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT));
        }
        return TimeUtil.toEpochMillis(now) + this.getCooldown() * 1000L;
    }

    public boolean isCooldownStep(int amount) {
        return amount % this.getCooldownStep() == 0;
    }

    public boolean hasCooldown() {
        return this.getCooldown() > 0L || this.isMidnight();
    }

    public boolean isUnlimitedAmount() {
        return this.getAmount() < 0;
    }

    public boolean isOneTimed() {
        return this.getAmount() == 1;
    }

    public void setMidnightCooldown() {
        this.setCooldown(MIDNIGHT_VALUE);
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

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldownStep() {
        return cooldownStep;
    }

    public void setCooldownStep(int cooldownStep) {
        this.cooldownStep = Math.max(1, cooldownStep);
    }
}
