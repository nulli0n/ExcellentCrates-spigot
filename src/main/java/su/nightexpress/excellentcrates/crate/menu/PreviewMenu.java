package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class PreviewMenu extends LinkedMenu<CratesPlugin, CrateSource> implements Filled<Reward>, ConfigBased {

    private static final String NO_PERMISSION = "%no_permission%";
    private static final String LIMITS        = "%limits%";
    private static final String PERSONAL_LIMITS = "%personal_limits%";
    private static final String SERVER_LIMITS   = "%server_limits%";

    private int[]        rewardSlots;
    private String       rewardName;
    private List<String> rewardLore;
    private List<String> noPermissionLore;
    private List<String> limitsLore;
    private List<String> personalLimitsLore;
    private List<String> serverLimitsLore;
    private boolean      hideUnavailable;

    public PreviewMenu(@NotNull CratesPlugin plugin, @NotNull FileConfig config) {
        super(plugin, MenuType.GENERIC_9X5, BLACK.wrap(CRATE_NAME));
        this.setApplyPlaceholderAPI(true);
        this.load(config);
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        CrateSource source = this.getLink(viewer);

        return source.getCrate().replacePlaceholders().apply(this.title);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    public MenuFiller<Reward> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Crate crate = this.getLink(player).getCrate();
        //CrateUser user = plugin.getUserManager().getUserData(player);
        //CrateData crateData = user.getCrateData(crate);

        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(this.rewardSlots);
        autoFill.setItems((this.hideUnavailable ? crate.getRewards(player) : crate.getRewards()).stream().filter(Reward::isRollable).toList());
        autoFill.setItemCreator(reward -> {
            List<String> restrictions = new ArrayList<>();
            List<String> limits = new ArrayList<>();

            if (reward.fitRequirements(player)) {
                if (reward.hasGlobalLimit() || reward.hasPersonalLimit()) {
                    List<String> personal = new ArrayList<>();
                    List<String> global = new ArrayList<>();

                    if (reward.hasGlobalLimit()) {
                        global = Replacer.create()
                            .replace(GENERIC_AMOUNT, String.valueOf(plugin.getCrateManager().getGlobalRollsLeft(reward)))
                            .replace(GENERIC_MAX, String.valueOf(reward.getGlobalLimits().getAmount()))
                            .apply(this.serverLimitsLore);
                    }
                    if (reward.hasPersonalLimit()) {
                        personal = Replacer.create()
                            .replace(GENERIC_AMOUNT, String.valueOf(plugin.getCrateManager().getPersonalRollsLeft(reward, player)))
                            .replace(GENERIC_MAX, String.valueOf(reward.getPlayerLimits().getAmount()))
                            .apply(this.personalLimitsLore);
                    }

                    limits.addAll(Replacer.create()
                        .replace(SERVER_LIMITS, global)
                        .replace(PERSONAL_LIMITS, personal)
                        .apply(this.limitsLore));
                }
            }
            else {
                restrictions.addAll(this.noPermissionLore);
            }

            return NightItem.fromItemStack(reward.getPreviewItem())
                .ignoreNameAndLore()
                .setDisplayName(this.rewardName)
                .setLore(this.rewardLore)
                .replacement(replacer -> {
                        replacer
                            .replace(LIMITS, limits)
                            .replace(NO_PERMISSION, restrictions)
                            .replace("%win_limit_amount%", limits)
                            .replace("%win_limit_cooldown%", Collections.emptyList())
                            .replace("%win_limit_drained%", Collections.emptyList())
                            .replace("%win_limit_no_permission%", restrictions)
                            .replace(reward.replacePlaceholders())
                            .replace(crate.replacePlaceholders());
                        if (this.applyPlaceholderAPI) {
                            replacer.replacePlaceholderAPI(player);
                        }
                    }
                );
        });

        return autoFill.build();
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.hideUnavailable = ConfigValue.create("Reward.Hide_Unavailable",
            true,
            "When enabled, displays only rewards that can be rolled out for a player."
        ).read(config);

        this.rewardSlots = ConfigValue.create("Reward.Slots",
            new int[] {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34}
        ).read(config);

        this.rewardName = ConfigValue.create("Reward.Name",
            REWARD_NAME
        ).read(config);

        this.rewardLore = ConfigValue.create("Reward.Lore.Default", Lists.newList(
            REWARD_DESCRIPTION,
            EMPTY_IF_ABOVE,
            LIMITS,
            NO_PERMISSION,
            EMPTY_IF_ABOVE,
            DARK_GRAY.wrap(WHITE.wrap(REWARD_RARITY_NAME) + " ┃┃ " + GREEN.wrap(REWARD_ROLL_CHANCE + "%"))
        )).read(config);

        this.noPermissionLore = ConfigValue.create("Reward.Lore.No_Permission", Lists.newList(
            GRAY.wrap(RED.wrap("✘") + " You don't have access to this reward.")
        )).read(config);

        this.limitsLore = ConfigValue.create("Reward.Lore.Limits.Info", Lists.newList(
            RED.wrap(BOLD.wrap("Limits:")),
            PERSONAL_LIMITS,
            SERVER_LIMITS
        )).read(config);

        this.personalLimitsLore = ConfigValue.create("Reward.Lore.Limits.Personal", Lists.newList(
            GRAY.wrap(RED.wrap("→") + " Your limit: " + RED.wrap(GENERIC_AMOUNT) + "/" + RED.wrap(GENERIC_MAX))
        )).read(config);

        this.serverLimitsLore = ConfigValue.create("Reward.Lore.Limits.Server", Lists.newList(
            GRAY.wrap(RED.wrap("→") + " Server limit: " + RED.wrap(GENERIC_AMOUNT) + "/" + RED.wrap(GENERIC_MAX))
        )).read(config);

        loader.addHandler(new ItemHandler("open", (viewer, event) -> {
            CrateSource source = this.getLink(viewer);
            if (!source.hasItem() || !source.hasBlock()) return;

            Player player = viewer.getPlayer();

            this.runNextTick(() -> {
                player.closeInventory();
                plugin.getCrateManager().interactCrate(player, source.getCrate(), InteractType.CRATE_OPEN, source.getItem(), source.getBlock());
            });
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> {
            CrateSource source = this.getLink(viewer);
            return source.hasItem() || source.hasBlock();
        }).build()));

        // NOWA FUNKCJONALNOŚĆ
        loader.addHandler(new ItemHandler("mass_open", (viewer, event) -> {
            CrateSource source = this.getLink(viewer);
            Player player = viewer.getPlayer();
        
            this.runNextTick(() -> {
                player.closeInventory();
                plugin.getCrateManager().interactCrate(player, source.getCrate(), InteractType.CRATE_MASS_OPEN, source.getItem(), source.getBlock());
            });
        }, ItemOptions.builder().build()));
        // KONIEC NOWEJ FUNKCJONALNOŚCI

        loader.addDefaultItem(new NightItem(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem()
            .setSlots(1,2,3,5,6,7,9,18,27,17,26,35,37,38,39,40,41,42,43));

        loader.addDefaultItem(new NightItem(Material.GRAY_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem()
            .setSlots(0,4,8,36,44));

        loader.addDefaultItem(NightItem.asCustomHead("1daf09284530ce92ed2df2a62e1b05a11f1871f85ae559042844206d66c0b5b0")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Milestones")))
            .toMenuItem()
            .setPriority(10)
            .setSlots(4)
            .setHandler(new ItemHandler("milestones", (viewer, event) -> {
                this.runNextTick(() -> plugin.getCrateManager().openMilestones(viewer.getPlayer(), this.getLink(viewer)));
            }, ItemOptions.builder().setVisibilityPolicy(viewer -> this.getLink(viewer).getCrate().hasMilestones()).build()))
        );

        loader.addDefaultItem(MenuItem.buildExit(this, 40).setPriority(10));
        loader.addDefaultItem(MenuItem.buildNextPage(this, 26).setPriority(10));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 18).setPriority(10));
    }
}
