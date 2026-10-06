package su.nightexpress.excellentcrates.api.common.cooldown;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class CooldownOptions {

    private boolean      enabled;
    private CooldownMode mode;
    private long         duration;

    public CooldownOptions(boolean enabled, CooldownMode mode, long duration) {
        this.enabled = enabled;
        this.mode = mode;
        this.duration = Math.max(0L, duration);
    }

    public static CooldownOptions defaults() {
        return new CooldownOptions(false, CooldownMode.DAILY, 0L);
    }

    public long getDurationMillis() {
        if (this.mode == CooldownMode.DAILY) {
            LocalTime time = LocalTime.MIDNIGHT;
            LocalDate date = TimeUtil.getCurrentDate().plusDays(this.duration);
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

            return TimeUtil.toEpochMillis(LocalDateTime.of(date, time)) - TimeUtil.toEpochMillis(now);
        }

        return TimeUnit.SECONDS.toMillis(this.duration);
    }

    public long createCooldownTimestamp() {
        if (this.mode == CooldownMode.DAILY) {
            LocalTime time = LocalTime.MIDNIGHT;
            LocalDate date = TimeUtil.getCurrentDate().plusDays(this.duration);

            return TimeUtil.toEpochMillis(LocalDateTime.of(date, time));
        }

        return TimeUtil.createFutureTimestamp(this.duration);
    }

    public boolean hasBiggerDuration(CooldownOptions other) {
        long currentTimestamp = this.createCooldownTimestamp();
        long otherTimestamp = other.createCooldownTimestamp();

        return currentTimestamp > otherTimestamp;
    }

    public boolean isEffectivelyEnabled() {
        return this.isEnabled() && this.duration != 0L;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CooldownMode getMode() {
        return this.mode;
    }

    public void setMode(CooldownMode mode) {
        this.mode = mode;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = Math.max(0L, duration);
    }
}
