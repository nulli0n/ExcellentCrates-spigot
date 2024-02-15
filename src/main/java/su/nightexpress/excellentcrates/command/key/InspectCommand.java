package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;

import java.util.List;

public class InspectCommand extends AbstractCommand<ExcellentCratesPlugin> {

    public InspectCommand(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, new String[]{"inspect"}, Perms.COMMAND_KEY_INSPECT);
        this.setDescription(plugin.getMessage(Lang.COMMAND_KEY_INSPECT_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_KEY_INSPECT_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 2 && player.hasPermission(Perms.COMMAND_KEY_INSPECT_OTHERS)) {
            return CollectionsUtil.playerNames(player);
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

            this.plugin.getMessage(Lang.COMMAND_KEY_INSPECT_LIST)
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(str -> str.contains(Placeholders.KEY_NAME), (line, list) -> {
                    this.plugin.getKeyManager().getKeys().forEach(key -> {
                        if (!key.isVirtual()) return;

                        list.add(key.replacePlaceholders().apply(line
                            .replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(user.getKeys(key.getId())))
                        ));
                    });
                })
                .send(sender);
        });
    }
}
