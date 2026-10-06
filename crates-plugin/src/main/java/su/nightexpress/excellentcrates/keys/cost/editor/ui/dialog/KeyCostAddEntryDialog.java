package su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostAddEntryDialogContext;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class KeyCostAddEntryDialog extends Dialog<KeyCostAddEntryDialogContext> {

    private static final String ACTION_KEY = "key";
    private static final String KEY_KEY    = "key";

    private final KeyRegistry               keyRegistry;
    private final KeyPlaceholders           keyPlaceholders;
    private final KeyCostEditorUIController uiController;

    public KeyCostAddEntryDialog(KeyRegistry keyRegistry,
                                 KeyPlaceholders keyPlaceholders,
                                 KeyCostEditorUIController uiController) {
        super();
        this.keyRegistry = keyRegistry;
        this.keyPlaceholders = keyPlaceholders;
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, KeyCostAddEntryDialogContext context) {
        Set<Identifier> existingKeys = context.existingKeys();

        List<WrappedActionButton> buttons = new ArrayList<>();

        this.keyRegistry.values().forEach(key -> {
            if (existingKeys.contains(key.id())) return;

            NightNbtHolder nbt = NightNbtHolder.builder().put(KEY_KEY, key.idString()).build();
            WrappedDialogAction action = DialogActions.customClick(ACTION_KEY, nbt);

            WrappedActionButton button = DialogButtons.action(KeyCostLang.EDITOR_UI_DIALOG_ADD_ENTRY_BUTTON_KEY)
                .action(action)
                .placeholders(PlaceholderContext.builder().apply(this.keyPlaceholders.allPlaceholders(key)).build())
                .build();

            buttons.add(button);
        });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyCostLang.EDITOR_UI_DIALOG_ADD_ENTRY_TITLE)
                .body(DialogBodies.plain(KeyCostLang.EDITOR_UI_DIALOG_ADD_ENTRY_BODY).build())
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons).exitAction(DialogButtons.cancel()).build());

            builder.handleResponse(ACTION_KEY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) {
                    viewer.close();
                    return;
                }

                Identifier keyId = nbtHolder.getText(KEY_KEY).flatMap(IdentifierParser::parse).orElse(null);
                if (keyId == null) return;

                if (this.uiController.onEntryAddDialogSubmit(player, crate, context.hook(), keyId)) {
                    viewer.callback();
                }
            });
        });
    }
}
