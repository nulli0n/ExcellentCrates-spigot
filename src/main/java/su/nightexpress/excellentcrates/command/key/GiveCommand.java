package su.nightexpress.excellentcrates.command.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

class GiveCommand extends ManageCommand {

    public GiveCommand(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, new String[]{"give"}, Perms.COMMAND_KEY_GIVE);
        this.setDescription(plugin.getMessage(Lang.COMMAND_KEY_GIVE_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_KEY_GIVE_USAGE));
        this.setMessageNotify(plugin.getMessage(Lang.COMMAND_KEY_GIVE_NOTIFY));
        this.setMessageDone(plugin.getMessage(Lang.COMMAND_KEY_GIVE_DONE));
    }

    @Override
    protected void manage(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        this.plugin.getKeyManager().giveKey(user, key, amount);
    }
}
