package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.menu.link.Linked;
import su.nexmedia.engine.api.menu.link.ViewLink;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.UserRewardData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PreviewMenu extends ConfigMenu<ExcellentCratesPlugin> implements AutoPaged<Reward>, Linked<Crate> {

    private static final String PLACEHOLDER_WIN_LIMIT_AMOUNT   = "%win_limit_amount%";
    private static final String PLACEHOLDER_WIN_LIMIT_COOLDOWN = "%win_limit_cooldown%";
    private static final String PLACEHOLDER_WIN_LIMIT_DRAINED  = "%win_limit_drained%";

    private final int[]        rewardSlots;
    private final String       rewardName;
    private final List<String> rewardLore;
    private final List<String> rewardLoreLimitAmount;
    private final List<String> rewardLoreLimitCoolown;
    private final List<String> rewardLoreLimitDrained;
    private final boolean      hideDrainedRewards;

    private final ViewLink<Crate> viewLink;

    public PreviewMenu(@NotNull ExcellentCratesPlugin plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.viewLink = new ViewLink<>();

        this.hideDrainedRewards = cfg.getBoolean("Reward.Hide_Drained_Rewards");
        this.rewardSlots = cfg.getIntArray("Reward.Slots");
        this.rewardName = Colorizer.apply(cfg.getString("Reward.Name", Placeholders.REWARD_PREVIEW_NAME));
        this.rewardLore = cfg.getStringList("Reward.Lore.Default");
        this.rewardLoreLimitAmount = cfg.getStringList("Reward.Lore.Win_Limit.Amount");
        this.rewardLoreLimitCoolown = cfg.getStringList("Reward.Lore.Win_Limit.Cooldown");
        this.rewardLoreLimitDrained = cfg.getStringList("Reward.Lore.Win_Limit.Drained");

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CLOSE, (viewer, event) -> {
                this.plugin.runTask(task -> viewer.getPlayer().closeInventory());
            })
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this));

        this.registerHandler(Type.class)
            .addClick(Type.MILESTONES, (viewer, event) -> {
                this.plugin.runTask(task -> {
                    Crate crate = this.getLink().get(viewer);
                    this.plugin.getCrateManager().getMilestonesMenu().open(viewer.getPlayer(), crate);
                });
            });

        this.load();
    }

    private enum Type {
        MILESTONES
    }

    @Override
    @NotNull
    public ViewLink<Crate> getLink() {
        return this.viewLink;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        Crate crate = this.getLink().get(viewer);

        options.setTitle(crate.replacePlaceholders().apply(options.getTitle()));
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);
        this.getLink().clear(viewer);
    }

    @Override
    public int[] getObjectSlots() {
        return this.rewardSlots;
    }

    @Override
    @NotNull
    public List<Reward> getObjects(@NotNull Player player) {
        Crate crate = this.getLink().get(player);

        return (this.hideDrainedRewards ? crate.getRewards(player) : crate.getRewards()).stream().filter(r -> r.getWeight() > 0).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Reward reward) {
        Crate crate = this.getLink().get(player);

        ItemStack item = reward.getPreview();
        ItemUtil.mapMeta(item, meta -> {
            CrateUser user = plugin.getUserManager().getUserData(player);
            UserRewardData rewardData = user.getRewardWinLimit(reward);

            List<String> lore = new ArrayList<>(this.rewardLore);
            if (rewardData == null || rewardData.isDrained(reward) || !reward.isWinLimitedAmount())
                lore.remove(PLACEHOLDER_WIN_LIMIT_AMOUNT);
            if (rewardData == null || rewardData.isDrained(reward) || rewardData.isExpired())
                lore.remove(PLACEHOLDER_WIN_LIMIT_COOLDOWN);
            if (rewardData == null || !rewardData.isDrained(reward)) lore.remove(PLACEHOLDER_WIN_LIMIT_DRAINED);

            lore = StringUtil.replaceInList(lore, PLACEHOLDER_WIN_LIMIT_AMOUNT, this.rewardLoreLimitAmount);
            lore = StringUtil.replaceInList(lore, PLACEHOLDER_WIN_LIMIT_COOLDOWN, this.rewardLoreLimitCoolown);
            lore = StringUtil.replaceInList(lore, PLACEHOLDER_WIN_LIMIT_DRAINED, this.rewardLoreLimitDrained);

            int amountLeft = rewardData == null ? reward.getWinLimitAmount() : reward.getWinLimitAmount() - rewardData.getAmount();
            long expireIn = rewardData == null ? 0L : rewardData.getExpireDate();

            lore.replaceAll(str -> str
                .replace(Placeholders.GENERIC_AMOUNT, String.valueOf(amountLeft))
                .replace(Placeholders.GENERIC_TIME, TimeUtil.formatTimeLeft(expireIn))
            );

            meta.setDisplayName(this.rewardName);
            meta.setLore(lore);

            ItemUtil.replace(meta, reward.replacePlaceholders());
            ItemUtil.replace(meta, crate.replacePlaceholders());
            ItemUtil.replace(meta, Colorizer::apply);
        });
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Reward reward) {
        return (viewer, event) -> {

        };
    }
}