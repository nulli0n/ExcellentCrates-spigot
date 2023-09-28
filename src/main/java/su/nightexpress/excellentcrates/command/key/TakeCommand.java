package su.nightexpress.excellentcrates.command.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

class TakeCommand extends ManageCommand {

    public TakeCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"take"}, Perms.COMMAND_KEY_TAKE);
        this.setDescription(plugin.getMessage(Lang.COMMAND_KEY_TAKE_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_KEY_TAKE_USAGE));
        this.setMessageNotify(plugin.getMessage(Lang.COMMAND_KEY_TAKE_NOTIFY));
        this.setMessageDone(plugin.getMessage(Lang.COMMAND_KEY_TAKE_DONE));
    }

    @Override
    protected void manage(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        this.plugin.getKeyManager().takeKey(user, key, amount);
        this.plugin.getUserManager().saveUser(user);
    }
}
