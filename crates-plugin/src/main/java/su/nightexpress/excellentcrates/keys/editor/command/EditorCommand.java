package su.nightexpress.excellentcrates.keys.editor.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIService;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeyBrowseMenuContext;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public final class EditorCommand implements KeyCommand {

    private final KeyEditorUIService     uiService;
    private final CrateMessageDispatcher dispatcher;

    public EditorCommand(KeyEditorUIService uiService, CrateMessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("editor", builder -> builder
            .description(KeyEditorLang.COMMAND_EDITOR_DESCRIPTION)
            .permission(KeyPerms.COMMAND_EDITOR)
            .playerOnly()
            .executes((context, arguments) -> this.run(context))
        );
    }

    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();

        BackwardNavigator navigator = Player::closeInventory;
        KeyBrowseMenuContext menuContext = new KeyBrowseMenuContext(navigator);
        return this.dispatcher.handleFeedback(player, this.uiService.openKeyListMenu(player, menuContext));
    }
}
