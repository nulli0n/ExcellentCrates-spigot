package su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIController;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog.context.RarityComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.rarity.reward.component.lang.RarityComponentLang;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
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
public class RarityComponentSelectionDialog extends Dialog<RarityComponentSelectionDialogContext> {

    private static final String KEY_RARITY    = "rarity";
    private static final String ACTION_RARITY = "rarity_action";

    private final RarityRegistry                    rarityRegistry;
    private final RarityComponentEditorUIController uiController;

    public RarityComponentSelectionDialog(RarityRegistry rarityRegistry,
                                          RarityComponentEditorUIController uiController) {
        super();
        this.rarityRegistry = rarityRegistry;
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, RarityComponentSelectionDialogContext context) {
        RewardEditorHook hook = context.hook();
        Identifier currentId = context.currentId();

        List<WrappedActionButton> buttons = new ArrayList<>();

        this.rarityRegistry.values().stream()
            .sorted(Comparator.comparing(Rarity::idString))
            .forEach(rarity -> {
                Identifier id = rarity.id();
                boolean isCurrent = id.equals(currentId);
                ButtonLocale locale = isCurrent ? RarityComponentLang.EDITOR_UI_DIALOG_RARITY_SELECTION_BUTTON_RARITY_SELECTED : RarityComponentLang.EDITOR_UI_DIALOG_RARITY_SELECTION_BUTTON_RARITY_UNSELECTED;

                PlaceholderContext placeholders = PlaceholderContext.builder()
                    .with(CommonPlaceholders.GENERIC_NAME, rarity::getName)
                    .build();

                NightNbtHolder nbtHolder = NightNbtHolder.builder()
                    .put(KEY_RARITY, id.value())
                    .build();

                buttons.add(DialogButtons.action(locale)
                    .action(DialogActions.customClick(ACTION_RARITY, nbtHolder))
                    .placeholders(placeholders)
                    .build()
                );
            });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RarityComponentLang.EDITOR_UI_DIALOG_RARITY_SELECTION_TITLE)
                .body(DialogBodies.plain(RarityComponentLang.EDITOR_UI_DIALOG_RARITY_SELECTION_BODY).build())
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            );

            builder.handleResponse(ACTION_RARITY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Reward reward = context.rewardRef().get();
                if (reward == null) return;

                String rawRarityId = nbtHolder.getText(KEY_RARITY, currentId.value());
                Identifier selectedId = IdentifierParser.parse(rawRarityId).orElse(currentId);

                if (this.uiController.onSelectionDialogRarityClick(player, reward, hook, selectedId)) {
                    viewer.callback();
                }
            });
        });
    }
}
