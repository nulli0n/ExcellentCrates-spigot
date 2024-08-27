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
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.OpenSettings;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class OpenForCommand extends AbstractCommand<CratesPlugin> {

    public OpenForCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"openfor"}, Perms.COMMAND_OPEN_FOR);
        this.setUsage(Lang.COMMAND_OPEN_FOR_USAGE);
        this.setDescription(Lang.COMMAND_OPEN_FOR_DESC);
        this.addFlag(CommandFlags.SILENT, CommandFlags.FORCE);
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
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 3) {
            this.errorUsage(sender);
            return;
        }

        Player player = plugin.getServer().getPlayer(result.getArg(1, sender.getName()));
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(2));
        if (crate == null) {
            Lang.ERROR_INVALID_CRATE.getMessage().send(sender);
            return;
        }

        if (!result.hasFlag(CommandFlags.SILENT)) {
            Lang.COMMAND_OPEN_FOR_NOTIFY.getMessage().replace(crate.replacePlaceholders()).send(player);
        }
        if (sender != player) {
            Lang.COMMAND_OPEN_FOR_DONE.getMessage()
                    .replace(Placeholders.forPlayer(player))
                    .replace(crate.replacePlaceholders())
                    .send(sender);
        }

        boolean force = result.hasFlag(CommandFlags.FORCE);
        plugin.getCrateManager().openCrate(player, new CrateSource(crate), new OpenSettings().setForce(force));
    }
}
