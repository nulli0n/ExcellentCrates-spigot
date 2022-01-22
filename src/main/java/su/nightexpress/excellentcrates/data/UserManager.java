package su.nightexpress.excellentcrates.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserManager;
import su.nightexpress.excellentcrates.ExcellentCrates;

public class UserManager extends AbstractUserManager<ExcellentCrates, CrateUser> {

    public UserManager(@NotNull ExcellentCrates plugin) {
        super(plugin, plugin);
    }

    @Override
    @NotNull
    protected CrateUser createData(@NotNull Player player) {
        return new CrateUser(this.plugin, player);
    }
}
