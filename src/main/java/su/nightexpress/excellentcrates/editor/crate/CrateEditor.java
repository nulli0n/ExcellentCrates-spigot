package su.nightexpress.excellentcrates.editor.crate;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.Menu;

public interface CrateEditor extends Menu {

    default void saveSettings(@NotNull MenuViewer viewer, @NotNull Crate crate, boolean flush) {
        crate.saveSettings();
        if (flush) this.runNextTick(() -> this.flush(viewer));
    }

    default void saveMilestones(@NotNull MenuViewer viewer, @NotNull Crate crate, boolean flush) {
        crate.saveMilestones();
        if (flush) this.runNextTick(() -> this.flush(viewer));
    }

    default void saveRewards(@NotNull MenuViewer viewer, @NotNull Crate crate, boolean flush) {
        crate.saveRewards();
        if (flush) this.runNextTick(() -> this.flush(viewer));
    }

    default void saveReward(@NotNull MenuViewer viewer, @NotNull Reward reward, boolean flush) {
        reward.getCrate().saveReward(reward);
        if (flush) this.runNextTick(() -> this.flush(viewer));
    }
}
