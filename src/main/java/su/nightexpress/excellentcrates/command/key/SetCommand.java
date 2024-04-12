package su.nightexpress.excellentcrates.command.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

class SetCommand extends ManageCommand {

    public SetCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"set"}, Perms.COMMAND_KEY_SET);
        this.setDescription(Lang.COMMAND_KEY_SET_DESC);
        this.setUsage(Lang.COMMAND_KEY_SET_USAGE);
        this.setMessageNotify(Lang.COMMAND_KEY_SET_NOTIFY.getMessage());
        this.setMessageDone(Lang.COMMAND_KEY_SET_DONE.getMessage());
    }

    @Override
    protected void manage(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        this.plugin.getKeyManager().setKey(user, key, amount);
    }
}
