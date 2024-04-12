package su.nightexpress.excellentcrates.command.basic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.OpenSettings;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;

import java.util.List;

public class OpenCommand extends AbstractCommand<CratesPlugin> {

    public OpenCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"open"}, Perms.COMMAND_OPEN);
        this.setUsage(Lang.COMMAND_OPEN_USAGE);
        this.setDescription(Lang.COMMAND_OPEN_DESC);
        this.addFlag(CommandFlags.SILENT);
        this.setPlayerOnly(true);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.errorUsage(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(1));
        if (crate == null) {
            Lang.ERROR_INVALID_CRATE.getMessage().send(sender);
            return;
        }

        Player player = (Player) sender;
        plugin.getCrateManager().openCrate(player, new CrateSource(crate), new OpenSettings());
    }
}
