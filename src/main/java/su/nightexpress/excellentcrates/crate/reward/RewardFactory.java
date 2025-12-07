package su.nightexpress.excellentcrates.crate.reward;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.crate.RewardType;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.limit.CooldownMode;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;

public class RewardFactory {

    @NotNull
    public static Reward read(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull String id, @NotNull FileConfig config, @NotNull String path) {
        // ------- CONVERT ITEM - START -------
        if (config.contains(path + ".Preview")) {
            ItemStack previewItem = config.getItemEncoded(path + ".Preview");
            if (previewItem == null) previewItem = new ItemStack(Material.BARRIER);

            AdaptedItem item = ItemHelper.vanilla(previewItem);
            config.set(path + ".PreviewData", item);
            config.remove(path + ".Preview");
        }
        // ------- CONVERT ITEM - END -------

        // ------- CONVERT LIMITS - START -------
        if (config.contains(path + ".Win_Limits")) {
            int winLimitAmount = config.getInt(path + ".Win_Limits.Amount", -1);
            long winLimitCooldown = config.getLong(path + ".Win_Limits.Cooldown", 0L);

            LimitValues winLimit = new LimitValues(winLimitAmount > 0, CooldownMode.CUSTOM, winLimitAmount, -1, winLimitCooldown, 0);
            winLimit.write(config, path + ".Win_Limit.Player");

            config.remove(path + ".Win_Limits");
        }
        // ------- CONVERT LIMITS - END -------


        // ------- CONVERT TYPE - START -------
        if (!config.contains(path + ".Type")) {
            if (!config.getStringList(path + ".Commands").isEmpty()) {
                config.set(path + ".Type", RewardType.COMMAND.name());
                config.remove(path + ".Items");
            }
            else {
                config.set(path + ".Type", RewardType.ITEM.name());
                config.remove(path + ".Commands");
                //config.remove(path + ".PreviewData");
            }
        }
        // ------- CONVERT TYPE - END -------

        String rarityId = String.valueOf(config.getString(path + ".Rarity"));
        Rarity rarity = plugin.getCrateManager().getRarity(rarityId);
        if (rarity == null) {
            plugin.error("Invalid rarity '" + rarityId + "', fallback to default rarity. Caused by '" + config.getFile().getName() + "' -> '" + path + "'.");
            rarity = plugin.getCrateManager().getMostCommonRarity();
        }

        RewardType type = config.getEnum(path + ".Type", RewardType.class, RewardType.ITEM);

        Reward reward = create(plugin, crate, id, rarity, type);
        reward.load(config, path);
        return reward;
    }

    @NotNull
    public static Reward wizardCreation(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull ItemStack source, @NotNull RewardType type, @NotNull AdaptedItem item) {
        String id = CrateUtils.generateRewardID(crate, source);
        Rarity rarity = plugin.getCrateManager().getMostCommonRarity();
        Reward reward = create(plugin, crate, id, rarity, type);

        if (reward instanceof ItemReward itemReward) {
            itemReward.setItems(Lists.newList(item));
        }
        else if (reward instanceof CommandReward commandReward) {
            commandReward.setPreview(item);
            commandReward.setName(ItemUtil.getNameSerialized(source));
            commandReward.setDescription(ItemUtil.getLoreSerialized(source));
        }

        return reward;
    }

    @NotNull
    public static Reward create(@NotNull CratesPlugin plugin,
                                @NotNull Crate crate,
                                @NotNull String id,
                                @NotNull Rarity rarity,
                                @NotNull RewardType type) {
        return switch (type) {
            case ITEM -> new ItemReward(plugin, crate, id, rarity);
            case COMMAND -> new CommandReward(plugin, crate, id, rarity);
        };
    }
}
