package su.nightexpress.excellentcrates.effect.crate.editor.ui.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIController;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.dialog.context.EffectComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.effect.lang.EffectsLang;
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
public class EffectComponentSelectionDialog extends Dialog<EffectComponentSelectionDialogContext> {

    private static final String ACTION_PROFILE = "profile";
    private static final String KEY_PROFILE    = "profile";

    private final EffectRegistry                    effectRegistry;
    private final EffectComponentEditorUIController uiController;

    public EffectComponentSelectionDialog(EffectRegistry effectRegistry,
                                          EffectComponentEditorUIController uiController) {
        super();
        this.effectRegistry = effectRegistry;
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, EffectComponentSelectionDialogContext context) {
        CrateEditorHook hook = context.hook();
        AdaptedKey currentKey = context.currentKey();

        List<WrappedActionButton> buttons = new ArrayList<>();

        this.effectRegistry.getProfiles().stream()
            .sorted(Comparator.comparing(profile -> profile.key().asString()))
            .forEach(profile -> {
                AdaptedKey key = profile.key();
                boolean isCurrent = key.equals(currentKey);
                ButtonLocale locale = isCurrent ? EffectsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_PROFILE_SELECTED : EffectsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_PROFILE_UNSELECTED;

                PlaceholderContext placeholders = PlaceholderContext.builder()
                    .with(CommonPlaceholders.GENERIC_NAME, () -> profile.baseSettings().name())
                    .build();

                NightNbtHolder nbtHolder = NightNbtHolder.builder()
                    .put(KEY_PROFILE, key.asString())
                    .build();

                buttons.add(DialogButtons.action(locale)
                    .action(DialogActions.customClick(ACTION_PROFILE, nbtHolder))
                    .placeholders(placeholders)
                    .build()
                );
            });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(EffectsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_TITLE)
                .body(DialogBodies.plain(EffectsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_BODY).build())
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            );

            builder.handleResponse(ACTION_PROFILE, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) return;

                String rawProfileKey = nbtHolder.getText(KEY_PROFILE, currentKey.asString());
                AdaptedKey selectedKey = BukkitKeys.parse(rawProfileKey).orElse(currentKey);

                if (this.uiController.onSelectionDialogProfileClick(player, crate, hook, selectedKey)) {
                    viewer.callback();
                }
            });
        });
    }
}
