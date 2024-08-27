package su.nightexpress.excellentcrates.opening.spinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.wrapper.UniSound;

public class SpinSettings {

    private final int spinTimes;
    private final long spinTickInterval;
    private final long spinSlowdownStep;
    private final long spinSlowdownAmount;
    private final UniSound spinSound;

    public SpinSettings(
            int spinTimes,
            long spinTickInterval,
            long spinSlowdownStep,
            long spinSlowdownAmount,
            @Nullable UniSound spinSound
    ) {
        this.spinTimes = spinTimes;
        this.spinTickInterval = Math.max(1, spinTickInterval);
        this.spinSlowdownStep = spinSlowdownStep;
        this.spinSlowdownAmount = spinSlowdownAmount;
        this.spinSound = spinSound;
    }

    @NotNull
    public static SpinSettings read(@NotNull FileConfig cfg, @NotNull String path) {
        int spinTimes = ConfigValue.create(path + ".Spin_Times", 50).read(cfg);

        long spinTickInterval = ConfigValue.create(path + ".Spin_Interval", 1L).read(cfg);

        long spinSlowdownStep = ConfigValue.create(path + ".Spin_Slowdown.Step", 5L).read(cfg);

        long spinSlowdownAmount = ConfigValue.create(path + ".Spin_Slowdown.Amount", 1L).read(cfg);

        UniSound spinSound = null;
        if (cfg.contains(path + ".Spin_Sound")) {
            spinSound = UniSound.read(cfg, path + ".Spin_Sound");
        }

        return new SpinSettings(spinTimes, spinTickInterval, spinSlowdownStep, spinSlowdownAmount, spinSound);
    }

    public int getSpinTimes() {
        return spinTimes;
    }

    public long getSpinTickInterval() {
        return spinTickInterval;
    }

    public long getSpinSlowdownAmount() {
        return spinSlowdownAmount;
    }

    public long getSpinSlowdownStep() {
        return spinSlowdownStep;
    }

    @Nullable
    public UniSound getSpinSound() {
        return spinSound;
    }
}
