package su.nightexpress.excellentcrates.user;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.data.DataHandler;
import su.nightexpress.nightcore.db.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<CratesPlugin, CrateUser> {

    public UserManager(@NotNull CratesPlugin plugin, @NotNull DataHandler dataHandler) {
        super(plugin, dataHandler);
    }

    @Override
    @NotNull
    public CrateUser create(@NotNull UUID uuid, @NotNull String name) {
        return new CrateUser(uuid, name);
    }
}
