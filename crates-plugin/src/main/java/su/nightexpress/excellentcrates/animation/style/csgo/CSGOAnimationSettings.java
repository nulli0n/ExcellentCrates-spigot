package su.nightexpress.excellentcrates.animation.style.csgo;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.animation.visibility.AnimationVisibility;

@NullMarked
public record CSGOAnimationSettings(String name, int totalRolls, long durationTicks, long finishTickDelay,
                                    AnimationVisibility visibility) {

    public CSGOAnimationSettings(String name, int totalRolls, long durationTicks, long finishTickDelay) {
        this(name, totalRolls, durationTicks, finishTickDelay, AnimationVisibility.PUBLIC);
    }

    public static CSGOAnimationSettings defaultSettings() {
        return new CSGOAnimationSettings("CSGO (Default)", 16, 50L, 20L);
    }
}
