package su.nightexpress.excellentcrates.crate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenuAuto;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.data.CrateUser;
import su.nightexpress.excellentcrates.data.UserRewardWinLimit;

import java.util.ArrayList;
import java.util.List;

public class CratePreview extends AbstractMenuAuto<ExcellentCrates, ICrateReward> {

    private static final String PLACEHOLDER_WIN_LIMIT_AMOUNT   = "%win_limit_amount%";
    private static final String PLACEHOLDER_WIN_LIMIT_COOLDOWN = "%win_limit_cooldown%";
    private static final String PLACEHOLDER_WIN_LIMIT_DRAINED  = "%win_limit_drained%";

    private final ICrate crate;
    private final int[]        rewardSlots;
    private final String       rewardName;
    private final List<String> rewardLore;
    private final List<String> rewardLoreLimitAmount;
    private final List<String> rewardLoreLimitCoolown;
    private final List<String> rewardLoreLimitDrained;
    private final boolean      hideDrainedRewards;

    public CratePreview(@NotNull ICrate crate, @NotNull JYML cfg) {
        super(crate.plugin(), cfg, "");
        this.crate = crate;
        this.title = crate.replacePlaceholders().apply(this.title);

        this.hideDrainedRewards = cfg.getBoolean("Reward.Hide_Drained_Rewards");
        this.rewardSlots = cfg.getIntArray("Reward.Slots");
        this.rewardName = StringUtil.color(cfg.getString("Reward.Name", ICrateReward.PLACEHOLDER_PREVIEW_NAME));
        this.rewardLore = StringUtil.color(cfg.getStringList("Reward.Lore.Default"));
        this.rewardLoreLimitAmount = StringUtil.color(cfg.getStringList("Reward.Lore.Win_Limit.Amount"));
        this.rewardLoreLimitCoolown = StringUtil.color(cfg.getStringList("Reward.Lore.Win_Limit.Cooldown"));
        this.rewardLoreLimitDrained = StringUtil.color(cfg.getStringList("Reward.Lore.Win_Limit.Drained"));

        IMenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.CLOSE) {
                    player.closeInventory();
                }
                else this.onItemClickDefault(player, type2);
            }
        };

        for (String id : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + id, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(click);
            }
            this.addItem(menuItem);
        }
    }

    @Override
    protected int[] getObjectSlots() {
        return this.rewardSlots;
    }

    @Override
    @NotNull
    protected List<ICrateReward> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.hideDrainedRewards ? crate.getRewards(player) : crate.getRewards());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull ICrateReward reward) {
        ItemStack item = reward.getPreview();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        CrateUser crateUser = plugin.getUserManager().getOrLoadUser(player);
        UserRewardWinLimit rewardLimit = crateUser.getRewardWinLimit(reward);

        List<String> lore = new ArrayList<>(this.rewardLore);
        if (rewardLimit == null || rewardLimit.isDrained(reward) || !reward.isWinLimitedAmount())
            lore.remove(PLACEHOLDER_WIN_LIMIT_AMOUNT);
        if (rewardLimit == null || rewardLimit.isDrained(reward) || rewardLimit.isExpired())
            lore.remove(PLACEHOLDER_WIN_LIMIT_COOLDOWN);
        if (rewardLimit == null || !rewardLimit.isDrained(reward)) lore.remove(PLACEHOLDER_WIN_LIMIT_DRAINED);

        lore = StringUtil.replace(lore, PLACEHOLDER_WIN_LIMIT_AMOUNT, false, this.rewardLoreLimitAmount);
        lore = StringUtil.replace(lore, PLACEHOLDER_WIN_LIMIT_COOLDOWN, false, this.rewardLoreLimitCoolown);
        lore = StringUtil.replace(lore, PLACEHOLDER_WIN_LIMIT_DRAINED, false, this.rewardLoreLimitDrained);
        lore.replaceAll(crateUser.replacePlaceholers(reward));

        meta.setDisplayName(this.rewardName);
        meta.setLore(lore);
        item.setItemMeta(meta);

        ItemUtil.replace(item, reward.replacePlaceholders());
        ItemUtil.replace(item, this.crate.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected IMenuClick getObjectClick(@NotNull Player player, @NotNull ICrateReward reward) {
        return (player1, type, e) -> {

        };
    }

    @Override
    public boolean cancelClick(@NotNull SlotType slotType, int slot) {
        return true;
    }

    @Override
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }
}
