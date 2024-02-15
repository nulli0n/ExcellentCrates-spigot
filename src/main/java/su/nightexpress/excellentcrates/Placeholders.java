package su.nightexpress.excellentcrates;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateInspector;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.crate.impl.RewardInspector;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.util.Inspector;

import java.util.stream.Collectors;

public class Placeholders extends su.nexmedia.engine.utils.Placeholders {

    public static final String WIKI              = "https://github.com/nulli0n/ExcellentCrates-spigot/wiki/";
    public static final String WIKI_PLACEHOLDERS = WIKI + "Internal-Placeholders";

    public static final String CHECK_MARK = Colors2.GREEN + "✔";
    public static final String WARN_MARK = Colors2.ORANGE + "⚠";
    public static final String CROSS_MARK = Colors2.RED + "✘";

    public static final String GENERIC_NAME = "%name%";
    public static final String GENERIC_AMOUNT = "%amount%";
    public static final String GENERIC_TIME   = "%time%";

    public static final String CURRENCY_NAME = "%currency_name%";
    public static final String CURRENCY_ID   = "%currency_id%";

    public static final String RARITY_ID     = "%rarity_id%";
    public static final String RARITY_NAME   = "%rarity_name%";
    public static final String RARITY_CHANCE = "%rarity_chance%";

    public static final String MILESTONE_OPENINGS = "%milestone_openings%";
    public static final String MILESTONE_REWARD_ID = "%milestone_reward_id%";

    public static final String CRATE_ID            = "%crate_id%";
    public static final String CRATE_NAME          = "%crate_name%";
    public static final String CRATE_PERMISSION    = "%crate_permission%";
    public static final String CRATE_OPEN_COOLDOWN = "%crate_open_cooldown%";
    public static final String CRATE_OPEN_COST     = "%crate_open_cost%";
    public static final String CRATE_LAST_OPENER   = "%crate_last_opener%";
    public static final String CRATE_LAST_REWARD   = "%crate_last_reward%";

    public static final String CRATE_ANIMATION_CONFIG      = "%crate_animation_config%";
    public static final String CRATE_PREVIEW_CONFIG        = "%crate_preview_config%";
    public static final String CRATE_PERMISSION_REQUIRED   = "%crate_permission_required%";
    public static final String CRATE_KEY_IDS               = "%crate_key_ids%";
    public static final String CRATE_PUSHBACK_ENABLED      = "%crate_pushback_enabled%";
    public static final String CRATE_HOLOGRAM_ENABLED      = "%crate_hologram_enabled%";
    public static final String CRATE_HOLOGRAM_TEMPLATE     = "%crate_hologram_template%";
    public static final String CRATE_LOCATIONS             = "%crate_locations%";
    public static final String CRATE_EFFECT_MODEL          = "%crate_effect_model%";
    public static final String CRATE_EFFECT_PARTICLE_NAME  = "%crate_effect_particle_name%";
    public static final String CRATE_EFFECT_PARTICLE_DATA  = "%crate_effect_particle_data%";
    public static final String CRATE_REWARDS_AMOUNT        = "%crate_rewards_amount%";
    public static final String CRATE_MILESTONES_AMOUNT     = "%crate_milestones_amount%";
    public static final String CRATE_MILESTONES_REPEATABLE = "%crate_milestones_repeatable%";

    public static final String CRATE_INSPECT_PREVIEW  = "%crate_inspect_preview%";
    public static final String CRATE_INSPECT_OPENING  = "%crate_inspect_opening%";
    public static final String CRATE_INSPECT_REWARDS  = "%crate_inspect_rewards%";
    public static final String CRATE_INSPECT_KEYS     = "%crate_inspect_keys%";
    public static final String CRATE_INSPECT_HOLOGRAM = "%crate_inspect_hologram%";

    public static final String KEY_ID        = "%key_id%";
    public static final String KEY_NAME      = "%key_name%";
    public static final String KEY_VIRTUAL   = "%key_virtual%";
    public static final String KEY_ITEM_NAME = "%key_item_name%";

    public static final String MENU_ID = "%menu_id%";

    public static final String REWARD_ID            = "%reward_id%";
    public static final String REWARD_NAME          = "%reward_name%";
    public static final String REWARD_WEIGHT        = "%reward_chance%";
    public static final String REWARD_REAL_CHANCE   = "%reward_real_chance%";
    public static final String REWARD_RARITY_NAME   = "%reward_rarity_name%";
    public static final String REWARD_RARITY_CHANCE = "%reward_rarity_chance%";
    public static final String REWARD_PREVIEW_NAME  = "%reward_preview_name%";
    public static final String REWARD_PREVIEW_LORE  = "%reward_preview_lore%";

