package su.nightexpress.excellentcrates.preview.crate.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.preview.PreviewContext;
import su.nightexpress.excellentcrates.preview.DefaultPreviewContext;
import su.nightexpress.excellentcrates.preview.lang.PreviewLang;
import su.nightexpress.excellentcrates.preview.permission.PreviewPerms;
import su.nightexpress.excellentcrates.preview.view.PreviewViewService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CratePreviewCommand implements CrateCommand {

    private static final String ARGUMENT_CRATE  = "crate";
    private static final String ARGUMENT_PLAYER = "player";

    private final PreviewViewService     viewService;
    private final CrateMessageDispatcher dispatcher;

    public CratePreviewCommand(PreviewViewService viewService, CrateMessageDispatcher dispatcher) {
        this.viewService = viewService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("preview", builder -> builder
            .description(PreviewLang.COMMAND_PREVIEW_DESCRIPTION)
            .permission(PreviewPerms.COMMAND_PREVIEW)
            .withArguments(
                Arguments.argument(ARGUMENT_CRATE, Crate.class),
                Arguments.player(ARGUMENT_PLAYER)
                    .optional()
                    .permission(PreviewPerms.COMMAND_PREVIEW_OTHERS)
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        CommandSender sender = context.getSender();

        Crate crate = arguments.get(ARGUMENT_CRATE, Crate.class);
        Player target;
        if (arguments.contains(ARGUMENT_PLAYER)) {
            target = arguments.getPlayer(ARGUMENT_PLAYER);
        }
        else {
            if (!context.isPlayer()) {
                context.printUsage();
                return false;
            }
            target = context.getPlayerOrThrow();
        }

        PreviewContext previewContext = new DefaultPreviewContext(crate, BackwardNavigator.CLOSE_INVENTORY);

        ActionResult result = viewService.openPreview(target, previewContext);
        if (!result.success()) {
            result.handleFeedback((locale, ctx) -> this.dispatcher.sendBase(sender, crate, locale, ctx));
            return false;
        }

        boolean self = sender.equals(target);
        MessageLocale locale = self ? PreviewLang.COMMAND_PREVIEW_SELF : PreviewLang.COMMAND_PREVIEW_OTHERS;
        this.dispatcher.sendBase(sender, crate, locale, ctx -> ctx
            .with(CommonPlaceholders.PLAYER.resolver(target))
        );

        return true;
    }
}
