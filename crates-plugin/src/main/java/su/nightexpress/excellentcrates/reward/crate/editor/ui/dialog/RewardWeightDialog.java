package su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardWeightDialog extends Dialog<RewardWeightDialogContext> {

    private static final String JSON_WEIGHT = "weight";

    private final RewardPlaceholders                rewardPlaceholders;
    private final RewardComponentEditorUIController controller;

    public RewardWeightDialog(RewardPlaceholders rewardPlaceholders,
                              RewardComponentEditorUIController controller) {
        super();
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
                .with(SharedPlaceholders.TOTAL, () -> {
                    double sum = rewardsComponent.getRewards().stream()
                        .mapToDouble(CrateRewardEntry::getWeight).sum();

                    return NumberUtil.format(sum);
                });
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardComponentLang.EDITOR_UI_DIALOG_WEIGHT_TITLE)
                .body(DialogBodies.plain(RewardComponentLang.EDITOR_UI_DIALOG_WEIGHT_BODY)
                    .placeholders(placeholderBuilder.build())
                    .build()
                )
                .inputs(
                    DialogInputs.text(JSON_WEIGHT, RewardComponentLang.EDITOR_UI_DIALOG_WEIGHT_INPUT_WEIGHT)
                        .initial(String.valueOf(context.weight()))
                        .maxLength(6)
                        .build())
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                double weight = nbtHolder.getDouble(JSON_WEIGHT, context.weight());

                if (this.controller.onWeightDialogApply(player, context, weight)) {
                    viewer.callback();
                }
            });
        });
    }
}