    public static final String REWARD_BROADCAST               = "%reward_broadcast%";
    public static final String REWARD_WIN_LIMIT_AMOUNT        = "%reward_win_limit_amount%";
    public static final String REWARD_WIN_LIMIT_COOLDOWN      = "%reward_win_limit_cooldown%";
    public static final String REWARD_IGNORED_FOR_PERMISSIONS = "%reward_ignored_for_permissions%";
    public static final String REWARD_EDITOR_COMMANDS         = "%reward_editor_commands%";
    public static final String REWARD_EDITOR_ITEMS            = "%reward_editor_items%";
    public static final String REWARD_INSPECT_CONTENT         = "%reward_inspect_content%";


    @NotNull
    public static PlaceholderMap forCrateAll(@NotNull Crate crate) {
        return PlaceholderMap.fusion(crate.getPlaceholders(), crate.getInspector().getPlaceholders(), forCrateEditor(crate));
    }

    @NotNull
    public static PlaceholderMap forCrate(@NotNull Crate crate) {
        return new PlaceholderMap()
            .add(Placeholders.CRATE_ID, crate.getId())
            .add(Placeholders.CRATE_NAME, crate::getName)
            .add(Placeholders.CRATE_PERMISSION, crate::getPermission)
            .add(Placeholders.CRATE_OPEN_COST, () -> {
                return crate.getOpenCostMap().entrySet().stream().map(e -> e.getKey().format(e.getValue()))
                    .collect(Collectors.joining(", "));
            })
            .add(Placeholders.CRATE_OPEN_COOLDOWN, () -> {
                if (crate.getOpenCooldown() == 0L) return "-";
                if (crate.getOpenCooldown() < 0L) return LangManager.getPlain(Lang.OTHER_ONE_TIMED);

                return TimeUtil.formatTime(crate.getOpenCooldown() * 1000L);
            })
            .add(CRATE_LAST_OPENER, () -> {
                String last = crate.getLastOpener();
                return last == null ? "-" : last;
            })
            .add(CRATE_LAST_REWARD, () -> {
                String last = crate.getLastReward();
                return last == null ? "-" : last;
            })
            ;
    }

    @NotNull
    public static PlaceholderMap forCrateInspector(@NotNull CrateInspector inspector) {
        return new PlaceholderMap()
            .add(CRATE_INSPECT_KEYS, () -> {
                if (!inspector.getCrate().isKeyRequired()) return Inspector.warning("No key requirements.");
                if (!inspector.hasValidKeys()) return Inspector.problem("All keys are invalid!");
                if (inspector.hasInvalidKeys()) return Inspector.warning("Some keys are invalid!");
                return Inspector.good("All keys are valid!");
            })
            .add(CRATE_INSPECT_REWARDS, () -> {
                if (!inspector.hasRewards()) return Inspector.problem("No rewards added!");
                return Inspector.good("Rewards are present.");
            })
            .add(CRATE_INSPECT_OPENING, () -> {
                if (!inspector.hasValidOpening()) return Inspector.warning("Invalid opening config!");
                return Inspector.good("Opening config present.");
            })
            .add(CRATE_INSPECT_PREVIEW, () -> {
                if (!inspector.hasValidPreview()) return Inspector.warning("Invalid preview config!");
                return Inspector.good("Preview config present.");
            })
            .add(CRATE_INSPECT_HOLOGRAM, () -> {
                if (inspector.getCrate().isHologramEnabled() && !inspector.hasValidHologram()) return Inspector.problem("Invalid hologram template!");
                return Inspector.good("Hologram template present.");
            })
            ;
    }

    @NotNull
    public static PlaceholderMap forCrateEditor(@NotNull Crate crate) {
        return new PlaceholderMap()
            .add(Placeholders.CRATE_ANIMATION_CONFIG, () -> String.valueOf(crate.getOpeningConfig()))
            .add(Placeholders.CRATE_PREVIEW_CONFIG, () -> String.valueOf(crate.getPreviewConfig()))
            .add(Placeholders.CRATE_PERMISSION_REQUIRED, () -> LangManager.getBoolean(crate.isPermissionRequired()))
            .add(Placeholders.CRATE_KEY_IDS, () -> String.join("\n", crate.getInspector().formatKeyList()))
            .add(Placeholders.CRATE_PUSHBACK_ENABLED, () -> LangManager.getBoolean(crate.isPushbackEnabled()))
            .add(Placeholders.CRATE_HOLOGRAM_ENABLED, () -> LangManager.getBoolean(crate.isHologramEnabled()))
            .add(Placeholders.CRATE_HOLOGRAM_TEMPLATE, crate::getHologramTemplate)
            .add(Placeholders.CRATE_LOCATIONS, () -> String.join("\n", crate.getInspector().formatBlockList()))
            .add(Placeholders.CRATE_EFFECT_MODEL, () -> StringUtil.capitalizeUnderscored(crate.getEffectModel().name().toLowerCase()))
            .add(Placeholders.CRATE_EFFECT_PARTICLE_NAME, () -> StringUtil.capitalizeUnderscored(crate.getEffectParticle().getParticle().name().toLowerCase()))
            .add(Placeholders.CRATE_EFFECT_PARTICLE_DATA, () -> {
                Class<?> dataType = crate.getEffectParticle().getParticle().getDataType();
                if (dataType == Void.class) return LangManager.getPlain(Lang.OTHER_NONE);

                Object data = crate.getEffectParticle().getData();
                if (data instanceof BlockData blockData) {
                    return LangManager.getMaterial(blockData.getMaterial());
                }
                else if (data instanceof ItemStack itemStack) {
                    return LangManager.getMaterial(itemStack.getType());
                }
                else if (data instanceof Particle.DustOptions dustOptions) {
                    Color color = dustOptions.getColor();
                    return "RGB: " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ", Size: " + dustOptions.getSize();
                }
                return "?";
            })
            .add(Placeholders.CRATE_REWARDS_AMOUNT, () -> NumberUtil.format(crate.getRewards().size()))
            .add(Placeholders.CRATE_MILESTONES_AMOUNT, () -> NumberUtil.format(crate.getMilestones().size()))
            .add(Placeholders.CRATE_MILESTONES_REPEATABLE, () -> LangManager.getBoolean(crate.isMilestonesRepeatable()))
            ;
    }

