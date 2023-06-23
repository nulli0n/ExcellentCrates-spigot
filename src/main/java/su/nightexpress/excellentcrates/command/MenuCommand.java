package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.menu.impl.MenuConfig;

import java.util.List;

public class MenuCommand extends AbstractCommand<ExcellentCrates> {

    public MenuCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"menu"}, Perms.COMMAND_MENU);
        this.setDescription(plugin.getMessage(Lang.COMMAND_MENU_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_MENU_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getMenuManager().getMenuIds();
        }
        if (arg == 2 && player.hasPermission(Perms.COMMAND_MENU_OTHERS)) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 1) {
            this.printUsage(sender);
            return;
        }
        if (result.length() == 2 && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }
        if (result.length() >= 3 && !sender.hasPermission(Perms.COMMAND_MENU_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        MenuConfig menu = plugin.getMenuManager().getMenuById(result.getArg(1, Placeholders.DEFAULT));
        if (menu == null) {
            plugin.getMessage(Lang.MENU_INVALID).send(sender);
            return;
        }

        String pName = result.getArg(2, sender.getName());
        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        menu.open(player);

        if (sender != player) {
            plugin.getMessage(Lang.COMMAND_MENU_DONE_OTHERS)
                .replace(Placeholders.Player.replacer(player))
                .replace(Placeholders.MENU_ID, menu.getId())
                .send(sender);
        }
    }
}
