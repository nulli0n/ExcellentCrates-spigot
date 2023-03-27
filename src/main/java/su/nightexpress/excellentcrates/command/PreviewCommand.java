package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.List;
import java.util.Map;

public class PreviewCommand extends AbstractCommand<ExcellentCrates> {

    public PreviewCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"preview"}, Perms.COMMAND_PREVIEW);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_PREVIEW_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_PREVIEW_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
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
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        if (args.length < 2) {
            this.printUsage(sender);
            return;
        }
        if (args.length == 2 && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }
        if (args.length >= 3 && !sender.hasPermission(Perms.COMMAND_PREVIEW_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(args[1]);
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        String pName = args.length >= 3 ? args[2] : sender.getName();
        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        crate.openPreview(player);

        if (!sender.equals(player)) {
            plugin.getMessage(Lang.COMMAND_PREVIEW_DONE_OTHERS)
                .replace(Placeholders.Player.replacer(player))
                .replace(Placeholders.CRATE_NAME, crate.getName())
                .replace(Placeholders.CRATE_ID, crate.getId())
                .send(sender);
        }
    }
}
