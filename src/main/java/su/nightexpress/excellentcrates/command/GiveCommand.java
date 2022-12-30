package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.Crate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GiveCommand extends AbstractCommand<ExcellentCrates> {

    public GiveCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"give"}, Perms.COMMAND_GIVE);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_GIVE_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_GIVE_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            List<String> list = new ArrayList<>(PlayerUtil.getPlayerNames());
            list.add(0, Placeholders.WILDCARD);
            return list;
        }
        if (arg == 2) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 3) {
            return Arrays.asList("1", "5", "10");
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        if (args.length < 3) {
            this.printUsage(sender);
            return;
        }

        String pName = args[1];
        String crateId = args[2];
        int amount = args.length >= 4 ? StringUtil.getInteger(args[3], 1) : 1;

        Crate crate = plugin.getCrateManager().getCrateById(crateId);
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        if (pName.equalsIgnoreCase(Placeholders.WILDCARD)) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.getCrateManager().giveCrate(player, crate, amount);
            }
        }
        else {
            Player player = plugin.getServer().getPlayer(pName);
            if (player == null) {
                this.errorPlayer(sender);
                return;
            }
            plugin.getCrateManager().giveCrate(player, crate, amount);
            plugin.getMessage(Lang.COMMAND_GIVE_NOTIFY)
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(Placeholders.CRATE_NAME, crate.getName())
                .send(player);
        }

        plugin.getMessage(Lang.COMMAND_GIVE_DONE)
            .replace(Placeholders.Player.NAME, pName)
            .replace(Placeholders.GENERIC_AMOUNT, amount)
            .replace(Placeholders.CRATE_NAME, crate.getName())
            .replace(Placeholders.CRATE_ID, crate.getId())
            .send(sender);
    }
}
