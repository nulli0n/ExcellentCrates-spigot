package su.nightexpress.excellentcrates.crates.editor.ui;

import java.util.function.Function;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.crates.editor.CrateEditorService;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateCreationDialogContext;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateDeletionDialogContext;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateBrowseMenuContext;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateOptionsMenuContext;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateEditorUIController implements FeedbackHandler {

    private final CrateEditorService   editorService;
    private final CrateEditorUIService uiService;
    private final MessageDispatcher    dispatcher;

    public CrateEditorUIController(CrateEditorService editorService,
                                   CrateEditorUIService uiService,
                                   MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void backToCrateSettings(Player player, Identifier crateId, BackwardNavigator currentNavigator) {
        CrateOptionsMenuContext newContext = new CrateOptionsMenuContext(crateId, currentNavigator);

        this.handleFeedback(player, this.uiService.openCrateOptionsMenu(player, newContext));
    }

    public ActionResult onExtensionModify(Player player, Identifier crateId, Function<Crate, ActionResult> modifier) {
        ActionResult result = this.editorService.modifyByExtension(crateId, modifier);

        this.handleFeedback(player, result);

        return result;
    }

    public boolean onBrowseMenuCrateClick(Player player, Crate crate, CrateBrowseMenuContext currentContext) {
        BackwardNavigator currentNavigator = currentContext.backwardNavigator();
        BackwardNavigator navigator = user -> {
            CrateBrowseMenuContext browseMenuContext = new CrateBrowseMenuContext(currentNavigator);

            this.handleFeedback(user, this.uiService.openCrateBrowseMenu(user, browseMenuContext));
        };

        CrateOptionsMenuContext menuContext = new CrateOptionsMenuContext(crate.id(), navigator);

        return this.handleFeedback(player, this.uiService.openCrateOptionsMenu(player, menuContext));
    }

    public boolean onBrowseMenuCreateClick(Player player, Runnable callback) {
        CrateCreationDialogContext context = new CrateCreationDialogContext();

        return this.handleFeedback(player, this.uiService.showCrateCreationDialog(player, context, callback));
    }

    public void onOptionsMenuDeleteClick(Player player, Identifier crateId, CrateOptionsMenuContext menuContext) {
        BackwardNavigator currentNavigator = menuContext.backwardNavigator();
        CrateDeletionDialogContext context = new CrateDeletionDialogContext(crateId, currentNavigator);

        this.handleFeedback(player, this.uiService.showCrateDeletionDialog(player, context));
    }

    public void onCreationDialogApply(Player player, String idName) {
        Identifier id = IdentifierParser.parseSanitized(idName).orElse(null);
        if (id == null) {
            this.dispatcher.send(player, CrateEditorLang.CREATION_INVALID_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> idName)
            );
            return;
        }

        this.editorService.createCrate(player, id).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(player, locale, ctx);
        });
    }

    public void onDeletionDialogConfirm(Player player, Identifier crateId, BackwardNavigator backwardNavigator) {
        if (this.handleFeedback(player, this.editorService.deleteCrate(crateId))) {
            backwardNavigator.moveBack(player);
        }
    }
}
