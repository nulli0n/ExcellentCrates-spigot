package su.nightexpress.excellentcrates.core;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SharedPlaceholders {

    public static final String LOCATION  = "%location%";
    public static final String WORLD     = "%world%";
    public static final String TOTAL     = "%total%";
    public static final String NAME      = "%name%";
    public static final String LORE      = "%lore%";
    public static final String AMOUNT    = "%amount%";
    public static final String ID        = "%id%";
    public static final String CURRENT   = "%current%";
    public static final String MAX       = "%max%";
    public static final String TYPE      = "%type%";
    public static final String REWARDS   = "%rewards%";
    public static final String AVAILABLE = "%available%";
    public static final String STATE     = "%state%";
    public static final String ITEM      = "%item%";
    public static final String WEIGHT    = "%weight%";
    public static final String X         = "%x%";
    public static final String Y         = "%y%";
    public static final String Z         = "%z%";
    public static final String VALUES    = "%values%";
    public static final String BALANCE   = "%balance%";
    public static final String COST      = "%cost%";

    public static final String CRATE_ID          = "%crate_id%";
    public static final String CRATE_NAME        = "%crate_name%";
    public static final String CRATE_DESCRIPTION = "%crate_description%";

    public static final String CRATE_EFFECTIVE_COOLDOWN          = "%crate_effective_cooldown%";
    public static final String CRATE_INITIAL_GLOBAL_COOLDOWN     = "%crate_initial_global_cooldown%";
    public static final String CRATE_INITIAL_INDIVIDUAL_COOLDOWN = "%crate_initial_individual_cooldown%";

    public static final String CRATE_TOTAL_REWARDS_WEIGHT = "%crate_total_rewards_weight%";

    public static final String REWARD_ID                = "%reward_id%";
    public static final String REWARD_NAME              = "%reward_name%";
    public static final String REWARD_DESCRIPTION       = "%reward_description%";
    public static final String REWARD_WEIGHT            = "%reward_weight%";
    public static final String REWARD_ROLL_CHANCE       = "%reward_roll_chance%";
    public static final String REWARD_RARITY            = "%reward_rarity%";
    public static final String REWARD_HAS_RARITY_MARKER = "%reward_has_rarity_marker%";

    public static final String REWARD_ACTIVE_COOLDOWN              = "%reward_active_cooldown%";
    public static final String REWARD_EXPECTED_COOLDOWN            = "%reward_expected_cooldown%";
    public static final String REWARD_HAS_ACTIVE_COOLDOWN_MARKER   = "%reward_has_active_cooldown_marker%";
    public static final String REWARD_HAS_EXPECTED_COOLDOWN_MARKER = "%reward_has_expected_cooldown_marker%";

    public static final String REWARD_LIMIT_REMAINING  = "%reward_limit_remaining%";
    public static final String REWARD_LIMIT_CAPACITY   = "%reward_limit_capacity%";
    public static final String REWARD_HAS_LIMIT_MARKER = "%reward_has_limit_marker%";

    public static final String REWARD_QUOTA_THRESHOLD = "%reward_quota_threshold%";

    public static final String KEY_ID   = "%key_id%";
    public static final String KEY_NAME = "%key_name%";
    public static final String KEY_LORE = "%key_lore%";

    public static final String KEY_BALANCE = "%key_balance%";

    private SharedPlaceholders() {
    }
}
