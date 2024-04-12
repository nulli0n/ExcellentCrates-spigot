package su.nightexpress.excellentcrates.crate.editor;

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

    default void saveRewards(@NotNull MenuViewer viewer, @NotNull Reward reward, boolean flush) {
        this.saveRewards(viewer, reward.getCrate(), flush);
    }

    default void saveRewards(@NotNull MenuViewer viewer, @NotNull Crate crate, boolean flush) {
        crate.saveRewards();
        if (flush) this.runNextTick(() -> this.flush(viewer));
    }
}
