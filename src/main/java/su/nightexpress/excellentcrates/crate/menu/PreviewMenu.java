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
import su.nexmedia.engine.utils.ItemReplacer;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.UserRewardData;
import su.nightexpress.excellentcrates.util.InteractType;

import java.util.Collections;
import java.util.List;

public class PreviewMenu extends ConfigMenu<ExcellentCratesPlugin> implements AutoPaged<Reward>, Linked<CrateSource> {

    private static final String PLACEHOLDER_WIN_LIMIT_AMOUNT        = "%win_limit_amount%";
    private static final String PLACEHOLDER_WIN_LIMIT_COOLDOWN      = "%win_limit_cooldown%";
    private static final String PLACEHOLDER_WIN_LIMIT_DRAINED       = "%win_limit_drained%";
    private static final String PLACEHOLDER_WIN_LIMIT_NO_PERMISSION = "%win_limit_no_permission%";

    private final int[]        rewardSlots;
    private final String       rewardName;
    private final List<String> rewardLore;
    private final List<String> rewardLoreLimitAmount;
    private final List<String> rewardLoreLimitCoolown;
    private final List<String> rewardLoreLimitDrained;
    private final List<String> rewardLoreLimitNoPermission;
    private final boolean      hideDrainedRewards;

    private final ViewLink<CrateSource> viewLink;

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
        this.rewardLoreLimitNoPermission = cfg.getStringList("Reward.Lore.Win_Limit.No_Permission");

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CLOSE, ClickHandler.forClose(this))
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this));

        this.registerHandler(Type.class)
            .addClick(Type.OPEN, (viewer, event) -> {
                this.plugin.runTask(task -> {
                    CrateSource source = this.getLink().get(viewer);
                    Player player = viewer.getPlayer();

                    player.closeInventory();
                    this.plugin.getCrateManager().interactCrate(player, source.getCrate(), InteractType.CRATE_OPEN, source.getItem(), source.getBlock());
                });
            })
            .addClick(Type.MILESTONES, (viewer, event) -> {
                this.plugin.runTask(task -> {
                    Crate crate = this.getLink().get(viewer).getCrate();
                    this.plugin.getCrateManager().getMilestonesMenu().open(viewer.getPlayer(), crate);
                });
            });

        this.load();

        this.getItems().forEach(menuItem -> {
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                ItemReplacer.create(item).readMeta().replacePlaceholderAPI(viewer.getPlayer()).writeMeta();
            });

            if (menuItem.getType() == Type.OPEN) {
                menuItem.getOptions().setVisibilityPolicy(viewer -> {
                    CrateSource source = this.getLink().get(viewer);
                    return source.getItem() != null || source.getBlock() != null;
                });
            }
        });
    }

    private enum Type {
        MILESTONES, OPEN
    }

    @Override
    @NotNull
    public ViewLink<CrateSource> getLink() {
        return this.viewLink;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        CrateSource source = this.getLink().get(viewer);

        options.setTitle(source.getCrate().replacePlaceholders().apply(options.getTitle()));
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
        Crate crate = this.getLink().get(player).getCrate();

        return (this.hideDrainedRewards ? crate.getRewards(player) : crate.getRewards()).stream().filter(r -> r.getWeight() > 0).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Reward reward) {
        CrateUser user = plugin.getUserManager().getUserData(player);
        Crate crate = this.getLink().get(player).getCrate();
        ItemStack item = reward.getPreview();

        UserRewardData rewardData = user.getRewardWinLimit(reward);

        //List<String> lore = new ArrayList<>(this.rewardLore);
        boolean hasIgnoredPermission = reward.getIgnoredForPermissions().stream().anyMatch(player::hasPermission);
        boolean hasAmountLimit = rewardData != null && !hasIgnoredPermission && !rewardData.isDrained(reward) && !reward.isWinLimitedAmount();
        boolean hasCooldown = rewardData != null && !hasIgnoredPermission && !rewardData.isDrained(reward) && !rewardData.isExpired();
        boolean isDrained = rewardData != null && !hasIgnoredPermission && rewardData.isDrained(reward);
        //if (rewardData == null || hasIgnoredPermission || rewardData.isDrained(reward) || !reward.isWinLimitedAmount())
        //    lore.remove(PLACEHOLDER_WIN_LIMIT_AMOUNT);

        //if (rewardData == null || hasIgnoredPermission || rewardData.isDrained(reward) || rewardData.isExpired())
        //    lore.remove(PLACEHOLDER_WIN_LIMIT_COOLDOWN);

        //if (rewardData == null || hasIgnoredPermission || !rewardData.isDrained(reward)) lore.remove(PLACEHOLDER_WIN_LIMIT_DRAINED);
        //if (!hasIgnoredPermission) lore.remove(PLACEHOLDER_WIN_LIMIT_NO_PERMISSION);

        int amountLeft = rewardData == null ? reward.getWinLimitAmount() : reward.getWinLimitAmount() - rewardData.getAmount();
        long expireIn = rewardData == null ? 0L : rewardData.getExpireDate();

        ItemReplacer.create(item).setDisplayName(this.rewardName).setLore(this.rewardLore).hideFlags().trimmed()
            .replaceLoreExact(PLACEHOLDER_WIN_LIMIT_AMOUNT, hasAmountLimit ? this.rewardLoreLimitAmount : Collections.emptyList())
            .replaceLoreExact(PLACEHOLDER_WIN_LIMIT_COOLDOWN, hasCooldown ? this.rewardLoreLimitCoolown : Collections.emptyList())
            .replaceLoreExact(PLACEHOLDER_WIN_LIMIT_DRAINED, isDrained ? this.rewardLoreLimitDrained : Collections.emptyList())
            .replaceLoreExact(PLACEHOLDER_WIN_LIMIT_NO_PERMISSION, hasIgnoredPermission ? this.rewardLoreLimitNoPermission : Collections.emptyList())
            .replace(Placeholders.GENERIC_AMOUNT, () -> NumberUtil.format(amountLeft))
            .replace(Placeholders.GENERIC_TIME, () -> TimeUtil.formatTimeLeft(expireIn))
            .replace(reward.getPlaceholders())
            .replace(crate.getPlaceholders())
            .replace(Colorizer::apply)
            .writeMeta();
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Reward reward) {
        return (viewer, event) -> {

        };
    }
}
