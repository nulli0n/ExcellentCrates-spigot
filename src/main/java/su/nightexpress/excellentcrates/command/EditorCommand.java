package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;

import java.util.Map;

public class EditorCommand extends AbstractCommand<ExcellentCrates> {

    public EditorCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"editor"}, Perms.COMMAND_EDITOR);
    }

    @Override
    public @NotNull String getUsage() {
        return "";
    }

    @Override
    public @NotNull String getDescription() {
        return "";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        this.plugin.getEditor().open((Player) sender, 1);
    }
}
