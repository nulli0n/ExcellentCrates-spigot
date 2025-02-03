package su.nightexpress.excellentcrates.ui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.ui.impl.ConfirmMenu;
import su.nightexpress.nightcore.manager.AbstractManager;

public class UIManager extends AbstractManager<CratesPlugin> {

    private ConfirmMenu confirmMenu;

    public UIManager(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.confirmMenu = new ConfirmMenu(this.plugin);
    }

    @Override
    protected void onShutdown() {
        if (this.confirmMenu != null) this.confirmMenu.clear();
    }

    public void openConfirm(@NotNull Player player, @NotNull Confirmation confirmation) {
        this.confirmMenu.open(player, confirmation);
    }
}
