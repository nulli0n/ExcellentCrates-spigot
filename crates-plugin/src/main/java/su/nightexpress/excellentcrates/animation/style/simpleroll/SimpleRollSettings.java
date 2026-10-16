package su.nightexpress.excellentcrates.animation.style.simpleroll;

public class SimpleRollSettings {

    private final String name;
    private final int    rollAmount;
    private final int    rollInterval;
    private final int    finishDelay;

    public SimpleRollSettings(String name, int rollAmount, int rollInterval, int finishDelay) {
        this.name = name;
        this.rollAmount = rollAmount;
        this.rollInterval = rollInterval;
        this.finishDelay = finishDelay;
    }

    public static SimpleRollSettings defaultSettings() {
        return new SimpleRollSettings("Simple Roll (Default)", 12, 5, 40);
    }

    public String getName() {
        return name;
    }

    public int getRollAmount() {
        return rollAmount;
    }

    public int getRollInterval() {
        return rollInterval;
    }

    public int getFinishDelay() {
        return finishDelay;
    }
}
