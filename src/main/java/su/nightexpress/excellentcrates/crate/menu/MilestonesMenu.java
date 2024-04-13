package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.ItemOptions;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class MilestonesMenu extends ConfigMenu<CratesPlugin> implements AutoFilled<Milestone>, Linked<CrateSource> {

    private static final String PLACEHOLDER_OPENINGS_LEFT = "%openings_left%";

    private String       mileCompName;
    private List<String> mileCompLore;
    private String       mileIncName;
    private List<String> mileIncLore;
    private int[]        mileSlots;
    private ItemStack    mileCompItem;
    private ItemStack    mileIncItem;

    private boolean   pointerEnabled;
    private int       pointerPerMile;
    private int[]     pointerSlots;
    private ItemStack pointerComp;
    private ItemStack pointerInc;

    private final ItemHandler returnHandler;
    private final ViewLink<CrateSource> link;

    public MilestonesMenu(@NotNull CratesPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.FILE_MILESTONES));
        this.link = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            CrateSource source = this.getLink(viewer.getPlayer());
            this.runNextTick(() -> {
                plugin.getCrateManager().previewCrate(viewer.getPlayer(), source);
            });
        }));

        this.load();
    }

    @Override
    protected @NotNull MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Crate Milestones"), 27,  InventoryType.CHEST);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        list.add(new MenuItem(border).setSlots(0,1,2,3,4,5,6,7,8,18,19,20,21,22,23,24,25,26).setPriority(0));

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        list.add(new MenuItem(background).setSlots(9,10,11,12,13,14,15,16,17).setPriority(0));

        ItemStack back = ItemUtil.getSkinHead(SKIN_ARROW_DOWN);
        ItemUtil.editMeta(back, meta -> {
            meta.setDisplayName(CoreLang.EDITOR_ITEM_RETURN.getDefaultName());
        });
        list.add(new MenuItem(back).setSlots(22).setPriority(10).setHandler(this.returnHandler));

        ItemStack nextPage = ItemUtil.getSkinHead(SKIN_ARROW_RIGHT);
        ItemUtil.editMeta(nextPage, meta -> {
            meta.setDisplayName(CoreLang.EDITOR_ITEM_NEXT_PAGE.getDefaultName());
        });
        list.add(new MenuItem(nextPage).setSlots(17).setPriority(10).setHandler(ItemHandler.forNextPage(this)));

        ItemStack backPage = ItemUtil.getSkinHead(SKIN_ARROW_LEFT);
        ItemUtil.editMeta(backPage, meta -> {
            meta.setDisplayName(CoreLang.EDITOR_ITEM_PREVIOUS_PAGE.getDefaultName());
        });
        list.add(new MenuItem(backPage).setSlots(17).setPriority(10).setHandler(ItemHandler.forPreviousPage(this)));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.mileCompName = ConfigValue.create("Milestones.Completed.Name",
            LIGHT_GREEN.enclose(BOLD.enclose(MILESTONE_OPENINGS + " Openings")) + " " + GRAY.enclose("(" + WHITE.enclose("Completed") + ")")
        ).read(cfg);

        this.mileCompLore = ConfigValue.create("Milestones.Completed.Lore", Lists.newList(
            LIGHT_GREEN.enclose(BOLD.enclose("Info:")),
            LIGHT_GREEN.enclose("▪ " + LIGHT_GRAY.enclose("Reward: ") + REWARD_NAME),
            "",
            LIGHT_GREEN.enclose("✔") + " " + LIGHT_GRAY.enclose("You have completed this milestone.")
        )).read(cfg);

        this.mileIncName = ConfigValue.create("Milestones.Incompleted.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(MILESTONE_OPENINGS + " Openings")) + " " + GRAY.enclose("(" + WHITE.enclose("Incompleted") + ")")
        ).read(cfg);

        this.mileIncLore = ConfigValue.create("Milestones.Incompleted.Lore", Lists.newList(
            LIGHT_YELLOW.enclose(BOLD.enclose("Info:")),
            LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose("Openings Left: ") + PLACEHOLDER_OPENINGS_LEFT),
            LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose("Reward: ") + REWARD_NAME),
            "",
            LIGHT_RED.enclose("✘") + " " + LIGHT_GRAY.enclose("You haven''t completed this milestone yet.")
        )).read(cfg);

        this.mileSlots = ConfigValue.create("Milestones.Slots", new int[]{10,12,14,16}).read(cfg);

        if (ConfigValue.create("Milestones.Completed.Custom_Item.Enabled", true).read(cfg)) {
            this.mileCompItem = ConfigValue.create("Milestones.Completed.Custom_Item",
                ItemUtil.getSkinHead(SKIN_CHECK_MARK)
            ).read(cfg);
        }

        if (ConfigValue.create("Milestones.Incompleted.Custom_Item.Enabled", false).read(cfg)) {
            this.mileIncItem = ConfigValue.create("Milestones.Incompleted.Custom_Item",
                ItemUtil.getSkinHead(SKIN_WRONG_MARK)
            ).read(cfg);
        }

        this.pointerEnabled = ConfigValue.create("Milestones.Pointer.Enabled", true).read(cfg);

        this.pointerPerMile = ConfigValue.create("Milestones.Pointer.Per_Milestone", 2).read(cfg);

        this.pointerSlots = ConfigValue.create("Milestones.Pointer.Slots", new int[]{1,19,3,21,5,23,7,25}).read(cfg);

        this.pointerComp = ConfigValue.create("Milestones.Pointer.Completed",
            new ItemStack(Material.LIME_STAINED_GLASS_PANE)
        ).read(cfg);

        this.pointerInc = ConfigValue.create("Milestones.Pointer.Incompleted",
            new ItemStack(Material.WHITE_STAINED_GLASS_PANE)
        ).read(cfg);
    }

    @Override
    @NotNull
    public ViewLink<CrateSource> getLink() {
        return this.link;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        CrateSource source = this.getLink(viewer.getPlayer());

        options.setTitle(source.getCrate().replacePlaceholders().apply(options.getTitle()));

        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Milestone> autoFill) {
        CrateSource source = this.getLink(viewer.getPlayer());
        if (source == null) return;

        Player player = viewer.getPlayer();
        AtomicInteger counter = new AtomicInteger();
        Crate crate = source.getCrate();
        CrateUser user = plugin.getUserManager().getUserData(player);

        autoFill.setSlots(this.mileSlots);
        autoFill.setItems(crate.getMilestones().stream().sorted(Comparator.comparing(Milestone::getOpenings)).toList());
        autoFill.setItemCreator(milestone -> {
            Reward reward = milestone.getReward();
            if (reward == null) return new ItemStack(Material.AIR);

            //Set<Milestone> milestones = crate.getMilestones();
            int openings = user.getMilestones(crate);
            boolean isCompleted = openings >= milestone.getOpenings();
            String name;
            List<String> lore;
            ItemStack item = reward.getPreview();

            if (this.pointerEnabled) {
                //int counter = link.counter.getAndIncrement();

                ItemStack pointerItem = new ItemStack(isCompleted ? this.pointerComp : this.pointerInc);
                int[] pointerSlots = new int[this.pointerPerMile];
                int start = this.pointerPerMile * counter.getAndIncrement();
                System.arraycopy(this.pointerSlots, start, pointerSlots, 0, this.pointerPerMile);

                MenuItem menuItem = new MenuItem(pointerItem, pointerSlots);
                menuItem.setOptions(ItemOptions.personalWeak(player));
                menuItem.setPriority(Integer.MAX_VALUE);
                this.addItem(menuItem);
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

            ItemReplacer.create(item).hideFlags().trimmed().setDisplayName(name).setLore(lore)
                .replace(crate.getPlaceholders())
                .replace(reward.getPlaceholders())
                .replace(milestone.getPlaceholders())
                .replace(PLACEHOLDER_OPENINGS_LEFT, NumberUtil.format(milestone.getOpenings() - openings))
                .writeMeta();

            return item;
        });
    }
}
