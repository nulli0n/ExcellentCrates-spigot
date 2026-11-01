package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@SuppressWarnings("UnstableApiUsage")
public class MilestonesMenu extends LinkedMenu<CratesPlugin, CrateSource> implements Filled<Milestone>, ConfigBased {

    private static final String PLACEHOLDER_OPENINGS_LEFT = "%openings_left%";

    private String       mileCompName;
    private List<String> mileCompLore;
    private String       mileIncName;
    private List<String> mileIncLore;
    private int[]        mileSlots;
    private NightItem    mileCompItem;
    private NightItem    mileIncItem;

    private boolean   pointerEnabled;
    private int       pointerPerMile;
    private int[]     pointerSlots;
    private NightItem pointerComp;
    private NightItem pointerInc;

    public MilestonesMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X3, BLACK.wrap("Crate Milestones"));
        this.load(FileConfig.load(plugin.getDataFolder().toPath().resolve(Config.FILE_MILESTONES)));
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        CrateSource source = this.getLink(viewer.getPlayer());

        return source.getCrate().replacePlaceholders().apply(this.title);
    }

    @Override
    @NotNull
    public MenuFiller<Milestone> createFiller(@NotNull MenuViewer viewer) {
        var autoFill = MenuFiller.builder(this);

        CrateSource source = this.getLink(viewer.getPlayer());
        Player player = viewer.getPlayer();
        AtomicInteger counter = new AtomicInteger();
        Crate crate = source.getCrate();
        CrateUser user = plugin.getUserManager().getOrFetch(player);

        autoFill.setSlots(this.mileSlots);
        autoFill.setItems(crate.getMilestones().stream().sorted(Comparator.comparing(Milestone::getOpenings)).toList());
        autoFill.setItemCreator(milestone -> {
            Reward reward = milestone.getReward();
            if (reward == null) return new NightItem(Material.AIR);

            int openings = user.getCrateData(crate).getMilestone();
            boolean isCompleted = openings >= milestone.getOpenings();
            String name;
            List<String> lore;
            NightItem item = NightItem.fromItemStack(reward.getPreviewItem());

            if (this.pointerEnabled) {
                NightItem pointerItem = (isCompleted ? this.pointerComp : this.pointerInc).copy();
                int[] pointerSlots = new int[this.pointerPerMile];
                int start = this.pointerPerMile * counter.getAndIncrement();
                System.arraycopy(this.pointerSlots, start, pointerSlots, 0, this.pointerPerMile);

                MenuItem menuItem = MenuItem.builder(pointerItem).setSlots(pointerSlots).setPriority(MenuItem.HIGH_PRIORITY).build();
                this.addItem(viewer, menuItem);
            }

            if (isCompleted) {
                name = this.mileCompName;
                lore = new ArrayList<>(this.mileCompLore);
                if (this.mileCompItem != null) item = this.mileCompItem;
            }
            else {
                name = this.mileIncName;
                lore = new ArrayList<>(this.mileIncLore);
                if (this.mileIncItem != null) item = this.mileIncItem;
            }

            return item
                .copy()
                .setDisplayName(name)
                .setLore(lore)
                .hideAllComponents()
                .replacement(replacer -> replacer
                    .replace(crate.replacePlaceholders())
                    .replace(reward.replacePlaceholders())
                    .replace(milestone.replacePlaceholders())
                    .replace(PLACEHOLDER_OPENINGS_LEFT, NumberUtil.format(milestone.getOpenings() - openings))
                );
        });

        return autoFill.build();
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.mileCompName = config.getOrSet("Milestones.Completed.Name", ConfigCodecs.STRING,
            LIGHT_GREEN.wrap(BOLD.wrap(MILESTONE_OPENINGS + " Openings")) + " " + GRAY.wrap("(" + WHITE.wrap("Completed") + ")")
        );

        this.mileCompLore = config.getOrSet("Milestones.Completed.Lore", ConfigCodecs.STRING_LIST, Lists.newList(
            LIGHT_GREEN.wrap(BOLD.wrap("Info:")),
            LIGHT_GREEN.wrap("▪ " + LIGHT_GRAY.wrap("Reward: ") + REWARD_NAME),
            "",
            LIGHT_GREEN.wrap("✔") + " " + LIGHT_GRAY.wrap("You have completed this milestone.")
        ));

        this.mileIncName = config.getOrSet("Milestones.Incompleted.Name", ConfigCodecs.STRING,
            LIGHT_YELLOW.wrap(BOLD.wrap(MILESTONE_OPENINGS + " Openings")) + " " + GRAY.wrap("(" + WHITE.wrap("Incompleted") + ")")
        );

        this.mileIncLore = config.getOrSet("Milestones.Incompleted.Lore", ConfigCodecs.STRING_LIST, Lists.newList(
            LIGHT_YELLOW.wrap(BOLD.wrap("Info:")),
            LIGHT_YELLOW.wrap("▪ " + LIGHT_GRAY.wrap("Openings Left: ") + PLACEHOLDER_OPENINGS_LEFT),
            LIGHT_YELLOW.wrap("▪ " + LIGHT_GRAY.wrap("Reward: ") + REWARD_NAME),
            "",
            LIGHT_RED.wrap("✘") + " " + LIGHT_GRAY.wrap("You haven''t completed this milestone yet.")
        ));

        this.mileSlots = config.getOrSet("Milestones.Slots", ConfigCodecs.INT_ARRAY, new int[]{10,12,14,16});

        if (config.getOrSet("Milestones.Completed.Custom_Item.Enabled", ConfigCodecs.BOOLEAN, true)) {
            this.mileCompItem = config.getOrSet("Milestones.Completed.Custom_Item.Value", ConfigCodecs.NIGHT_ITEM,
                NightItem.asCustomHead(SKIN_CHECK_MARK)
            );
        }

        if (config.getOrSet("Milestones.Incompleted.Custom_Item.Enabled", ConfigCodecs.BOOLEAN, false)) {
            this.mileIncItem = config.getOrSet("Milestones.Incompleted.Custom_Item.Value", ConfigCodecs.NIGHT_ITEM,
                NightItem.asCustomHead(SKIN_WRONG_MARK)
            );
        }

        this.pointerEnabled = config.getOrSet("Milestones.Pointer.Enabled", ConfigCodecs.BOOLEAN, true);

        this.pointerPerMile = config.getOrSet("Milestones.Pointer.Per_Milestone", ConfigCodecs.INT, 2);

        this.pointerSlots = config.getOrSet("Milestones.Pointer.Slots", ConfigCodecs.INT_ARRAY, new int[]{1,19,3,21,5,23,7,25});

        this.pointerComp = config.getOrSet("Milestones.Pointer.Completed", ConfigCodecs.NIGHT_ITEM,
            new NightItem(Material.LIME_STAINED_GLASS_PANE)
        );

        this.pointerInc = config.getOrSet("Milestones.Pointer.Incompleted", ConfigCodecs.NIGHT_ITEM,
            new NightItem(Material.WHITE_STAINED_GLASS_PANE)
        );

        loader.addDefaultItem(MenuItem.buildReturn(this, 22, (viewer, event) -> {
            Player player = viewer.getPlayer();
            CrateSource source = this.getLink(player);
            this.plugin.runTask(player, () -> plugin.getCrateManager().previewCrate(player, source));
        }).setPriority(MenuItem.HIGH_PRIORITY));

        loader.addDefaultItem(MenuItem.buildNextPage(this, 17).setPriority(MenuItem.HIGH_PRIORITY));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 9).setPriority(MenuItem.HIGH_PRIORITY));

        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).toMenuItem()
            .setSlots(0,1,2,3,4,5,6,7,8,18,19,20,21,22,23,24,25,26));

        loader.addDefaultItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE).toMenuItem()
            .setSlots(9,10,11,12,13,14,15,16,17));
    }
}
