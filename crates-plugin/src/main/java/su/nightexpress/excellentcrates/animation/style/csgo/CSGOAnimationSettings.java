package su.nightexpress.excellentcrates.animation.style.csgo;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record CSGOAnimationSettings(String name, int totalRolls, long durationTicks, long finishTickDelay) {

    public static CSGOAnimationSettings defaultSettings() {
        return new CSGOAnimationSettings("CSGO (Default)", 16, 50L, 20L);
    }
}
