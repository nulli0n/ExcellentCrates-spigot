package su.nightexpress.excellentcrates;

import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderList;

import java.util.stream.Collectors;

public class Placeholders extends su.nightexpress.nightcore.util.Placeholders {

    public static final String WIKI_URL          = "https://nightexpressdev.com/excellentcrates/";
    public static final String WIKI_WEIGHTS      = WIKI_URL + "rewards/rarity-weights/";
    public static final String WIKI_PLACEHOLDERS = WIKI_URL + "placeholders/internal";

    public static final String GENERIC_NAME       = "%name%";
    public static final String GENERIC_AMOUNT     = "%amount%";
    public static final String GENERIC_ID         = "%id%";
    public static final String GENERIC_CURRENT    = "%current%";
    public static final String GENERIC_MAX        = "%max%";
    public static final String GENERIC_TIME       = "%time%";
    public static final String GENERIC_KEYS       = "%keys%";
    public static final String GENERIC_MODE       = "%mode%";
    public static final String GENERIC_TYPE       = "%type%";
    public static final String GENERIC_REWARDS    = "%rewards%";
    public static final String GENERIC_COSTS      = "%costs%";
    public static final String GENERIC_AVAILABLE  = "%available%";
    public static final String GENERIC_STATE      = "%state%";
    public static final String GENERIC_PROBLEMS   = "%problems%";
    public static final String GENERIC_INSPECTION = "%inspection%";
    public static final String GENERIC_COOLDOWN   = "%cooldown%";
    public static final String GENERIC_LIMITS     = "%limits%";

    public static final String RARITY_ID          = "%rarity_id%";
    public static final String RARITY_NAME        = "%rarity_name%";
    public static final String RARITY_WEIGHT      = "%rarity_weight%";
    public static final String RARITY_ROLL_CHANCE = "%rarity_roll_chance%";

    public static final String MILESTONE_OPENINGS       = "%milestone_openings%";
    public static final String MILESTONE_REWARD_ID      = "%milestone_reward_id%";

    public static final String CRATE_ID          = "%crate_id%";
    public static final String CRATE_NAME        = "%crate_name%";
    public static final String CRATE_DESCRIPTION = "%crate_description%";
    public static final String CRATE_LAST_OPENER = "%crate_last_opener%";
    public static final String CRATE_LAST_REWARD = "%crate_last_reward%";
    public static final String CRATE_OPEN_COST = "%crate_open_cost%";

    public static final String KEY_ID   = "%key_id%";
    public static final String KEY_NAME = "%key_name%";

    public static final String REWARD_ID                 = "%reward_id%";
    public static final String REWARD_NAME               = "%reward_name%";
    public static final String REWARD_DESCRIPTION        = "%reward_description%";
    public static final String REWARD_WEIGHT             = "%reward_weight%";
    public static final String REWARD_ROLL_CHANCE        = "%reward_roll_chance%";
    public static final String REWARD_RARITY_NAME        = "%reward_rarity_name%";
    public static final String REWARD_RARITY_WEIGHT      = "%reward_rarity_weight%";
    public static final String REWARD_RARITY_ROLL_CHANCE = "%reward_rarity_roll_chance%";

    public static final String COST_ID   = "%cost_id%";
    public static final String COST_NAME = "%cost_name%";

    public static final PlaceholderList<Crate> CRATE = PlaceholderList.create(list -> list
        .add(CRATE_ID, Crate::getId)
        .add(CRATE_NAME, Crate::getName)
        .add(CRATE_DESCRIPTION, crate -> String.join("\n", crate.getDescription()))
        .add(CRATE_LAST_OPENER, Crate::getLastOpenerName)
        .add(CRATE_LAST_REWARD, Crate::getLastRewardName)
        .add(CRATE_OPEN_COST, crate -> crate.getCosts().stream().map(Cost::getName).collect(Collectors.joining(", ")))
    );

    public static final PlaceholderList<Reward> REWARD = PlaceholderList.create(list -> list
        .add(REWARD_ID, Reward::getId)
        .add(REWARD_NAME, Reward::getName)
        .add(REWARD_DESCRIPTION, reward -> String.join("\n", reward.getDescription()))
        .add(REWARD_WEIGHT, reward -> NumberUtil.format(reward.getWeight()))
        .add(REWARD_ROLL_CHANCE, reward -> NumberUtil.format(reward.getRollChance()))
        .add(REWARD_RARITY_NAME, reward -> reward.getRarity().getName())
        .add(REWARD_RARITY_WEIGHT, reward -> NumberUtil.format(reward.getRarity().getWeight()))
        .add(REWARD_RARITY_ROLL_CHANCE, reward -> NumberUtil.format(reward.getRarity().getRollChance(reward.getCrate())))
    );

    public static final PlaceholderList<Milestone> MILESTONE = PlaceholderList.create(list -> list
        .add(MILESTONE_OPENINGS, milestone -> NumberUtil.format(milestone.getOpenings()))
        .add(MILESTONE_REWARD_ID, Milestone::getRewardId)
    );

    public static final PlaceholderList<Rarity> RARITY = PlaceholderList.create(list -> list
        .add(RARITY_ID, Rarity::getId)
        .add(RARITY_NAME, Rarity::getName)
        .add(RARITY_WEIGHT, rarity -> NumberUtil.format(rarity.getWeight()))
        .add(RARITY_ROLL_CHANCE, rarity -> NumberUtil.format(rarity.getRollChance()))
    );

    public static final PlaceholderList<CrateKey> KEY = PlaceholderList.create(list -> list
        .add(KEY_ID, CrateKey::getId)
        .add(KEY_NAME, CrateKey::getName)
    );

    public static final PlaceholderList<Cost> COST = PlaceholderList.create(list -> list
        .add(COST_ID, Cost::getId)
        .add(COST_NAME, Cost::getName)
    );
}
