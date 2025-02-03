package su.nightexpress.excellentcrates.command.basic;

@Deprecated
public class MenuCommand {

//    public MenuCommand(@NotNull CratesPlugin plugin) {
//        super(plugin, new String[]{"menu"}, Perms.COMMAND_MENU);
//        this.setDescription(Lang.COMMAND_MENU_DESC);
//    }
//
//    @Override
//    @NotNull
//    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
//        if (arg == 1) {
//            return plugin.getMenuManager().getMenuIds();
//        }
//        if (arg == 2 && player.hasPermission(Perms.COMMAND_MENU_OTHERS)) {
//            return Players.playerNames(player);
//        }
//        return super.getTab(player, arg, args);
//    }
//
//    @Override
//    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
//        if (result.length() < 1) {
//            this.errorUsage(sender);
//            return;
//        }
//        if (result.length() == 2 && !(sender instanceof Player)) {
//            this.errorSender(sender);
//            return;
//        }
//        if (result.length() >= 3 && !sender.hasPermission(Perms.COMMAND_MENU_OTHERS)) {
//            this.errorPermission(sender);
//            return;
//        }
//
//        CratesMenu menu = plugin.getMenuManager().getMenuById(result.getArg(1, Placeholders.DEFAULT));
//        if (menu == null) {
//            Lang.ERROR_INVALID_MENU.getMessage().send(sender);
//            return;
//        }
//
//        Player player = plugin.getServer().getPlayer(result.getArg(2, sender.getName()));
//        if (player == null) {
//            this.errorPlayer(sender);
//            return;
//        }
//
//        menu.open(player);
//
//        if (sender != player) {
//            Lang.COMMAND_MENU_DONE_OTHERS.getMessage()
//                .replace(Placeholders.forPlayer(player))
//                .send(sender);
//        }
//    }
}