    @NotNull
    public static PlaceholderMap forRewardAll(@NotNull Reward reward) {
        return PlaceholderMap.fusion(reward.getPlaceholders(), reward.getInspector().getPlaceholders(), forRewardEditor(reward));
    }

    @NotNull
    public static PlaceholderMap forReward(@NotNull Reward reward) {
        PlaceholderMap placeholderMap = new PlaceholderMap()
            .add(Placeholders.REWARD_ID, reward::getId)
            .add(Placeholders.REWARD_NAME, reward::getName)
            .add(Placeholders.REWARD_WEIGHT, () -> NumberUtil.format(reward.getWeight()))
            .add(Placeholders.REWARD_REAL_CHANCE, () -> NumberUtil.format(reward.getRealChance()))
            .add(Placeholders.REWARD_RARITY_NAME, () -> reward.getRarity().getName())
            .add(Placeholders.REWARD_RARITY_CHANCE, () -> NumberUtil.format(reward.getRarity().getChance()))
            .add(Placeholders.REWARD_PREVIEW_NAME, () -> ItemUtil.getItemName(reward.getPreview()))
            .add(Placeholders.REWARD_PREVIEW_LORE, () -> String.join("\n", ItemUtil.getLore(reward.getPreview())))
            ;
        placeholderMap.add(reward.getRarity().getPlaceholders());

        return placeholderMap;
    }

    public static PlaceholderMap forRewardEditor(@NotNull Reward reward) {
        return new PlaceholderMap()
            .add(Placeholders.REWARD_BROADCAST, () -> LangManager.getBoolean(reward.isBroadcast()))
            .add(Placeholders.REWARD_WIN_LIMIT_AMOUNT, () -> {
                if (!reward.isWinLimitedAmount()) return LangManager.getPlain(Lang.OTHER_INFINITY);
                return String.valueOf(reward.getWinLimitAmount());
            })
            .add(Placeholders.REWARD_WIN_LIMIT_COOLDOWN, () -> {
                if (!reward.isWinLimitedCooldown()) return LangManager.getPlain(Lang.OTHER_NO);
                return reward.getWinLimitCooldown() > 0 ? TimeUtil.formatTime(reward.getWinLimitCooldown() * 1000L) : LangManager.getPlain(Lang.OTHER_ONE_TIMED);
            })
            .add(Placeholders.REWARD_IGNORED_FOR_PERMISSIONS, () -> String.join("\n", reward.getIgnoredForPermissions()))
            .add(REWARD_EDITOR_ITEMS, () -> String.join("\n", reward.getInspector().formatItemList()))
            .add(REWARD_EDITOR_COMMANDS, () -> String.join("\n", reward.getInspector().formatCommandList()))
            ;
    }

    @NotNull
    public static PlaceholderMap forRewardInspector(@NotNull RewardInspector inspector) {
        return new PlaceholderMap()
            .add(REWARD_INSPECT_CONTENT, () -> {
                if (!inspector.hasContent()) return Inspector.problem("Reward gives nothing!");
                return Inspector.good("Reward content is present.");
            })
            ;
    }

    @NotNull
    public static PlaceholderMap forKey(@NotNull CrateKey key) {
        return new PlaceholderMap()
            .add(Placeholders.KEY_ID, key::getId)
            .add(Placeholders.KEY_NAME, key::getName)
            .add(Placeholders.KEY_VIRTUAL, () -> LangManager.getBoolean(key.isVirtual()))
            ;
    }
}
