package su.nightexpress.excellentcrates.reward.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardWeightDialog extends Dialog<RewardWeightDialogContext> {

    private static final String KEY_WEIGHT = "weight";

    private final CratePlaceholders        cratePlaceholders;
    private final RewardPlaceholders       rewardPlaceholders;
    private final RewardEditorUIController controller;

    public RewardWeightDialog(CratePlaceholders cratePlaceholders,
                              RewardPlaceholders rewardPlaceholders,
                              RewardEditorUIController controller) {
        super();
        this.cratePlaceholders = cratePlaceholders;
        this.rewardPlaceholders = rewardPlaceholders;
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardWeightDialogContext context) {
        Crate crate = context.crateRef().get();
        Reward reward = context.rewardRef().get();
        CrateRewardsComponent rewardsComponent = crate != null ? crate.getComponentOrNull(
            CrateComponentKeys.REWARDS) : null;

        PlaceholderContext.Builder placeholderBuilder = PlaceholderContext.builder();

        if (crate != null && reward != null && rewardsComponent != null) {
            placeholderBuilder
                .apply(this.rewardPlaceholders.allPlaceholders(crate, reward))
                .apply(this.cratePlaceholders.basePlaceholders(crate));
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardEditorLang.UI_DIALOG_WEIGHT_TITLE)
                .body(DialogBodies.plain(RewardEditorLang.UI_DIALOG_WEIGHT_BODY)
                    .placeholders(placeholderBuilder.build())
                    .build()
                )
                .inputs(
                    DialogInputs.text(KEY_WEIGHT, RewardEditorLang.UI_DIALOG_WEIGHT_INPUT_WEIGHT)
                        .initial(String.valueOf(context.weight()))
                        .maxLength(6)
                        .build())
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                double weight = nbtHolder.getDouble(KEY_WEIGHT, context.weight());

                if (this.controller.onWeightDialogApply(player, context, weight)) {
                    viewer.callback();
                }
            });
        });
    }
}
