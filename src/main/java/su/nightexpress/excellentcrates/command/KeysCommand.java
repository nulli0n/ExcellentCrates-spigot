package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.data.CrateUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeysCommand extends AbstractCommand<ExcellentCrates> {

    public KeysCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"keys", "checkkey"}, Perms.COMMAND_KEYS);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_CheckKey_Usage.getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_CheckKey_Desc.getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1 && player.hasPermission(Perms.COMMAND_KEYS_OTHERS)) {
            return PlayerUtil.getPlayerNames();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 2 && !sender.hasPermission(Perms.COMMAND_KEYS_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        String pName = args.length >= 2 ? args[1] : sender.getName();
        Player player = plugin.getServer().getPlayer(pName);
        CrateUser user = plugin.getUserManager().getOrLoadUser(pName, false);
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        Map<ICrateKey, Integer> keys = new HashMap<>();
        for (ICrateKey key : plugin.getKeyManager().getKeys()) {
            int has;
            if (!key.isVirtual()) {
                has = player != null ? plugin.getKeyManager().getKeysAmount(player, key) : -2;
            }
            else {
                has = user.getKeys(key.getId());
            }
            keys.put(key, has);
        }

        plugin.lang().Command_CheckKey_Format_List.replace("%player%", user.getName()).asList()
            .forEach(line -> {
                if (line.contains(ICrateKey.PLACEHOLDER_NAME)) {
                    keys.forEach((key, amount) -> {
                        sender.sendMessage(line
                            .replace(ICrateKey.PLACEHOLDER_NAME, key.getName())
                            .replace("%amount%", amount == -2 ? plugin.lang().Command_CheckKey_Format_OfflineItem.getLocalized() : String.valueOf(amount))
                        );
                    });
                    return;
                }
                sender.sendMessage(line);
            });
    }
}
