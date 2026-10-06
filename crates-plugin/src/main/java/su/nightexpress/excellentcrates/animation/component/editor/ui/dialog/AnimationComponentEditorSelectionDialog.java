package su.nightexpress.excellentcrates.animation.component.editor.ui.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIController;
import su.nightexpress.excellentcrates.animation.component.editor.ui.dialog.context.AnimationComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
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
public class AnimationComponentEditorSelectionDialog extends Dialog<AnimationComponentSelectionDialogContext> {

    private static final String ACTION_ANIMATION = "animation";
    private static final String KEY_STYLE        = "style";

    private final AnimationRegistry                    animations;
    private final AnimationComponentEditorUIController uiController;

    public AnimationComponentEditorSelectionDialog(AnimationRegistry animations,
                                                   AnimationComponentEditorUIController uiController) {
        super();
        this.animations = animations;
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, AnimationComponentSelectionDialogContext context) {
        CrateEditorHook hook = context.hook();
        AdaptedKey currentKey = context.currentKey();

        List<WrappedActionButton> buttons = new ArrayList<>();

        this.animations.getProfiles().stream()
            .sorted(Comparator.comparing(config -> config.key().asString()))
            .forEach(instantiator -> {
                AdaptedKey key = instantiator.key();
                boolean isCurrent = key.equals(currentKey);
                ButtonLocale locale = isCurrent ? AnimationsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_SELECTED : AnimationsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_UNSELECTED;

                PlaceholderContext placeholders = PlaceholderContext.builder()
                    .with(CommonPlaceholders.GENERIC_NAME, instantiator::getName)
                    .build();

                NightNbtHolder nbtHolder = NightNbtHolder.builder()
                    .put(KEY_STYLE, key.asString())
                    .build();

                buttons.add(DialogButtons.action(locale)
                    .action(DialogActions.customClick(ACTION_ANIMATION, nbtHolder))
                    .placeholders(placeholders)
                    .build()
                );
            });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(AnimationsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_TITLE)
                .body(DialogBodies.plain(AnimationsLang.EDITOR_UI_DIALOG_PROFILE_SELECTION_BODY).build())
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            );

            builder.handleResponse(ACTION_ANIMATION, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) {
                    viewer.close();
                    return;
                }

                String rawAnimationKey = nbtHolder.getText(KEY_STYLE, currentKey.asString());
                AdaptedKey selectedKey = BukkitKeys.parse(rawAnimationKey).orElse(currentKey);

                if (this.uiController.onSelectionDialogAnimationClick(player, crate, hook, selectedKey)) {
                    viewer.callback();
                }
            });
        });
    }
}
