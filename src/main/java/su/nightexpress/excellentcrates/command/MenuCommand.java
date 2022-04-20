package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.ICrateMenu;

import java.util.List;

public class MenuCommand extends AbstractCommand<ExcellentCrates> {

    public MenuCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"menu"}, Perms.COMMAND_MENU);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_Menu_Usage.getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_Menu_Desc.getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getMenuManager().getMenuIds();
        }
        if (arg == 2 && player.hasPermission(Perms.COMMAND_KEY_SHOW_OTHERS)) {
            return PlayerUtil.getPlayerNames();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            this.printUsage(sender);
            return;
        }
        if (args.length == 2 && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }
        if (args.length >= 3 && !sender.hasPermission(Perms.COMMAND_MENU_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        ICrateMenu menu = plugin.getMenuManager().getMenuById(args[1]);
        if (menu == null) {
            plugin.lang().Menu_Invalid.send(sender);
            return;
        }

        String pName = args.length >= 3 ? args[2] : sender.getName();
        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        menu.open(player);

        if (!sender.equals(player)) {
            plugin.lang().Command_Menu_Done_Others
                .replace("%player%", player.getName())
                .replace(Placeholders.MENU_ID, menu.getId())
                .send(sender);
        }
    }
}
