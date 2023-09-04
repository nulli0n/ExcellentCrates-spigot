package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class GiveAllCommand extends AbstractCommand<ExcellentCrates> {

    public GiveAllCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"giveall"}, Perms.COMMAND_KEY_GIVE);
        this.setDescription(plugin.getMessage(Lang.COMMAND_KEY_GIVE_ALL_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_KEY_GIVE_ALL_USAGE));
        this.addFlag(CommandFlags.SILENT);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 2) {
            return plugin.getKeyManager().getKeyIds();
        }
        if (arg == 3) {
            return Arrays.asList("1", "5", "10");
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 4) {
            this.printUsage(sender);
            return;
        }

        CrateKey key = plugin.getKeyManager().getKeyById(result.getArg(2));
        if (key == null) {
            plugin.getMessage(Lang.CRATE_KEY_ERROR_INVALID).send(sender);
            return;
        }

        int amount = Math.abs(result.getInt(3, 1));
        if (amount <= 0) return;

        Collection<CrateUser> users = this.plugin.getUserManager().getUsersLoaded();

        users.forEach(user -> {
            this.plugin.getKeyManager().giveKey(user, key, amount);

            Player target = user.getPlayer();
            if (target != null && !result.hasFlag(CommandFlags.SILENT)) {
                this.plugin.getMessage(Lang.COMMAND_KEY_GIVE_NOTIFY)
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders())
                    .send(target);
            }
        });
        this.plugin.runTaskAsync(task -> {
            users.forEach(user -> plugin.getData().saveUser(user));
        });

        this.plugin.getMessage(Lang.COMMAND_KEY_GIVE_ALL_DONE)
            .replace(Placeholders.GENERIC_AMOUNT, amount)
            .replace(key.replacePlaceholders())
            .send(sender);
    }
}
