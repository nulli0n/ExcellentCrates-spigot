package su.nightexpress.excellentcrates.command.basic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class GiveCommand extends AbstractCommand<CratesPlugin> {

    public GiveCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"give"}, Perms.COMMAND_GIVE);
        this.setDescription(Lang.COMMAND_GIVE_DESC);
        this.setUsage(Lang.COMMAND_GIVE_USAGE);
        this.addFlag(CommandFlags.SILENT);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return Players.playerNames(player);
        }
        if (arg == 2) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 3) {
            return List.of("1", "5", "10");
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 3) {
            this.errorUsage(sender);
            return;
        }

        int amount = result.length() >= 4 ? result.getInt(3, 1) : 1;

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(2));
        if (crate == null) {
            Lang.ERROR_INVALID_CRATE.getMessage().send(sender);
            return;
        }

        Player player = plugin.getServer().getPlayer(result.getArg(1));
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        plugin.getCrateManager().giveCrate(player, crate, amount);

        if (!result.hasFlag(CommandFlags.SILENT)) {
            Lang.COMMAND_GIVE_NOTIFY.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(crate.replacePlaceholders())
                .send(player);
        }
        if (sender != player) {
            Lang.COMMAND_GIVE_DONE.getMessage()
                .replace(Placeholders.forPlayer(player))
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(crate.replacePlaceholders())
                .send(sender);
        }
    }
}
