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

    private SelectableMenu menu;

    public SelectableProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.selectionAmount = ConfigValue.create("Selection.Amount", this.selectionAmount).read(config);

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
}
