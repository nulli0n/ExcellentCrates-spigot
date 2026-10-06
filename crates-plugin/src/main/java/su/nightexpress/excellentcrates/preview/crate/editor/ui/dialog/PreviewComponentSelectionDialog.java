package su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.crate.editor.lang.PreviewComponentLang;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIController;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog.context.PreviewComponentSelectionDialogContext;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class PreviewComponentSelectionDialog extends Dialog<PreviewComponentSelectionDialogContext> {

    private static final String ACTION_PREVIEW = "preview";
    private static final String KEY_PREVIEW    = "preview";

    private final PreviewRegistry                    previewRegistry;
    private final PreviewComponentEditorUIController uiController;

    public PreviewComponentSelectionDialog(PreviewRegistry previewRegistry,
                                           PreviewComponentEditorUIController uiController) {
        super();
        this.previewRegistry = previewRegistry;
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, PreviewComponentSelectionDialogContext context) {
        CrateEditorHook hook = context.hook();
        AdaptedKey currentKey = context.currentKey();

        List<WrappedActionButton> buttons = new ArrayList<>();

        this.previewRegistry.getPreviews().stream()
            .sorted(Comparator.comparing(config -> config.key().asString()))
            .forEach(instantiator -> {
                AdaptedKey key = instantiator.key();
                boolean isCurrent = key.equals(currentKey);
                ButtonLocale locale = isCurrent ? PreviewComponentLang.EDITOR_UI_DIALOG_PREVIEW_SELECTION_BUTTON_PREVIEW_SELECTED : PreviewComponentLang.EDITOR_UI_DIALOG_PREVIEW_SELECTION_BUTTON_PREVIEW_UNSELECTED;

                PlaceholderContext placeholders = PlaceholderContext.builder()
                    .with(CommonPlaceholders.GENERIC_NAME, instantiator::getName)
                    .build();

                NightNbtHolder nbtHolder = NightNbtHolder.builder()
                    .put(KEY_PREVIEW, key.asString())
                    .build();

                buttons.add(DialogButtons.action(locale)
                    .action(DialogActions.customClick(ACTION_PREVIEW, nbtHolder))
                    .placeholders(placeholders)
                    .build()
                );
            });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(PreviewComponentLang.EDITOR_UI_DIALOG_PREVIEW_SELECTION_TITLE)
                .body(DialogBodies.plain(PreviewComponentLang.EDITOR_UI_DIALOG_PREVIEW_SELECTION_BODY).build())
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            );

            builder.handleResponse(ACTION_PREVIEW, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) {
                    viewer.close();
                    return;
                }

                Player clicker = viewer.getPlayer();
                String rawPreviewKey = nbtHolder.getText(KEY_PREVIEW, currentKey.asString());
                AdaptedKey selectedKey = BukkitKeys.parse(rawPreviewKey).orElse(currentKey);

                if (this.uiController.onSelectionDialogPreviewClick(clicker, crate, hook, selectedKey)) {
                    viewer.callback();
                }
            });
        });
    }
}
