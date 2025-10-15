package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OpenOptions {

    private final Set<Option> options;

    private OpenOptions() {
        this.options = new HashSet<>();
    }

    @NotNull
    public static OpenOptions empty() {
        return new OpenOptions();
    }

    @NotNull
    public static OpenOptions of(@NotNull Option... options) {
        return new OpenOptions();
    }

    @NotNull
    public static OpenOptions ignoreRestrictions() {
        return of(
            Option.IGNORE_COOLDOWN,
            Option.IGNORE_COST,
            Option.IGNORE_PERMISSION
        );
    }

    public boolean has(@NotNull Option option) {
        return this.options.contains(option);
    }

    @NotNull
    public OpenOptions add(@NotNull Option... options) {
        this.options.addAll(Arrays.asList(options));
        return this;
    }

    @NotNull
    public OpenOptions remove(@NotNull Option... options) {
        Arrays.asList(options).forEach(this.options::remove);
        return this;
    }

    public enum Option {
        IGNORE_COOLDOWN,
        IGNORE_COST,
        IGNORE_PERMISSION,
        IGNORE_ANIMATION
    }
}
