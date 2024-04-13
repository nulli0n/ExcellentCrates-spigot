package su.nightexpress.excellentcrates.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.nightcore.database.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<CratesPlugin, CrateUser> {

    public UserManager(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    public CrateUser createUserData(@NotNull UUID uuid, @NotNull String name) {
        return new CrateUser(plugin, uuid, name);
    }
}
