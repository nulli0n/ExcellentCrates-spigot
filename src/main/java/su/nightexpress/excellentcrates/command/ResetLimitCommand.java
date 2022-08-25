package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.lang.LangMessage;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.CrateUser;

import java.util.List;

public class ResetLimitCommand extends AbstractCommand<ExcellentCrates> {

    public ResetLimitCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"resetlimit"}, Perms.COMMAND_RESETLIMIT);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_RESET_LIMIT_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_RESET_LIMIT_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return PlayerUtil.getPlayerNames();
        }
        if (arg == 2) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 3) {
            ICrate crate = plugin.getCrateManager().getCrateById(args[2]);
            if (crate != null) {
                return crate.getRewards().stream().map(ICrateReward::getId).toList();
            }
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 3) {
            this.printUsage(sender);
            return;
        }

        CrateUser user = plugin.getUserManager().getUserData(args[1]);
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        ICrate crate = plugin.getCrateManager().getCrateById(args[2]);
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        ICrateReward reward = args.length >= 4 ? crate.getReward(args[3]) : null;
        LangMessage message;
        if (reward == null) {
            user.removeRewardWinLimit(crate.getId());
            message = plugin.getMessage(Lang.COMMAND_RESET_LIMIT_DONE_CRATE);
        }
        else {
            user.removeRewardWinLimit(crate.getId(), reward.getId());
            message = plugin.getMessage(Lang.COMMAND_RESET_LIMIT_DONE_REWARD).replace(Placeholders.REWARD_NAME, reward.getName());
        }

        message
            .replace("%player%", user.getName())
            .replace(Placeholders.CRATE_NAME, crate.getName())
            .send(sender);
    }
}
