package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.data.CrateUser;

import java.util.List;

public class ResetCooldownCommand extends AbstractCommand<ExcellentCrates> {

    public ResetCooldownCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"resetcooldown"}, Perms.COMMAND_RESETCOOLDOWN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_ResetCooldown_Usage.getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_ResetCooldown_Desc.getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return PlayerUtil.getPlayerNames();
        }
        if (arg == 2) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            this.printUsage(sender);
            return;
        }

        CrateUser user = plugin.getUserManager().getOrLoadUser(args[1], false);
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        ICrate crate = plugin.getCrateManager().getCrateById(args[2]);
        if (crate == null) {
            plugin.lang().Crate_Error_Invalid.send(sender);
            return;
        }

        user.setCrateCooldown(crate, 0L);
        plugin.lang().Command_ResetCooldown_Done
            .replace("%player%", user.getName())
            .replace(Placeholders.CRATE_NAME, crate.getName())
            .replace(Placeholders.CRATE_ID, crate.getId())
            .send(sender);
    }
}
