package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.api.crate.ICrate;

import java.util.List;

public class ForceOpenCommand extends AbstractCommand<ExcellentCrates> {

    public ForceOpenCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"forceopen"}, Perms.COMMAND_FORCEOPEN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_ForceOpen_Usage.getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_ForceOpen_Desc.getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 2) {
            return PlayerUtil.getPlayerNames();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            this.printUsage(sender);
            return;
        }
        if (args.length == 2 && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }

        ICrate crate = plugin.getCrateManager().getCrateById(args[1]);
        if (crate == null) {
            plugin.lang().Crate_Error_Invalid.send(sender);
            return;
        }

        String pName = args.length >= 3 ? args[2] : sender.getName();
        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        plugin.lang().Command_ForceOpen_Notify
            .replace(ICrate.PLACEHOLDER_NAME, crate.getName()).send(player);

        if (!sender.equals(player)) {
            plugin.lang().Command_ForceOpen_Done
                .replace("%player%", player.getName())
                .replace(ICrate.PLACEHOLDER_NAME, crate.getName())
                .replace(ICrate.PLACEHOLDER_ID, crate.getId())
                .send(sender);
        }

        plugin.getCrateManager().openCrate(player, crate, true, null, null);
    }
}
