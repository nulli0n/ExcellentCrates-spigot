package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.RewardWinData;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class PreviewMenu extends ConfigMenu<CratesPlugin> implements AutoFilled<Reward>, Linked<CrateSource> {

    private static final String WIN_LIMIT_AMOUNT        = "%win_limit_amount%";
    private static final String WIN_LIMIT_COOLDOWN      = "%win_limit_cooldown%";
    private static final String WIN_LIMIT_OUT           = "%win_limit_drained%";
    private static final String WIN_LIMIT_NO_PERMISSION = "%win_limit_no_permission%";

    private int[]        rewardSlots;
    private String       rewardName;
    private List<String> rewardLore;
    private List<String> rewardLimitAmountLore;
    private List<String> rewardLimitCoolownLore;
    private List<String> rewardLimitOutLore;
    private List<String> rewardLimitBadPermissionLore;
    private boolean      hideExceededRewards;

    protected final ItemHandler openHandler;
    protected final ItemHandler milesHandler;

    private final ViewLink<CrateSource> viewLink;

    public PreviewMenu(@NotNull CratesPlugin plugin, @NotNull FileConfig config) {
        super(plugin, config);
        this.viewLink = new ViewLink<>();

        this.addHandler(this.openHandler = new ItemHandler("open", (viewer, event) -> {
            CrateSource source = this.getLink(viewer);
            if (!source.hasItem() || !source.hasBlock()) return;

            Player player = viewer.getPlayer();

            this.runNextTick(() -> {
                player.closeInventory();
                this.plugin.getCrateManager().interactCrate(player, source.getCrate(), InteractType.CRATE_OPEN, source.getItem(), source.getBlock());
            });
        }));

        this.addHandler(this.milesHandler = new ItemHandler("milestones", (viewer, event) -> {
            this.runNextTick(() -> {
                this.plugin.getCrateManager().openMilestones(viewer.getPlayer(), this.getLink(viewer));
            });
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                ItemReplacer.create(item).readMeta().replacePlaceholderAPI(viewer.getPlayer()).writeMeta();
            });

            if (menuItem.getHandler() == this.openHandler) {
                menuItem.getOptions().setVisibilityPolicy(viewer -> {
                    CrateSource source = this.getLink(viewer);
                    return source.getItem() != null || source.getBlock() != null;
                });
            }
            else if (menuItem.getHandler() == this.milesHandler) {
                menuItem.getOptions().setVisibilityPolicy(viewer -> this.getLink(viewer).getCrate().hasMilestones());
            }
        });
    }

    @Override
    @NotNull
    public ViewLink<CrateSource> getLink() {
        return this.viewLink;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        CrateSource source = this.getLink(viewer);

        options.editTitle(source.getCrate().replacePlaceholders());
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Reward> autoFill) {
        Player player = viewer.getPlayer();
        Crate crate = this.getLink(player).getCrate();

        autoFill.setSlots(this.getRewardSlots());
        autoFill.setItems((this.hideExceededRewards ? crate.getRewards(player) : crate.getRewards()).stream().filter(Reward::isRollable).toList());
        autoFill.setItemCreator(reward -> {
            CrateUser user = plugin.getUserManager().getUserData(player);
            ItemStack item = reward.getPreview();

            List<String> limitAmountLore = new ArrayList<>();
            List<String> limitCooldownLore = new ArrayList<>();
            List<String> limitOutLore = new ArrayList<>();
            List<String> limitPermissionLore = new ArrayList<>();
            if (!reward.hasBadPermissions(player)) {
                RewardWinData playerWinData = user.getWinData(reward);
                RewardWinData globalWinData = reward.getGlobalWinData();

                boolean playerLimitOut = playerWinData != null && playerWinData.isOut(reward.getPlayerWinLimit());
                boolean globalLimitOut = globalWinData != null && globalWinData.isOut(reward.getGlobalWinLimit());
                if (playerLimitOut || globalLimitOut) {
                    limitOutLore.addAll(this.rewardLimitOutLore);
                }
                else {
                    int amountLeft = reward.getWinsAmountLeft(player);
                    long expireIn = reward.getWinCooldown(player);

                    if (amountLeft > 0) {
                        limitAmountLore.addAll(this.rewardLimitAmountLore);
                        limitAmountLore.replaceAll(str -> str.replace(GENERIC_AMOUNT, NumberUtil.format(amountLeft)));
                    }
                    if (expireIn != 0L) {
                        limitCooldownLore.addAll(this.rewardLimitCoolownLore);
                        limitCooldownLore.replaceAll(str -> str.replace(GENERIC_TIME, TimeUtil.formatDuration(expireIn)));
                    }
                }
            }
            else {
                limitPermissionLore.addAll(this.rewardLimitBadPermissionLore);
            }

            ItemReplacer.create(item).setDisplayName(this.rewardName).setLore(this.rewardLore).trimmed()
                .replace(WIN_LIMIT_AMOUNT, limitAmountLore)
                .replace(WIN_LIMIT_COOLDOWN, limitCooldownLore)
                .replace(WIN_LIMIT_OUT, limitOutLore)
                .replace(WIN_LIMIT_NO_PERMISSION, limitPermissionLore)
                .replace(reward.getPlaceholders())
                .replace(crate.getPlaceholders())
                .replacePlaceholderAPI(player)
                .writeMeta();
            return item;
        });
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose(CRATE_NAME), MenuSize.CHEST_45);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        return new ArrayList<>();
    }

    @Override
    protected void loadAdditional() {
        this.hideExceededRewards = ConfigValue.create("Reward.Hide_Drained_Rewards",
            true,
            "Sets whether or not to hide rewards that player can not win anymore."
        ).read(cfg);

        this.rewardSlots = ConfigValue.create("Reward.Slots",
            new int[] {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34},
            "Sets slots to display crate rewards."
        ).read(cfg);

        this.rewardName = ConfigValue.create("Reward.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(REWARD_PREVIEW_NAME))
        ).read(cfg);

        this.rewardLore = ConfigValue.create("Reward.Lore.Default", Lists.newList(
            REWARD_PREVIEW_LORE,
            "",
            LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose("Chance: ") + REWARD_ROLL_CHANCE + "%"),
            LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose("Rarity: ") + REWARD_RARITY_NAME + " " + LIGHT_GRAY.enclose("(" + WHITE.enclose(REWARD_RARITY_ROLL_CHANCE + "%") + ")")),
            "",
            WIN_LIMIT_AMOUNT,
            WIN_LIMIT_COOLDOWN,
            WIN_LIMIT_OUT,
            WIN_LIMIT_NO_PERMISSION
        )).read(cfg);

        this.rewardLimitAmountLore = ConfigValue.create("Reward.Lore.Win_Limit.Amount", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_GREEN.enclose("✔") + " You can win this " + LIGHT_GREEN.enclose(GENERIC_AMOUNT) + " more times.")
        )).read(cfg);

        this.rewardLimitCoolownLore = ConfigValue.create("Reward.Lore.Win_Limit.Cooldown", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_ORANGE.enclose("[❗]") + " You can win this again in " + LIGHT_ORANGE.enclose(GENERIC_TIME) + ".")
        )).read(cfg);

        this.rewardLimitOutLore = ConfigValue.create("Reward.Lore.Win_Limit.Drained", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_RED.enclose("✘") + " Sorry, you can not win this " + LIGHT_RED.enclose("anymore") + ".")
        )).read(cfg);

        this.rewardLimitBadPermissionLore = ConfigValue.create("Reward.Lore.Win_Limit.No_Permission", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_RED.enclose("✘") + " Sorry, you can not win this.")
        )).read(cfg);

    }

    public int[] getRewardSlots() {
        return rewardSlots;
    }
}
