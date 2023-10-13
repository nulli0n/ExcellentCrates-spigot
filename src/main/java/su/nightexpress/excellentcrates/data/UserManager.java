package su.nightexpress.excellentcrates.data;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserManager;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.data.impl.CrateUser;

import java.util.UUID;

public class UserManager extends AbstractUserManager<ExcellentCratesPlugin, CrateUser> {

    public UserManager(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, plugin);
    }

    @Override
    @NotNull
    protected CrateUser createData(@NotNull UUID uuid, @NotNull String name) {
        return new CrateUser(plugin, uuid, name);
    }
}
