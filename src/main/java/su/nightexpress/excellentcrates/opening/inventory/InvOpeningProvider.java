package su.nightexpress.excellentcrates.opening.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.inventory.impl.NormalInvOpening;
import su.nightexpress.excellentcrates.opening.inventory.impl.SelectionInvOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.*;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.AnimationProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.RewardProvider;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;
import java.util.*;

public class InvOpeningProvider extends AbstractFileData<CratesPlugin> implements OpeningProvider {

    private MenuType invType;
    private String   invTitle;

    private InvOpeningType mode;
    private int[]          winSlots;
    private long           maxTicksForSkip;
    private long           completionPauseTicks;

    private int       selectionAmount;
    private int[]     selectionSlots;
    private NightItem selectionOriginIcon;
    private NightItem selectionClickedIcon;

    private final Map<String, MenuItem> defaultItems;

    private final Map<SpinnerType, Map<String, SpinnerData>>     spinnerDataMap;
    private final Map<SpinnerType, Map<String, SpinnerProvider>> spinnerProviderMap;

    public InvOpeningProvider(@NotNull CratesPlugin plugin, @NotNull File file) {
        super(plugin, file);
        this.defaultItems = new HashMap<>();
        this.spinnerDataMap = new HashMap<>();
        this.spinnerProviderMap = new HashMap<>();
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        this.invType = BukkitThing.getMenuType(ConfigValue.create("Settings.Menu_Type", BukkitThing.getValue(MenuType.GENERIC_9X3)).read(config));

        this.invTitle = ConfigValue.create("Settings.Title", "Opening " + Placeholders.CRATE_NAME).read(config);

        this.mode = ConfigValue.create("Settings.Mode", InvOpeningType.class, InvOpeningType.NORMAL).read(config);

        this.winSlots = ConfigValue.create("Settings.WinSlots", new int[0]).read(config);

        this.maxTicksForSkip = ConfigValue.create("Settings.Max_Ticks_To_Skip",
            40L,
            "Sets max. amount of the opening ticks while players can skip the opening animation.",
            "Set to -1 to disable (no skip)."
        ).read(config);

        this.completionPauseTicks = ConfigValue.create("Settings.Completion_Pause_Ticks",
            20,
            "Sets how soon (in ticks) animation GUI will be closed when completed.",
            "[1 second = 20 ticks]",
            "[Default is 20 ticks]"
        ).read(config);

        if (this.mode == InvOpeningType.SELECTION) {
            this.selectionAmount = ConfigValue.create("Settings.Selection.Amount", 1).read(config);

            this.selectionSlots = ConfigValue.create("Settings.Selection.Slots", new int[0]).read(config);

            this.selectionOriginIcon = ConfigValue.create("Settings.Selection.Item_Original",
                new NightItem(Material.ENDER_CHEST)
            ).read(config);

            this.selectionClickedIcon = ConfigValue.create("Settings.Selection.Item_Selected",
                new NightItem(Material.CHEST)
            ).read(config);
        }

        config.getSection("Content.Default").forEach(sId -> {
            NightItem item = config.getCosmeticItem("Content.Default." + sId + ".Item");
            int[] slots = config.getIntArray("Content.Default." + sId + ".Slots");

            this.defaultItems.put(sId.toLowerCase(), item.toMenuItem().setSlots(slots).build());
        });

        for (SpinnerType type : SpinnerType.values()) {
            String runPath = "Settings.RunOnLaunch." + type.name();
            String settingsPath = "Spinners." + type.name();

            config.getSection(runPath).forEach(sId -> {
                SpinnerData data = SpinnerData.read(config, runPath + "." + sId);
                if (data == null) return;

                this.spinnerDataMap.computeIfAbsent(type, k -> new HashMap<>()).put(sId.toLowerCase(), data);
            });

            config.getSection(settingsPath).forEach(sId -> {
                SpinnerProvider provider = switch (type) {
                    case REWARD -> RewardProvider.read(config, settingsPath + "." + sId);
                    case ANIMATION -> AnimationProvider.read(config, settingsPath + "." + sId);
                };

                this.spinnerProviderMap.computeIfAbsent(type, k -> new HashMap<>()).put(sId.toLowerCase(), provider);
            });
        }

        return true;
    }

    @Override
    @NotNull
    public InventoryOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        InventoryView view = this.invType.typed().create(player, NightMessage.asLegacy(source.getCrate().replacePlaceholders().apply(this.invTitle)));

