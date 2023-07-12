package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.menu.item.ItemOptions;
import su.nexmedia.engine.api.menu.item.MenuItem;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateReward;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.data.impl.CrateUser;

import java.util.*;

public class MilestonesMenu extends ConfigMenu<ExcellentCrates> implements AutoPaged<Milestone> {

    private static final String PLACEHOLDER_OPENINGS_LEFT = "%openings_left%";

    private final String       mileCompName;
    private final List<String> mileCompLore;
    private final String       mileIncName;
    private final List<String> mileIncLore;
    private final int[]        mileSlots;
    private ItemStack    mileCompItem;
    private ItemStack mileIncItem;

    private final boolean pointerEnabled;
    private       int     pointerPerMile;
    private int[] pointerSlots;
    private ItemStack pointerComp;
    private ItemStack pointerInc;

    private final Map<Player, Crate> crateMap;
    private final Map<Player, Integer> counter;

    public MilestonesMenu(@NotNull ExcellentCrates plugin) {
        super(plugin, JYML.loadOrExtract(plugin, Config.FILE_MILESTONES));
        this.crateMap = new WeakHashMap<>();
        this.counter = new WeakHashMap<>();

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CLOSE, ClickHandler.forClose(this))
            .addClick(MenuItemType.RETURN, (viewer, event) -> {
                Crate crate = this.getCrate(viewer.getPlayer());
                if (crate != null) crate.openPreview(viewer.getPlayer());
            })
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this));

        this.mileCompName = cfg.getString("Milestones.Completed.Name");
        this.mileCompLore = cfg.getStringList("Milestones.Completed.Lore");
        this.mileIncName = cfg.getString("Milestones.Incompleted.Name");
        this.mileIncLore = cfg.getStringList("Milestones.Incompleted.Lore");
        this.mileSlots = cfg.getIntArray("Milestones.Slots");
        if (cfg.getBoolean("Milestones.Completed.Custom_Item.Enabled")) {
            this.mileCompItem = cfg.getItem("Milestones.Completed.Custom_Item");
        }
        if (cfg.getBoolean("Milestones.Incompleted.Custom_Item.Enabled")) {
            this.mileIncItem = cfg.getItem("Milestones.Incompleted.Custom_Item");
        }

        if (this.pointerEnabled = cfg.getBoolean("Milestones.Pointer.Enabled")) {
            this.pointerPerMile = cfg.getInt("Milestones.Pointer.Per_Milestone");
            this.pointerSlots = cfg.getIntArray("Milestones.Pointer.Slots");
            this.pointerComp = cfg.getItem("Milestones.Pointer.Completed");
            this.pointerInc = cfg.getItem("Milestones.Pointer.Incompleted");
        }

        this.load();
    }

    public void open(@NotNull Player player, @NotNull Crate crate) {
        this.crateMap.put(player, crate);
        this.open(player, 1);
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        Crate crate = this.getCrate(viewer.getPlayer());
        if (crate == null) return;

        options.setTitle(crate.replacePlaceholders().apply(options.getTitle()));

        this.counter.put(viewer.getPlayer(), 0);

        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Nullable
    public Crate getCrate(@NotNull Player player) {
        return this.crateMap.get(player);
    }

    @Override
    public int[] getObjectSlots() {
        return this.mileSlots;
    }

    @Override
    @NotNull
    public List<Milestone> getObjects(@NotNull Player player) {
        Crate crate = this.getCrate(player);
        if (crate == null) return Collections.emptyList();

        return crate.getMilestones().stream().sorted(Comparator.comparing(Milestone::getOpenings)).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Milestone milestone) {
        Crate crate = this.getCrate(player);
        if (crate == null) return new ItemStack(Material.AIR);

        CrateReward reward = crate.getReward(milestone.getRewardId());
        if (reward == null) return new ItemStack(Material.AIR);

        Set<Milestone> milestones = crate.getMilestones();
        CrateUser user = plugin.getUserManager().getUserData(player);

        int openings = user.getMilestones(crate);
        boolean isCompleted = openings >= milestone.getOpenings();
        String name;
        List<String> lore;
        ItemStack item = reward.getPreview();

        if (this.pointerEnabled) {
            int counter = this.counter.get(player);

            ItemStack pointerItem = new ItemStack(isCompleted ? this.pointerComp : this.pointerInc);
            int[] pointerSlots = new int[this.pointerPerMile];
            int start = this.pointerPerMile * counter;
            System.arraycopy(this.pointerSlots, start, pointerSlots, 0, this.pointerPerMile);

            MenuItem menuItem = new MenuItem(pointerItem, pointerSlots);
            menuItem.setOptions(ItemOptions.personalWeak(player));
            menuItem.setPriority(Integer.MAX_VALUE);
            this.addItem(menuItem);

            this.counter.put(player, counter + 1);
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

        ItemUtil.mapMeta(item, meta -> {
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.values());
            ItemUtil.replace(meta, crate.replacePlaceholders());
            ItemUtil.replace(meta, reward.replacePlaceholders());
            ItemUtil.replace(meta, milestone.replacePlaceholders());
            ItemUtil.replace(meta, str -> Colorizer.apply(str
                .replace(PLACEHOLDER_OPENINGS_LEFT, NumberUtil.format(milestone.getOpenings() - openings))
            ));
        });

        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Milestone milestone) {
        return (viewer, event) -> {

        };
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);
        this.crateMap.remove(viewer.getPlayer());
        this.counter.remove(viewer.getPlayer());
    }
}
