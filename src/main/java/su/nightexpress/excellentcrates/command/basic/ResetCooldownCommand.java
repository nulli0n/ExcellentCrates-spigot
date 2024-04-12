package su.nightexpress.excellentcrates.command.basic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class ResetCooldownCommand extends AbstractCommand<CratesPlugin> {

    public ResetCooldownCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"resetcooldown"}, Perms.COMMAND_RESETCOOLDOWN);
        this.setDescription(Lang.COMMAND_RESET_COOLDOWN_DESC);
        this.setUsage(Lang.COMMAND_RESET_COOLDOWN_USAGE);
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

        CrateUser user = plugin.getUserManager().getUserData(result.getArg(1));
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(2));
        if (crate == null) {
            Lang.ERROR_INVALID_CRATE.getMessage().send(sender);
            return;
        }

        user.setCrateCooldown(crate, 0L);
        this.plugin.getUserManager().saveAsync(user);

        Lang.COMMAND_RESET_COOLDOWN_DONE.getMessage()
            .replace(Placeholders.PLAYER_NAME, user.getName())
            .replace(crate.replacePlaceholders())
            .send(sender);
    }
}
