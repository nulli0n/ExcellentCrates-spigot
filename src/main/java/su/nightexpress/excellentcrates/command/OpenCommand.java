package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandFlag;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.OpenSettings;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.List;

public class OpenCommand extends AbstractCommand<ExcellentCrates> {

    private static final CommandFlag<Boolean> FORCE = CommandFlag.booleanFlag("f");

    public OpenCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"open"}, Perms.COMMAND_OPEN);
        this.setUsage(plugin.getMessage(Lang.COMMAND_OPEN_USAGE));
        this.setDescription(plugin.getMessage(Lang.COMMAND_OPEN_DESC));
        this.addFlag(CommandFlags.SILENT, FORCE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 2) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.printUsage(sender);
            return;
        }
        if (result.length() == 2 && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(1));
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        String pName = result.length() >= 3 ? result.getArg(2) : sender.getName();
        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        if (!result.hasFlag(CommandFlags.SILENT)) {
            plugin.getMessage(Lang.COMMAND_OPEN_NOTIFY).replace(crate.replacePlaceholders()).send(player);
        }
        if (sender != player) {
            plugin.getMessage(Lang.COMMAND_OPEN_DONE)
                .replace(Placeholders.Player.replacer(player))
                .replace(crate.replacePlaceholders())
                .send(sender);
        }

        boolean force = sender.hasPermission(Perms.COMMAND) && result.hasFlag(FORCE);
        plugin.getCrateManager().openCrate(player, crate, new OpenSettings().setForce(force));
    }
}
