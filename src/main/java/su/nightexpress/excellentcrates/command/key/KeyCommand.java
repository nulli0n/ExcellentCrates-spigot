package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.base.HelpSubCommand;
import su.nightexpress.nightcore.command.impl.PluginCommand;

public class KeyCommand extends PluginCommand<CratesPlugin> {

    public KeyCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"key"}, Perms.COMMAND_KEY);
        this.setDescription(Lang.COMMAND_KEY_DESC);
        this.setUsage(Lang.COMMAND_KEY_USAGE);

        this.addDefaultCommand(new HelpSubCommand(plugin));
        this.addChildren(new GiveCommand(plugin));
        this.addChildren(new GiveAllCommand(plugin));
        this.addChildren(new TakeCommand(plugin));
        this.addChildren(new SetCommand(plugin));
        this.addChildren(new InspectCommand(plugin));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }
}
