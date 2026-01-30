package su.nightexpress.excellentcrates.crate.limit;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.TimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class LimitValues implements Writeable {

    private static final int UNLIMITED = -1;

    private boolean      enabled;
    private CooldownMode cooldownType;

    private int  globalAmount;
    private int  playerAmount;
    private long globalCooldown;
    private long playerCooldown;

    public LimitValues(boolean enabled, @NotNull CooldownMode cooldownType, int globalAmount, int playerAmount, long globalCooldown, long playerCooldown) {
        this.setEnabled(enabled);
        this.setCooldownType(cooldownType);
        this.setGlobalAmount(globalAmount);
        this.setPlayerAmount(playerAmount);
        this.setGlobalCooldown(globalCooldown);
        this.setPlayerCooldown(playerCooldown);
    }

    @NotNull
    public static LimitValues unlimited() {
        return new LimitValues(false, CooldownMode.DAILY, UNLIMITED, UNLIMITED, 0, 0);
    }

    @NotNull
    public static LimitValues read(@NotNull FileConfig config, @NotNull String path) {
        boolean enabled = ConfigValue.create(path + ".Enabled", false).read(config);
        CooldownMode cooldownType = ConfigValue.create(path + ".CooldownType", CooldownMode.class, CooldownMode.DAILY).read(config);

        int globalAmount = ConfigValue.create(path + ".GlobalAmount", UNLIMITED).read(config);
        int playerAmount = ConfigValue.create(path + ".PlayerAmount", UNLIMITED).read(config);

        long globalCooldown = ConfigValue.create(path + ".GlobalCooldown", 0L).read(config);
        long playerCooldown = ConfigValue.create(path + ".PlayerCooldown", 0L).read(config);

        return new LimitValues(enabled, cooldownType, globalAmount, playerAmount, globalCooldown, playerCooldown);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Enabled", this.enabled);
        config.set(path + ".CooldownType", this.cooldownType.name());

        config.set(path + ".GlobalAmount", this.globalAmount);
        config.set(path + ".PlayerAmount", this.playerAmount);

        config.set(path + ".GlobalCooldown", this.globalCooldown);
        config.set(path + ".PlayerCooldown", this.playerCooldown);
    }

    public long generateGlobalCooldown() {
        return this.createCooldownTimestamp(this.globalCooldown);
    }

    public long generatePlayerCooldown() {
        return this.createCooldownTimestamp(this.playerCooldown);
    }

    private long createCooldownTimestamp(long cooldown) {
        if (cooldown == 0L) return 0L;

        return switch (this.cooldownType) {
            case CUSTOM -> TimeUtil.createFutureTimestamp(cooldown);
            case DAILY -> TimeUtil.toEpochMillis(LocalDateTime.of(TimeUtil.getCurrentDate().plusDays(1), LocalTime.MIDNIGHT));
        };
    }

    public boolean isGlobalAmountLimited() {
        return this.globalAmount > UNLIMITED;
    }

    public boolean isPlayerAmountLimited() {
        return this.playerAmount > UNLIMITED;
    }

    public boolean hasGlobalCooldown() {
        return this.globalCooldown > 0L;
    }

    public boolean hasPlayerCooldown() {
        return this.playerCooldown > 0L;
    }

    public boolean isAmountLimited() {
        return this.isGlobalAmountLimited() || this.isPlayerAmountLimited();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @NotNull
    public CooldownMode getCooldownType() {
        return this.cooldownType;
    }

    public void setCooldownType(@NotNull CooldownMode cooldownType) {
        this.cooldownType = cooldownType;
    }

    public int getGlobalAmount() {
        return globalAmount;
    }

    public void setGlobalAmount(int globalAmount) {
        this.globalAmount = globalAmount;
    }

    public int getPlayerAmount() {
        return this.playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }

    public long getGlobalCooldown() {
        return this.globalCooldown;
    }

    public void setGlobalCooldown(long globalCooldown) {
        this.globalCooldown = Math.max(0, globalCooldown);
    }

    public long getPlayerCooldown() {
        return this.playerCooldown;
    }

    public void setPlayerCooldown(long playerCooldown) {
        this.playerCooldown = Math.max(0, playerCooldown);
    }
}
