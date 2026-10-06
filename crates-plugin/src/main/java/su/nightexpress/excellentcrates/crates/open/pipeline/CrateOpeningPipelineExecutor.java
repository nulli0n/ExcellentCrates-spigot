package su.nightexpress.excellentcrates.crates.open.pipeline;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.open.OpenActionsComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class CrateOpeningPipelineExecutor implements PipelineExecutor {

    private final CratePlaceholders cratePlaceholders;

    public CrateOpeningPipelineExecutor(CratePlaceholders cratePlaceholders) {
        this.cratePlaceholders = cratePlaceholders;
    }

    @Override
    public void execute(Player player, Crate crate, PipelineContext context) {
        OpenActionsComponent component = crate.getComponentOrNull(CrateComponentKeys.OPEN_ACTIONS);
        if (component == null || !component.isEnabled()) return;

        List<String> commands = component.getCommands();
        if (commands.isEmpty()) return;

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .apply(this.cratePlaceholders.allPlaceholders(crate, player))
            .andThen(CommonPlaceholders.forPlaceholderAPI(player))
            .build();

        CommandSender console = Bukkit.getConsoleSender();

        for (String command : commands) {
            String parsedCommand = placeholders.apply(command);
            Bukkit.dispatchCommand(console, parsedCommand);
        }
    }
}
