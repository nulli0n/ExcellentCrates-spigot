package su.nightexpress.excellentcrates.crates.editor.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIService;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateBrowseMenuContext;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class CrateEditorCommandExtension implements CrateCommand {

    private final CrateEditorUIService uiService;
    private final MessageDispatcher    dispatcher;

    public CrateEditorCommandExtension(CrateEditorUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("editor", builder -> builder
            .permission(CratePerms.COMMAND_EDITOR)
            .description(CratesLang.COMMAND_EDITOR_DESCRIPTION)
            .playerOnly()
            .executes((context, arguments) -> this.run(context))
        );
    }

    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();
        BackwardNavigator navigator = user -> user.closeInventory();
        CrateBrowseMenuContext menuContext = new CrateBrowseMenuContext(navigator);

        return this.uiService.openCrateBrowseMenu(player, menuContext).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(player, locale, ctx);
        });
    }
}
