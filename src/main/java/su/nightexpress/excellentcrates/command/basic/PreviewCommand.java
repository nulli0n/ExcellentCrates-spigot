package su.nightexpress.excellentcrates.command.basic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class PreviewCommand extends AbstractCommand<CratesPlugin> {

    public PreviewCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"preview"}, Perms.COMMAND_PREVIEW);
        this.setDescription(Lang.COMMAND_PREVIEW_DESC);
        this.setUsage(Lang.COMMAND_PREVIEW_USAGE);
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 2 && player.hasPermission(Perms.COMMAND_PREVIEW_OTHERS)) {
            return Players.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.errorUsage(sender);
            return;
        }
        if (result.length() == 2 && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }
        if (result.length() >= 3 && !sender.hasPermission(Perms.COMMAND_PREVIEW_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(1));
        if (crate == null) {
            Lang.ERROR_INVALID_CRATE.getMessage().send(sender);
            return;
        }

        Player player = plugin.getServer().getPlayer(result.getArg(2, sender.getName()));
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        plugin.getCrateManager().previewCrate(player, new CrateSource(crate));

        if (sender != player) {
            Lang.COMMAND_PREVIEW_DONE_OTHERS.getMessage()
                .replace(Placeholders.forPlayer(player))
                .replace(crate.replacePlaceholders())
                .send(sender);
        }
    }
}
