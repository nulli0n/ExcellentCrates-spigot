package su.nightexpress.excellentcrates.crates.pipeline.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.excellentcrates.crates.pipeline.CratePipelineService;
import su.nightexpress.excellentcrates.crates.pipeline.component.DefaultFastOpenPipelineComponent;
import su.nightexpress.excellentcrates.crates.pipeline.component.DefaultFreeOpenComponent;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateOpenCommand implements CrateCommand, FeedbackHandler {

    private static final String ARG_CRATE  = "crate";
    private static final String ARG_PLAYER = "player";

    private static final String FLAG_FAST = "fast";
    private static final String FLAG_FREE = "free";

    private final CratePipelineService   pipelineService;
    private final CrateMessageDispatcher dispatcher;

    public CrateOpenCommand(CratePipelineService pipelineService,
                            CrateMessageDispatcher dispatcher) {
        this.pipelineService = pipelineService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("open", builder -> builder
            .permission(CratePerms.COMMAND_OPEN)
            .description(CratesLang.COMMAND_OPEN_DESCRIPTION)
            .withArguments(
                Arguments.argument(ARG_CRATE, Crate.class),
                Arguments.player(ARG_PLAYER)
                    .permission(CratePerms.COMMAND_OPEN_OTHERS)
                    .optional()
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        CommandSender sender = context.getSender();
        Player target;
        if (arguments.contains(ARG_PLAYER)) {
            target = arguments.getPlayer(ARG_PLAYER);
        }
        else {
            Player executor = context.getPlayer();
            if (executor == null) {
                context.printUsage();
                return false;
            }
            target = executor;
        }

        Crate crate = arguments.get(ARG_CRATE, Crate.class);

        if (target == sender) {
            this.dispatcher.sendBase(sender, crate, CratesLang.PIPELINE_START_SELF);
        }
        else {
            this.dispatcher.sendBase(sender, crate, CratesLang.PIPELINE_START_OTHERS, ctx -> ctx
                .with(CommonPlaceholders.PLAYER_NAME, target::getName)
            );
        }

        this.pipelineService.startPipeline(target, crate, ctx -> {
            if (context.hasFlag(FLAG_FAST)) {
                ctx.putComponent(PipelineComponentKeys.FAST_OPEN, new DefaultFastOpenPipelineComponent());
            }
            if (context.hasFlag(FLAG_FREE)) {
                ctx.putComponent(PipelineComponentKeys.FREE_OPEN, new DefaultFreeOpenComponent());
            }
        });
        return true;
    }
}
