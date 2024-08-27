package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.Players;

import java.util.Arrays;
import java.util.List;

abstract class ManageCommand extends AbstractCommand<CratesPlugin> {

    protected LangMessage messageNotify;
    protected LangMessage messageDone;

    public ManageCommand(@NotNull CratesPlugin plugin, @NotNull String[] aliases, @Nullable Permission permission) {
        super(plugin, aliases, permission);
        this.addFlag(CommandFlags.SILENT, CommandFlags.NO_SAVE);
    }

    public void setMessageNotify(@NotNull LangMessage messageNotify) {
        this.messageNotify = messageNotify;
    }

    public void setMessageDone(@NotNull LangMessage messageDone) {
        this.messageDone = messageDone;
    }

    protected abstract void manage(@NotNull CrateUser user, @NotNull CrateKey key, int amount);

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 2) {
            return Players.playerNames(player);
        }
        if (arg == 3) {
            return plugin.getKeyManager().getKeyIds();
        }
        if (arg == 4) {
            return Arrays.asList("1", "5", "10");
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 5) {
            this.errorUsage(sender);
            return;
        }

        CrateKey key = plugin.getKeyManager().getKeyById(result.getArg(3));
        if (key == null) {
            Lang.ERROR_INVALID_KEY.getMessage().send(sender);
            return;
        }

        int amount = Math.abs(result.getInt(4, 1));
        if (amount <= 0) return;

        CrateUser user = plugin.getUserManager().getUserData(result.getArg(2));
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        this.manage(user, key, amount);
        if (!result.hasFlag(CommandFlags.NO_SAVE)) {
            this.plugin.getUserManager().saveAsync(user);
        }

        Player target = user.getPlayer();
        if (target != null && !result.hasFlag(CommandFlags.SILENT)) {
            this.messageNotify
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders())
                    .send(target);
        }

        this.messageDone
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(key.replacePlaceholders())
                .send(sender);
    }
}
