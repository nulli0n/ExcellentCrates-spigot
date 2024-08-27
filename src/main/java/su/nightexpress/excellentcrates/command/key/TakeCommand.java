package su.nightexpress.excellentcrates.command.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

class TakeCommand extends ManageCommand {

    public TakeCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"take"}, Perms.COMMAND_KEY_TAKE);
        this.setDescription(Lang.COMMAND_KEY_TAKE_DESC);
        this.setUsage(Lang.COMMAND_KEY_TAKE_USAGE);
        this.setMessageNotify(Lang.COMMAND_KEY_TAKE_NOTIFY.getMessage());
        this.setMessageDone(Lang.COMMAND_KEY_TAKE_DONE.getMessage());
    }

    @Override
    protected void manage(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        this.plugin.getKeyManager().takeKey(user, key, amount);
    }
}
