package su.nightexpress.excellentcrates.opening.selectable;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

public class SelectableProvider extends AbstractProvider {

    private int selectionAmount = 1;

    private boolean lockSelection;

    private int lockSelectionRedos = 0;

    private SelectableMenu menu;

    public SelectableProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.selectionAmount = ConfigValue.create("Selection.Amount", this.selectionAmount).read(config);

        // Locks selection & displays reward immediately. (Overriding the selected icon & overrides)
        this.lockSelection = ConfigValue.create("Selection.LockSelection", this.lockSelection).read(config);

        // How many times can a player redo their selection if lock is enabled. (This allows for "rerolling" certain rewards for a better player experience)
        this.lockSelectionRedos = ConfigValue.create("Selection.LockSelectionRedos", this.lockSelectionRedos).read(config);

        this.menu = new SelectableMenu(this.plugin);
        this.menu.load(config);
    }

    @Override
    @NotNull
    public SelectableOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        return new SelectableOpening(this.plugin, this, this.menu, player, source, key);
    }

    public int getSelectionAmount() {
        return this.selectionAmount;
    }

    public void setSelectionAmount(int selectionAmount) {
        this.selectionAmount = selectionAmount;
    }

    public boolean isLockSelection() {
        return this.lockSelection;
    }

    public int getLockSelectionRedos() {
        return this.lockSelectionRedos;
    }
}
