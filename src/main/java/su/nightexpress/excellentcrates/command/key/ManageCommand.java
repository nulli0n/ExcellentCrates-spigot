package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.api.lang.LangMessage;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.Arrays;
import java.util.List;

abstract class ManageCommand extends AbstractCommand<ExcellentCrates> {

    protected LangMessage messageNotify;
    protected LangMessage messageDone;

    public ManageCommand(@NotNull ExcellentCrates plugin, @NotNull String[] aliases, @Nullable Permission permission) {
        super(plugin, aliases, permission);
        this.addFlag(CommandFlags.SILENT);
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
            return CollectionsUtil.playerNames(player);
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
            this.printUsage(sender);
            return;
        }

        CrateKey key = plugin.getKeyManager().getKeyById(result.getArg(3));
        if (key == null) {
            plugin.getMessage(Lang.CRATE_KEY_ERROR_INVALID).send(sender);
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
