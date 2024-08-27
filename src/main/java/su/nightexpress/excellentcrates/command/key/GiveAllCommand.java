package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class GiveAllCommand extends AbstractCommand<CratesPlugin> {

    public GiveAllCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"giveall"}, Perms.COMMAND_KEY_GIVE);
        this.setDescription(Lang.COMMAND_KEY_GIVE_ALL_DESC);
        this.setUsage(Lang.COMMAND_KEY_GIVE_ALL_USAGE);
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
            this.errorUsage(sender);
            return;
        }

        CrateKey key = plugin.getKeyManager().getKeyById(result.getArg(2));
        if (key == null) {
            Lang.ERROR_INVALID_KEY.getMessage().send(sender);
            return;
        }

        int amount = Math.abs(result.getInt(3, 1));
        if (amount == 0) return;

        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(Perms.INCLUDE_KEY_GIVEALL))
                .collect(Collectors.toSet());
        boolean silent = result.hasFlag(CommandFlags.SILENT);

        players.forEach(player -> {
            this.plugin.getKeyManager().giveKey(player, key, amount);

            if (!silent) {
                Lang.COMMAND_KEY_GIVE_NOTIFY.getMessage()
                        .replace(Placeholders.GENERIC_AMOUNT, amount)
                        .replace(key.replacePlaceholders())
                        .send(player);
            }
        });

        if (key.isVirtual()) {
            //this.plugin.runTaskAsync(() -> {
            players.forEach(player -> {
                CrateUser user = this.plugin.getUserManager().getUserData(player);
                this.plugin.getUserManager().saveAsync(user);
            });
            //});
        }

        Lang.COMMAND_KEY_GIVE_ALL_DONE.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(key.replacePlaceholders())
                .send(sender);
    }
}
