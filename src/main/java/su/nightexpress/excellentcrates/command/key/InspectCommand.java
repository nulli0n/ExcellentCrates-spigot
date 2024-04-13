package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class InspectCommand extends AbstractCommand<CratesPlugin> {

    public InspectCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"inspect"}, Perms.COMMAND_KEY_INSPECT);
        this.setDescription(Lang.COMMAND_KEY_INSPECT_DESC);
        this.setUsage(Lang.COMMAND_KEY_INSPECT_USAGE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 2 && player.hasPermission(Perms.COMMAND_KEY_INSPECT_OTHERS)) {
            return Players.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() >= 3 && !sender.hasPermission(Perms.COMMAND_KEY_INSPECT_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        String targetName = result.getArg(2, sender.getName());
        this.plugin.getUserManager().getUserDataAsync(targetName).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            Lang.COMMAND_KEY_INSPECT_LIST.getMessage()
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(Placeholders.GENERIC_ENTRY, list -> {
                    this.plugin.getKeyManager().getKeys().forEach(key -> {
                        if (!key.isVirtual()) return;

                        list.add(key.replacePlaceholders().apply(Lang.COMMAND_KEY_INSPECT_ENTRY.getString()
                            .replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(user.getKeys(key.getId())))
                        ));
                    });
                })
                .send(sender);
        });
    }
}
