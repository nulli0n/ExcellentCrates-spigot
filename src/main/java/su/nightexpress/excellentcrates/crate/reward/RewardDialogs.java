package su.nightexpress.excellentcrates.crate.reward;

import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.dialog.DialogKey;
import su.nightexpress.excellentcrates.dialog.reward.RewardCreationDialog;
import su.nightexpress.excellentcrates.dialog.reward.RewardItemDialog;
import su.nightexpress.excellentcrates.dialog.reward.RewardPreviewDialog;

public class RewardDialogs {

    public static final DialogKey<RewardCreationDialog.Data> CREATION    = new DialogKey<>("reward_creation");
    public static final DialogKey<Crate>                     SORTING     = new DialogKey<>("reward_sorting");
    public static final DialogKey<RewardPreviewDialog.Data>  PREVIEW     = new DialogKey<>("reward_preview");
    public static final DialogKey<RewardItemDialog.Data>     ITEM        = new DialogKey<>("reward_item");
    public static final DialogKey<CommandReward>             COMMANDS    = new DialogKey<>("reward_commands");
    public static final DialogKey<CommandReward>             NAME        = new DialogKey<>("reward_name");
    public static final DialogKey<CommandReward>             DESCRIPTION = new DialogKey<>("reward_description");
    public static final DialogKey<Reward>                    WEIGHT      = new DialogKey<>("reward_weight");
    public static final DialogKey<Reward>                    PERMISSIONS = new DialogKey<>("reward_permissions");
    public static final DialogKey<Reward>                    LIMITS      = new DialogKey<>("reward_limits");
}
