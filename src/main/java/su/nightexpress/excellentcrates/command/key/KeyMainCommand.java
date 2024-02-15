package su.nightexpress.excellentcrates.command.key;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.command.list.HelpSubCommand;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.config.Lang;

public class KeyMainCommand extends GeneralCommand<ExcellentCratesPlugin> {

    public KeyMainCommand(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, new String[]{"key"}, Perms.COMMAND_KEY);
        this.setDescription(plugin.getMessage(Lang.COMMAND_KEY_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_KEY_USAGE));

        this.addDefaultCommand(new HelpSubCommand<>(plugin));
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
