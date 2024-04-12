package su.nightexpress.excellentcrates.command.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

class GiveCommand extends ManageCommand {

    public GiveCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"give"}, Perms.COMMAND_KEY_GIVE);
        this.setDescription(Lang.COMMAND_KEY_GIVE_DESC);
        this.setUsage(Lang.COMMAND_KEY_GIVE_USAGE);
        this.setMessageNotify(Lang.COMMAND_KEY_GIVE_NOTIFY.getMessage());
        this.setMessageDone(Lang.COMMAND_KEY_GIVE_DONE.getMessage());
    }

    @Override
    protected void manage(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        this.plugin.getKeyManager().giveKey(user, key, amount);
    }
}
