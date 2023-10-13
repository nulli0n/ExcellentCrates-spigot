package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.config.Lang;

public class EditorCommand extends AbstractCommand<ExcellentCratesPlugin> {

    public EditorCommand(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, new String[]{"editor"}, Perms.COMMAND_EDITOR);
        this.setDescription(plugin.getMessage(Lang.COMMAND_EDITOR_DESC));
        this.setPlayerOnly(true);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        this.plugin.getEditor().open((Player) sender, 1);
    }
}
