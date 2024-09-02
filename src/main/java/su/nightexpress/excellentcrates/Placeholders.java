package su.nightexpress.excellentcrates;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.*;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.tag.Tags;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Placeholders extends su.nightexpress.nightcore.util.Placeholders {

    public static final String WIKI_URL          = "https://nightexpress.gitbook.io/excellentcrates/";
    public static final String WIKI_PLACEHOLDERS = WIKI_URL + "utility/placeholders";

    public static final String CHECK_MARK = Tags.GREEN.enclose("✔");
    public static final String WARN_MARK  = Tags.ORANGE.enclose("[❗]");
    public static final String CROSS_MARK = Tags.RED.enclose("✘");

    public static final String SKIN_NEW_CRATE  = "7a3c8c6d3aaa96363d4bef2578f1024781ea14e9d85a9dcfc0935847a6fb5c8d";
    public static final String SKIN_NEW_REWARD = "2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec";

    public static final String GENERIC_NAME   = "%name%";
    public static final String GENERIC_AMOUNT = "%amount%";
    public static final String GENERIC_TIME   = "%time%";
    public static final String GENERIC_KEYS   = "%keys%";

    public static final String CURRENCY_NAME = "%currency_name%";
    public static final String CURRENCY_ID   = "%currency_id%";

    public static final  String RARITY_ID          = "%rarity_id%";
    public static final  String RARITY_NAME        = "%rarity_name%";
    public static final  String RARITY_WEIGHT      = "%rarity_weight%";
    @Deprecated
    private static final String RARITY_CHANCE      = "%rarity_chance%";
    public static final  String RARITY_ROLL_CHANCE = "%rarity_roll_chance%";

    public static final String MILESTONE_OPENINGS       = "%milestone_openings%";
    public static final String MILESTONE_REWARD_ID      = "%milestone_reward_id%";
    public static final String MILESTONE_INSPECT_REWARD = "%milestone_inspect_reward%";

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
    public static final String CRATE_KEY_REQUIRED          = "%crate_key_required%";
    public static final String CRATE_KEYS                  = "%crate_key_ids%";
    public static final String CRATE_PUSHBACK_ENABLED      = "%crate_pushback_enabled%";
    public static final String CRATE_HOLOGRAM_ENABLED      = "%crate_hologram_enabled%";
    public static final String CRATE_HOLOGRAM_TEMPLATE     = "%crate_hologram_template%";
    public static final String CRATE_HOLOGRAM_Y_OFFSET     = "%crate_hologram_y_offset%";
    public static final String CRATE_LOCATIONS             = "%crate_locations%";
    public static final String CRATE_EFFECT_MODEL          = "%crate_effect_model%";
    public static final String CRATE_EFFECT_PARTICLE_NAME  = "%crate_effect_particle_name%";
    public static final String CRATE_EFFECT_PARTICLE_DATA  = "%crate_effect_particle_data%";
    public static final String CRATE_REWARDS_AMOUNT        = "%crate_rewards_amount%";
    public static final String CRATE_MILESTONES_AMOUNT     = "%crate_milestones_amount%";
    public static final String CRATE_MILESTONES_REPEATABLE = "%crate_milestones_repeatable%";

    public static final String CRATE_INSPECT = "%crate_inspect%";

    public static final String KEY_ID        = "%key_id%";
    public static final String KEY_NAME      = "%key_name%";
    public static final String KEY_VIRTUAL   = "%key_virtual%";
    public static final String KEY_ITEM_NAME = "%key_item_name%";

    public static final  String REWARD_ID                 = "%reward_id%";
    public static final  String REWARD_NAME               = "%reward_name%";
    @Deprecated
    private static final String REWARD_CHANCE             = "%reward_chance%";
    @Deprecated
    private static final String REWARD_REAL_CHANCE        = "%reward_real_chance%";
    public static final  String REWARD_WEIGHT             = "%reward_weight%";
    public static final  String REWARD_ROLL_CHANCE        = "%reward_roll_chance%";
    public static final  String REWARD_RARITY_NAME        = "%reward_rarity_name%";
    @Deprecated
    public static final  String REWARD_RARITY_CHANCE      = "%reward_rarity_chance%";
    public static final  String REWARD_RARITY_WEIGHT      = "%reward_rarity_weight%";
    public static final  String REWARD_RARITY_ROLL_CHANCE = "%reward_rarity_roll_chance%";
    public static final  String REWARD_PREVIEW_NAME       = "%reward_preview_name%";
    public static final  String REWARD_PREVIEW_LORE       = "%reward_preview_lore%";
    public static final  String REWARD_BROADCAST          = "%reward_broadcast%";
    public static final  String REWARD_PLACEHOLDER_APPLY  = "%reward_placeholder_apply%";


    public static final Function<LimitType, String> REWARD_WIN_LIMIT_ENABLED  = limitType -> "%reward_" + limitType.name().toLowerCase() + "_win_limit_enabled%";
    public static final Function<LimitType, String> REWARD_WIN_LIMIT_AMOUNT   = limitType -> "%reward_" + limitType.name().toLowerCase() + "_win_limit_amount%";
    public static final Function<LimitType, String> REWARD_WIN_LIMIT_COOLDOWN = limitType -> "%reward_" + limitType.name().toLowerCase() + "_win_limit_cooldown%";
    public static final Function<LimitType, String> REWARD_WIN_LIMIT_STEP     = limitType -> "%reward_" + limitType.name().toLowerCase() + "_win_limit_step%";

    public static final String REWARD_IGNORED_FOR_PERMISSIONS = "%reward_ignored_for_permissions%";
    public static final String REWARD_EDITOR_COMMANDS         = "%reward_editor_commands%";
    public static final String REWARD_EDITOR_ITEMS            = "%reward_editor_items%";
    public static final String REWARD_INSPECT_CONTENT         = "%reward_inspect_content%";

    @NotNull
    public static String problem(@NotNull String text) {
        return CROSS_MARK + " " + Tags.LIGHT_GRAY.enclose(text);
    }

    @NotNull
    public static String good(@NotNull String text) {
        return CHECK_MARK + " " + Tags.LIGHT_GRAY.enclose(text);
    }

    @NotNull
    public static String warning(@NotNull String text) {
        return WARN_MARK + " " + Tags.LIGHT_GRAY.enclose(text);
    }

    /*@NotNull
    public static String toggled(boolean b, @NotNull String textA, @NotNull String textB) {
        if (b) {
            return Tags.LIGHT_GREEN.enclose("☑") + " " + Tags.LIGHT_GRAY.enclose(textA);
        }
        return Tags.LIGHT_RED.enclose("❎") + " " + Tags.LIGHT_GRAY.enclose(textB);
    }

    @NotNull
    public static String enabledOrDisabled(boolean b) {
        String prefix = b ? Tags.LIGHT_GREEN.enclose("☑") : Tags.LIGHT_RED.enclose("❎");

        return prefix + Tags.LIGHT_GRAY.enclose(CoreLang.getEnabledOrDisabled(b));
    }*/

    @NotNull
    public static PlaceholderMap forCrateAll(@NotNull Crate crate) {
        return PlaceholderMap.fusion(crate.getPlaceholders(), forCrateEditor(crate));
    }

    @NotNull
    public static PlaceholderMap forCrate(@NotNull Crate crate) {
        return new PlaceholderMap()
            .add(CRATE_ID, crate.getId())
            .add(CRATE_NAME, crate::getName)
            .add(CRATE_PERMISSION, crate::getPermission)
            .add(CRATE_OPEN_COST, () -> {
                if (!crate.hasOpenCost()) {
                    return Lang.OTHER_FREE.getString();
                }

                return crate.getOpenCostMap().entrySet().stream().map(entry -> entry.getKey().format(entry.getValue()))
                    .collect(Collectors.joining(", "));
            })
            .add(CRATE_OPEN_COOLDOWN, () -> {
                if (crate.getOpenCooldown() == 0L) return Lang.OTHER_DISABLED.getString();
                if (crate.getOpenCooldown() < 0L) return Lang.OTHER_ONE_TIMED.getString();

                return TimeUtil.formatTime(crate.getOpenCooldown() * 1000L);
            })
            .add(CRATE_LAST_OPENER, () -> {
                String last = crate.getLastOpener();
                if (last == null) return "-";

                Player player = Players.getPlayer(last);
                return player == null ? last : player.getDisplayName();
            })
            .add(CRATE_LAST_REWARD, () -> {
                String last = crate.getLastReward();
                return last == null ? "-" : last;
            })
            ;
    }

    @NotNull
    public static PlaceholderMap forCrateEditor(@NotNull Crate crate) {
        return new PlaceholderMap()
            .add(CRATE_PERMISSION_REQUIRED, () -> CoreLang.getYesOrNo(crate.isPermissionRequired()))
            .add(CRATE_KEY_REQUIRED, () -> CoreLang.getYesOrNo(crate.isKeyRequired()))
            .add(CRATE_KEYS, () -> {
                return crate.getKeys().stream().map(key -> good(key.getName())).collect(Collectors.joining("\n"));
            })
            .add(CRATE_PUSHBACK_ENABLED, () -> CoreLang.getEnabledOrDisabled(crate.isPushbackEnabled()))
            .add(CRATE_HOLOGRAM_ENABLED, () -> CoreLang.getEnabledOrDisabled(crate.isHologramEnabled()))
            .add(CRATE_HOLOGRAM_TEMPLATE, () -> {
                if (!crate.hasValidHologram()) return problem(crate.getHologramTemplate());

                return good(crate.getHologramTemplate());
            })
            .add(CRATE_HOLOGRAM_Y_OFFSET, () -> NumberUtil.format(crate.getHologramYOffset()))
            .add(CRATE_LOCATIONS, () -> {
                return crate.getBlockPositions().stream().map(worldPos -> {
                    Block block = worldPos.toBlock();
                    if (block == null) return problem("null");

                    String name = Tags.LIGHT_ORANGE.enclose(LangAssets.get(block.getType()));

                    String x = Tags.LIGHT_ORANGE.enclose(NumberUtil.format(worldPos.getX()));
                    String y = Tags.LIGHT_ORANGE.enclose(NumberUtil.format(worldPos.getY()));
                    String z = Tags.LIGHT_ORANGE.enclose(NumberUtil.format(worldPos.getZ()));
                    String world = Tags.LIGHT_ORANGE.enclose(worldPos.getWorldName());
                    String coords = x + ", " + y + ", " + z + " in " + world;
                    String line = coords + " (" + name + ")";

                    return block.isEmpty() ? problem(line) : good(line);
                }).collect(Collectors.joining("\n"));
            })
            .add(CRATE_EFFECT_MODEL, () -> StringUtil.capitalizeUnderscored(crate.getEffectModel().name().toLowerCase()))
            .add(CRATE_EFFECT_PARTICLE_NAME, () -> {
                UniParticle particle = crate.getEffectParticle();
                if (particle.getParticle() == null) return problem("Undefined");

                return good(StringUtil.capitalizeUnderscored(particle.getParticle().name().toLowerCase()));
            })
            .add(CRATE_EFFECT_PARTICLE_DATA, () -> {
                UniParticle particle = crate.getEffectParticle();
                if (!CrateUtils.isSupportedParticleData(particle)) return problem("Not supported");

                Object data = particle.getData();
                if (data instanceof BlockData blockData) {
                    return good(LangAssets.get(blockData.getMaterial()));
                }
                else if (data instanceof ItemStack itemStack) {
                    return good(LangAssets.get(itemStack.getType()));
                }
                else if (data instanceof Particle.DustOptions dustOptions) {
                    Color color = dustOptions.getColor();
                    return good("Red: " + color.getRed() + ", Green: " + color.getGreen() + ", Blue: " + color.getBlue() + ", Size: " + dustOptions.getSize());
                }
                else if (data instanceof Float f) {
                    return good(String.valueOf(f));
                }
                else if (data instanceof Integer i) {
                    return good(String.valueOf(i));
                }

                return problem("Not set");
            })
            .add(CRATE_REWARDS_AMOUNT, () -> NumberUtil.format(crate.getRewards().size()))
            .add(CRATE_MILESTONES_AMOUNT, () -> NumberUtil.format(crate.getMilestones().size()))
            .add(CRATE_MILESTONES_REPEATABLE, () -> CoreLang.getYesOrNo(crate.isMilestonesRepeatable()))
            .add(CRATE_INSPECT, () -> {
                List<String> list = new ArrayList<>();

                if (!crate.hasRewards()) list.add(problem("No rewards added!"));

                if (crate.isKeyRequired() && crate.getKeys().isEmpty()) {
                    list.add(problem("No keys assigned!"));
                }

                if (!crate.hasValidPreview()) list.add(warning("No preview config!"));
                if (!crate.hasValidOpening()) list.add(warning("No opening config!"));
                if (crate.isHologramEnabled() && !crate.hasValidHologram()) list.add(warning("Invalid hologram template!"));

                return String.join("\n", list);
            })
            .add(CRATE_ANIMATION_CONFIG, () -> {
                if (crate.getOpeningConfig() == null) return Lang.OTHER_DISABLED.getString();
                if (!crate.hasValidOpening()) return problem(crate.getOpeningConfig());

                return good(String.valueOf(crate.getOpeningConfig()));
            })
            .add(CRATE_PREVIEW_CONFIG, () -> {
                if (crate.getPreviewConfig() == null) return Lang.OTHER_DISABLED.getString();
                if (!crate.hasValidPreview()) return problem(crate.getPreviewConfig());

                return good(String.valueOf(crate.getPreviewConfig()));
            })
            ;
    }

    @NotNull
    public static PlaceholderMap forRewardAll(@NotNull Reward reward) {
        return PlaceholderMap.fusion(reward.getPlaceholders(), forRewardEditor(reward));
    }

    @NotNull
    public static PlaceholderMap forReward(@NotNull Reward reward) {
        PlaceholderMap placeholderMap = new PlaceholderMap()
            .add(REWARD_ID, reward::getId)
            .add(REWARD_NAME, reward::getName)
            .add(REWARD_WEIGHT, () -> NumberUtil.format(reward.getWeight()))
            .add(REWARD_ROLL_CHANCE, () -> NumberUtil.format(reward.getRollChance()))
            .add(REWARD_CHANCE, () -> NumberUtil.format(reward.getWeight()))
            .add(REWARD_REAL_CHANCE, () -> NumberUtil.format(reward.getRollChance()))
            .add(REWARD_RARITY_NAME, () -> reward.getRarity().getName())
            .add(REWARD_RARITY_CHANCE, () -> NumberUtil.format(reward.getRarity().getWeight()))
            .add(REWARD_RARITY_WEIGHT, () -> NumberUtil.format(reward.getRarity().getWeight()))
            .add(REWARD_RARITY_ROLL_CHANCE, () -> NumberUtil.format(reward.getRarity().getRollChance(reward.getCrate())))
            .add(REWARD_PREVIEW_NAME, () -> ItemUtil.getItemName(reward.getPreview()))
            .add(REWARD_PREVIEW_LORE, () -> String.join("\n", ItemUtil.getLore(reward.getPreview())));
        placeholderMap.add(reward.getRarity().getPlaceholders());

        return placeholderMap;
    }

    @NotNull
    public static PlaceholderMap forRewardEditor(@NotNull Reward reward) {
        PlaceholderMap map = new PlaceholderMap();

        for (LimitType limitType : LimitType.values()) {
            RewardWinLimit winLimit = reward.getWinLimit(limitType);
            map
                .add(REWARD_WIN_LIMIT_ENABLED.apply(limitType), () -> Lang.getYesOrNo(winLimit.isEnabled()))
                .add(REWARD_WIN_LIMIT_AMOUNT.apply(limitType), () -> winLimit.isUnlimitedAmount() ? Lang.OTHER_INFINITY.getString() : String.valueOf(winLimit.getAmount()))
                .add(REWARD_WIN_LIMIT_STEP.apply(limitType), () -> String.valueOf(winLimit.getCooldownStep()))
                .add(REWARD_WIN_LIMIT_COOLDOWN.apply(limitType), () -> {
                    if (!winLimit.hasCooldown()) return Lang.OTHER_DISABLED.getString();
                    return winLimit.isMidnight() ? Lang.OTHER_MIDNIGHT.getString() : TimeUtil.formatTime(winLimit.getCooldown() * 1000L);
                });
        }

        map
            .add(REWARD_BROADCAST, () -> CoreLang.getYesOrNo(reward.isBroadcast()))
            .add(REWARD_PLACEHOLDER_APPLY, () -> CoreLang.getYesOrNo(reward.isPlaceholderApply()))
            .add(REWARD_IGNORED_FOR_PERMISSIONS, () -> {
                return String.join("\n", reward.getIgnoredForPermissions().stream().map(Placeholders::good).toList());
            })
            .add(REWARD_EDITOR_ITEMS, () -> {
                return reward.getItems().stream()
                    .filter(item -> !item.getType().isAir())
                    .map(item -> good(ItemUtil.getItemName(item) + " x" + item.getAmount())).collect(Collectors.joining("\n"));
            })
            .add(REWARD_EDITOR_COMMANDS, () -> {
                return reward.getCommands().stream().map(Placeholders::good).collect(Collectors.joining("\n"));
            })
            .add(REWARD_INSPECT_CONTENT, () -> {
                if (!reward.hasContent()) return problem("Reward gives nothing!");
                return good("Reward content is present.");
            })
            ;

        return map;
    }

    @NotNull
    public static PlaceholderMap forMilestone(@NotNull Milestone milestone) {
        return new PlaceholderMap()
            .add(MILESTONE_OPENINGS, () -> NumberUtil.format(milestone.getOpenings()))
            .add(MILESTONE_REWARD_ID, milestone::getRewardId)
            .add(MILESTONE_INSPECT_REWARD, () -> {
                return milestone.getReward() == null ? problem("Invalid reward!") : good("Reward is correct.");
            });
    }

    @NotNull
    public static PlaceholderMap forRarity(@NotNull Rarity rarity) {
        return new PlaceholderMap()
            .add(RARITY_ID, rarity::getId)
            .add(RARITY_NAME, rarity::getName)
            .add(RARITY_WEIGHT, () -> NumberUtil.format(rarity.getWeight()))
            .add(RARITY_CHANCE, () -> NumberUtil.format(rarity.getWeight()))
            .add(RARITY_ROLL_CHANCE, () -> NumberUtil.format(rarity.getRollChance()));
    }

    @NotNull
    public static PlaceholderMap forKey(@NotNull CrateKey key) {
        return new PlaceholderMap()
            .add(KEY_ID, key::getId)
            .add(KEY_NAME, key::getName)
            .add(KEY_VIRTUAL, () -> CoreLang.getYesOrNo(key.isVirtual()))
            ;
    }

    @NotNull
    public static PlaceholderMap forCurrency(@NotNull Currency currency) {
        return new PlaceholderMap()
            .add(Placeholders.CURRENCY_ID, currency::getId)
            .add(Placeholders.CURRENCY_NAME, currency::getName);
    }
}
