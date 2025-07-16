package su.nightexpress.excellentcrates.opening.inventory.spinner;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class SpinStep {

    private static final String DELIMITER = ":";

    private final int spinsAmount;
    private final int tickInterval;

    public SpinStep(int spinsAmount, int tickInterval) {
        this.spinsAmount = spinsAmount;
        this.tickInterval = tickInterval;
    }

    @NotNull
    public static SpinStep of(int spinsAmount, int tickInterval) {
        return new SpinStep(spinsAmount, tickInterval);
    }

    @NotNull
    public static SpinStep deserialize(@NotNull String str) {
        String[] split = str.split(DELIMITER);
        int amount = NumberUtil.getIntegerAbs(split[0]);
        int tickInterval = split.length >= 2 ? NumberUtil.getIntegerAbs(split[1]) : 0;

        return new SpinStep(amount, tickInterval);
    }

    @NotNull
    public String serialize() {
        return this.spinsAmount + DELIMITER + this.tickInterval;
    }

    @NotNull
    public static List<SpinStep> convertFromFlat(int totalSpins, int initialSpeed, int slowdownInterval, int slowdownStrength) {
        if (slowdownInterval <= 0 || slowdownStrength <= 0) {
            return Lists.newList(new SpinStep(totalSpins, initialSpeed));
        }

        List<SpinStep> segments = new ArrayList<>();
        int remainingSpins = totalSpins;
        int index = 0;

        while (remainingSpins > 0) {
            int spinsThisStep = Math.min(slowdownInterval, remainingSpins);
            int speed = initialSpeed + (index * slowdownStrength);
            segments.add(new SpinStep(spinsThisStep, speed));

            remainingSpins -= spinsThisStep;
            index++;
        }

        return segments;
    }

    public int getSpinsAmount() {
        return this.spinsAmount;
    }

    public int getTickInterval() {
        return this.tickInterval;
    }

    @Override
    public String toString() {
        return "SpinSpeed{" +
            "amount=" + spinsAmount +
            ", speed=" + tickInterval +
            '}';
    }
}
