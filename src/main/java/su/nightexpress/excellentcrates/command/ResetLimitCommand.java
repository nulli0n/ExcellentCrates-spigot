package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.config.LangMessage;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.data.CrateUser;

import java.util.List;

public class ResetLimitCommand extends AbstractCommand<ExcellentCrates> {

    public ResetLimitCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"resetlimit"}, Perms.COMMAND_RESETLIMIT);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_ResetLimit_Usage.getMsg();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_ResetLimit_Desc.getMsg();
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

        ICrateReward reward = args.length >= 4 ? crate.getReward(args[3]) : null;
        LangMessage message;
        if (reward == null) {
            user.removeRewardWinLimit(crate.getId());
            message = plugin.lang().Command_ResetLimit_Done_Crate;
        }
        else {
            user.removeRewardWinLimit(crate.getId(), reward.getId());
            message = plugin.lang().Command_ResetLimit_Done_Reward.replace(ICrateReward.PLACEHOLDER_NAME, reward.getName());
        }

        message
            .replace("%player%", user.getName())
            .replace(ICrate.PLACEHOLDER_NAME, crate.getName())
            .send(sender);
    }
}
