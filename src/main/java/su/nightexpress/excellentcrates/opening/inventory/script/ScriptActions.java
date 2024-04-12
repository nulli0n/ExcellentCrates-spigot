package su.nightexpress.excellentcrates.opening.inventory.script;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.spinner.SpinMode;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class ScriptActions {

    private static final Map<String, ScriptAction> REGISTRY = new HashMap<>();

    public static final ScriptAction RUN_REWARD = register("run_reward", (opening, result) -> {
        String id = result.get(Parameters.ID, Placeholders.DEFAULT);
        String name = result.get(Parameters.NAME, id);

        int delay = result.get(Parameters.DELAY, 0);
        int[] slots = result.get(Parameters.SLOTS, new int[0]);
        SpinMode mode = result.get(Parameters.SPIN_MODE, SpinMode.INDEPENDENT);
        double chance = result.get(Parameters.CHANCE, 100D);

        opening.schedule(() -> opening.runReward(id, name, mode, slots, chance), delay);
    }, Parameters.ID, Parameters.NAME, Parameters.DELAY, Parameters.SLOTS, Parameters.SPIN_MODE, Parameters.CHANCE);

    public static final ScriptAction RUN_ANIMATION = register("run_animation", (opening, result) -> {
        String id = result.get(Parameters.ID, Placeholders.DEFAULT);
        String name = result.get(Parameters.NAME, id);

        int delay = result.get(Parameters.DELAY, 0);
        int[] slots = result.get(Parameters.SLOTS, new int[0]);
        SpinMode mode = result.get(Parameters.SPIN_MODE, SpinMode.INDEPENDENT);
        double chance = result.get(Parameters.CHANCE, 100D);

        opening.schedule(() -> opening.runAnimation(id, name, mode, slots, chance), delay);
    }, Parameters.ID, Parameters.NAME, Parameters.DELAY, Parameters.SLOTS, Parameters.SPIN_MODE, Parameters.CHANCE);

    public static final ScriptAction STOP_REWARD = register("stop_reward", (opening, result) -> {
        String name = result.get(Parameters.NAME, Placeholders.DEFAULT);
        int delay = result.get(Parameters.DELAY, 0);

        opening.schedule(() -> opening.stopReward(name), delay);
    }, Parameters.NAME, Parameters.DELAY);

    public static final ScriptAction STOP_ANIMATION = register("stop_animation", (opening, result) -> {
        String name = result.get(Parameters.NAME, Placeholders.DEFAULT);
        int delay = result.get(Parameters.DELAY, 0);

        opening.schedule(() -> opening.stopAnimation(name), delay);
    }, Parameters.NAME, Parameters.DELAY);

    public static final ScriptAction PLAY_SOUND = register("play_sound", (opening, result) -> {
        String name = result.get(Parameters.NAME, Placeholders.DEFAULT);
        int delay = result.get(Parameters.DELAY, 0);

        Sound sound = StringUtil.getEnum(name, Sound.class).orElse(null);
        if (sound == null) return;

        opening.schedule(() -> UniSound.of(sound).play(opening.getPlayer()), delay);
    }, Parameters.NAME, Parameters.DELAY);

    @NotNull
    public static ScriptAction register(@NotNull String name,
                                        @NotNull BiConsumer<InventoryOpening, ParameterResult> executor,
                                        Parameter<?>...                       parameters) {
        return register(new ScriptAction(name, executor, parameters));
    }

    @NotNull
    public static ScriptAction register(@NotNull ScriptAction action) {
        REGISTRY.put(action.getName(), action);
        return action;
    }

    @Nullable
    public static ScriptAction getByName(@NotNull String name) {
        return REGISTRY.get(name.toLowerCase());
    }

    @NotNull
    public static Set<ScriptAction> getActions() {
        return new HashSet<>(REGISTRY.values());
    }
}