        return switch (this.mode) {
            case SELECTION -> new SelectionInvOpening(this.plugin, this, view, player, source, key);
            case NORMAL -> new NormalInvOpening(this.plugin, this, view, player, source, key);
        };
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        config.set("Settings.Menu_Type", BukkitThing.getValue(this.invType));
        config.set("Settings.Title", this.invTitle);
        config.set("Settings.Mode", this.mode.name());
        config.setIntArray("Settings.WinSlots", this.winSlots);
        config.set("Settings.Max_Ticks_To_Skip", this.maxTicksForSkip);
        config.set("Settings.Completion_Pause_Ticks", this.completionPauseTicks);

        if (this.mode == InvOpeningType.SELECTION) {
            config.set("Settings.Selection.Amount", this.selectionAmount);
            config.setIntArray("Settings.Selection.Slots", this.selectionSlots);
            config.set("Settings.Selection.Item_Original", this.selectionOriginIcon);
            config.set("Settings.Selection.Item_Selected", this.selectionClickedIcon);
        }

        config.remove("Content");

        this.defaultItems.forEach((id, menuItem) -> {
            config.set("Content.Default." + id + ".Item", menuItem.getItem());
            config.setIntArray("Content.Default." + id + ".Slots", menuItem.getSlots());
        });

        this.spinnerDataMap.forEach((type, map) -> {
            String path = "Settings.RunOnLaunch." + type.name();

            config.remove(path);

            map.forEach((id, data) -> {
                config.set(path + "." + id, data);
            });
        });

        this.spinnerProviderMap.forEach((type, map) -> {
            String settingsPath = "Spinners." + type.name();

            config.remove(settingsPath);

            map.forEach((id, settings) -> {
                config.set(settingsPath + "." + id, settings);
            });
        });
    }

    @NotNull
    public Set<SpinnerData> getSpinnersToRun(@NotNull SpinnerType type) {
        return new HashSet<>(this.spinnerDataMap.getOrDefault(type, Collections.emptyMap()).values());
    }

    @Nullable
    public SpinnerProvider getSpinnerProvider(@NotNull SpinnerType type, @NotNull String id) {
        return this.spinnerProviderMap.getOrDefault(type, Collections.emptyMap()).get(id.toLowerCase());
    }

    public void setInvType(@NotNull MenuType invType) {
        this.invType = invType;
    }

    public void setInvTitle(@NotNull String invTitle) {
        this.invTitle = invTitle;
    }

    @NotNull
    public InvOpeningType getMode() {
        return this.mode;
    }

    public void setMode(@NotNull InvOpeningType mode) {
        this.mode = mode;
    }

    public int[] getWinSlots() {
        return this.winSlots;
    }

    public void setWinSlots(int[] winSlots) {
        this.winSlots = winSlots;
    }

    public long getMaxTicksForSkip() {
        return this.maxTicksForSkip;
    }

    public void setMaxTicksForSkip(long maxTicksForSkip) {
        this.maxTicksForSkip = maxTicksForSkip;
    }

    public long getCompletionPauseTicks() {
        return this.completionPauseTicks;
    }

    public void setCompletionPauseTicks(long completionPauseTicks) {
        this.completionPauseTicks = completionPauseTicks;
    }

    public int getSelectionAmount() {
        return this.selectionAmount;
    }

    public void setSelectionAmount(int selectionAmount) {
        this.selectionAmount = selectionAmount;
    }

    public int[] getSelectionSlots() {
        return this.selectionSlots;
    }

    public void setSelectionSlots(int[] selectionSlots) {
        this.selectionSlots = selectionSlots;
    }

    public NightItem getSelectionOriginIcon() {
        return this.selectionOriginIcon;
    }

    public void setSelectionOriginIcon(NightItem selectionOriginIcon) {
        this.selectionOriginIcon = selectionOriginIcon;
    }

    public NightItem getSelectionClickedIcon() {
        return this.selectionClickedIcon;
    }

    public void setSelectionClickedIcon(NightItem selectionClickedIcon) {
        this.selectionClickedIcon = selectionClickedIcon;
    }

    @NotNull
    public Map<String, MenuItem> getDefaultItems() {
        return this.defaultItems;
    }

    @NotNull
    public Map<SpinnerType, Map<String, SpinnerData>> getSpinnerDataMap() {
        return this.spinnerDataMap;
    }

    @NotNull
    public Map<SpinnerType, Map<String, SpinnerProvider>> getSpinnerProviderMap() {
        return this.spinnerProviderMap;
    }
}
