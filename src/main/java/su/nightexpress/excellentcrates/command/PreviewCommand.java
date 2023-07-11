package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.List;

public class PreviewCommand extends AbstractCommand<ExcellentCrates> {

    public PreviewCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"preview"}, Perms.COMMAND_PREVIEW);
        this.setDescription(plugin.getMessage(Lang.COMMAND_PREVIEW_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_PREVIEW_USAGE));
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 2 && player.hasPermission(Perms.COMMAND_PREVIEW_OTHERS)) {
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
        if (result.length() >= 3 && !sender.hasPermission(Perms.COMMAND_PREVIEW_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(1));
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        String pName = result.getArg(2, sender.getName());
        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        crate.openPreview(player);

        if (sender != player) {
            plugin.getMessage(Lang.COMMAND_PREVIEW_DONE_OTHERS)
                .replace(Placeholders.forPlayer(player))
                .replace(crate.replacePlaceholders())
                .send(sender);
        }
    }
}
