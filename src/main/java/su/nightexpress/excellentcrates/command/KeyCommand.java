package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.lang.LangMessage;
import su.nexmedia.engine.command.list.HelpSubCommand;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.MessageUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.*;

public class KeyCommand extends GeneralCommand<ExcellentCrates> {

    public KeyCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"key"}, Perms.COMMAND_KEY);
        this.addDefaultCommand(new HelpSubCommand<>(this.plugin));
        this.addChildren(new GiveCommand(this.plugin));
        this.addChildren(new TakeCommand(this.plugin));
        this.addChildren(new SetCommand(this.plugin));
        this.addChildren(new ShowCommand(this.plugin));
    }

    @Override
    @NotNull
    public String getUsage() {
        return "";
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_KEY_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {

    }

    private enum Mode {
        GIVE, TAKE, SET
    }

    private abstract static class ManageCommand extends AbstractCommand<ExcellentCrates> {

        protected final Mode mode;

        public ManageCommand(@NotNull ExcellentCrates plugin, @NotNull String[] aliases, @Nullable Permission permission, @NotNull Mode mode) {
            super(plugin, aliases, permission);
            this.mode = mode;
        }

        //@NotNull public abstract LangMessage getMessageError();

        @NotNull public abstract LangMessage getMessageDone();

        @NotNull public abstract LangMessage getMessageNotify();

        @Override
        public boolean isPlayerOnly() {
            return false;
        }

        @Override
        @NotNull
        public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
            if (arg == 2) {
                List<String> list = new ArrayList<>(CollectionsUtil.playerNames(player));
                list.add(0, Placeholders.WILDCARD);
                return list;
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
        protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
            if (args.length < 5) {
                this.printUsage(sender);
                return;
            }

            CrateKey crateKey = plugin.getKeyManager().getKeyById(args[3]);
            if (crateKey == null) {
                plugin.getMessage(Lang.CRATE_KEY_ERROR_INVALID).send(sender);
                return;
            }

            int amount = StringUtil.getInteger(args[4], -1, false);
            if ((amount == 0 && this.mode != Mode.SET) || amount < 0) {
                this.errorNumber(sender, args[4]);
                return;
            }

            String pName = args[2];
            Set<String> playerNames = new HashSet<>();
            if (pName.equalsIgnoreCase(Placeholders.WILDCARD)) {
                playerNames.addAll(CollectionsUtil.playerNames());
            }
            else playerNames.add(pName);

            playerNames.forEach(name -> {
                boolean isDone = switch (this.mode) {
                    case SET -> plugin.getKeyManager().setKey(name, crateKey, amount);
                    case GIVE -> plugin.getKeyManager().giveKey(name, crateKey, amount);
                    case TAKE -> plugin.getKeyManager().takeKey(name, crateKey, amount);
                };
                if (!isDone) {
                    this.plugin.getMessage(Lang.COMMAND_KEY_ERROR_PLAYER).replace(Placeholders.Player.NAME, name).send(sender);
                    return;
                }

                Player target = plugin.getServer().getPlayer(name);
                if (target != null) {
                    this.getMessageNotify()
                        .replace(Placeholders.GENERIC_AMOUNT, amount)
                        .replace(crateKey.replacePlaceholders())
                        .send(target);
                }

                this.getMessageDone()
                    .replace(Placeholders.Player.NAME, name)
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(crateKey.replacePlaceholders())
                    .send(sender);
            });
        }
    }

    private static class GiveCommand extends ManageCommand {

        public GiveCommand(@NotNull ExcellentCrates plugin) {
            super(plugin, new String[]{"give"}, Perms.COMMAND_KEY_GIVE, Mode.GIVE);
        }

        /*@Override
        @NotNull
        public LangMessage getMessageError() {
            return plugin.lang().Error_Player_Invalid;
        }*/

        @Override
        @NotNull
        public LangMessage getMessageDone() {
            return plugin.getMessage(Lang.COMMAND_KEY_GIVE_DONE);
        }

        @Override
        @NotNull
        public LangMessage getMessageNotify() {
            return plugin.getMessage(Lang.COMMAND_KEY_GIVE_NOTIFY);
        }

        @Override
        @NotNull
        public String getUsage() {
            return plugin.getMessage(Lang.COMMAND_KEY_GIVE_USAGE).getLocalized();
        }

        @Override
        @NotNull
        public String getDescription() {
            return plugin.getMessage(Lang.COMMAND_KEY_GIVE_DESC).getLocalized();
        }
    }

    private static class TakeCommand extends ManageCommand {

        public TakeCommand(@NotNull ExcellentCrates plugin) {
            super(plugin, new String[]{"take"}, Perms.COMMAND_KEY_TAKE, Mode.TAKE);
        }

        /*@Override
        @NotNull
        public LangMessage getMessageError() {
            return plugin.lang().Command_Key_Take_Error;
        }*/

        @Override
        @NotNull
        public LangMessage getMessageDone() {
            return plugin.getMessage(Lang.COMMAND_KEY_TAKE_DONE);
        }

        @Override
        @NotNull
        public LangMessage getMessageNotify() {
            return plugin.getMessage(Lang.COMMAND_KEY_TAKE_NOTIFY);
        }

        @Override
        @NotNull
        public String getUsage() {
            return plugin.getMessage(Lang.COMMAND_KEY_TAKE_USAGE).getLocalized();
        }

        @Override
        @NotNull
        public String getDescription() {
            return plugin.getMessage(Lang.COMMAND_KEY_TAKE_DESC).getLocalized();
        }
    }

    private static class SetCommand extends ManageCommand {

        public SetCommand(@NotNull ExcellentCrates plugin) {
            super(plugin, new String[]{"set"}, Perms.COMMAND_KEY_SET, Mode.SET);
        }

        /*@Override
        @NotNull
        public LangMessage getMessageError() {
            return plugin.lang().Error_Player_Invalid;
        }*/

        @Override
        @NotNull
        public LangMessage getMessageDone() {
            return plugin.getMessage(Lang.COMMAND_KEY_SET_DONE);
        }

        @Override
        @NotNull
        public LangMessage getMessageNotify() {
            return plugin.getMessage(Lang.COMMAND_KEY_SET_NOTIFY);
        }

        @Override
        @NotNull
        public String getUsage() {
            return plugin.getMessage(Lang.COMMAND_KEY_SET_USAGE).getLocalized();
        }

        @Override
        @NotNull
        public String getDescription() {
            return plugin.getMessage(Lang.COMMAND_KEY_SET_DESC).getLocalized();
        }
    }

    private static class ShowCommand extends AbstractCommand<ExcellentCrates> {

        public ShowCommand(@NotNull ExcellentCrates plugin) {
            super(plugin, new String[]{"show"}, Perms.COMMAND_KEY_SHOW);
        }

        @Override
        @NotNull
        public String getUsage() {
            return plugin.getMessage(Lang.COMMAND_KEY_SHOW_USAGE).getLocalized();
        }

        @Override
        @NotNull
        public String getDescription() {
            return plugin.getMessage(Lang.COMMAND_KEY_SHOW_DESC).getLocalized();
        }

        @Override
        public boolean isPlayerOnly() {
            return false;
        }

        @Override
        @NotNull
        public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
            if (arg == 2 && player.hasPermission(Perms.COMMAND_KEY_SHOW_OTHERS)) {
                return CollectionsUtil.playerNames(player);
            }
            return super.getTab(player, arg, args);
        }

        @Override
        public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
            if (args.length >= 3 && !sender.hasPermission(Perms.COMMAND_KEY_SHOW_OTHERS)) {
                this.errorPermission(sender);
                return;
            }

            String pName = args.length >= 3 ? args[2] : sender.getName();
            CrateUser user = plugin.getUserManager().getUserData(pName);
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            Player player = user.getPlayer();
            Map<CrateKey, Integer> keys = new HashMap<>();
            for (CrateKey key : plugin.getKeyManager().getKeys()) {
                int has;
                if (!key.isVirtual()) {
                    has = player != null ? plugin.getKeyManager().getKeysAmount(player, key) : -2;
                }
                else {
                    has = user.getKeys(key.getId());
                }
                keys.put(key, has);
            }

            plugin.getMessage(Lang.COMMAND_KEY_SHOW_FORMAT_LIST).replace(Placeholders.Player.NAME, user.getName()).asList().forEach(line -> {
                if (line.contains(Placeholders.KEY_NAME)) {
                    keys.forEach((key, amount) -> {
                        MessageUtil.sendCustom(sender, line
                            .replace(Placeholders.KEY_NAME, key.getName())
                            .replace(Placeholders.GENERIC_AMOUNT, amount == -2 ? "?" : String.valueOf(amount))
                        );
                    });
                    return;
                }
                MessageUtil.sendCustom(sender, line);
            });
        }
    }
}
