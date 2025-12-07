package su.nightexpress.excellentcrates.opening.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerHolder;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerType;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.AnimationProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.RewardProvider;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryProvider extends AbstractProvider {

    private MenuType invType  = MenuType.GENERIC_9X3;
    private String   invTitle = "Crate opening...";

    private int[] winSlots             = {-1};
    private long  maxTicksForSkip      = 40;
    private long  completionPauseTicks = 40;

    private final Map<String, MenuItem>      defaultItems;
    private final Map<String, SpinnerHolder> spinners;

    public InventoryProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
        this.defaultItems = new HashMap<>();
        this.spinners = new HashMap<>();
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.invType = BukkitThing.getMenuType(ConfigValue.create("Settings.Menu_Type", BukkitThing.getValue(this.invType)).read(config));

        this.invTitle = ConfigValue.create("Settings.Title", this.invTitle).read(config);

        this.winSlots = ConfigValue.create("Settings.WinSlots", this.winSlots).read(config);

        this.maxTicksForSkip = ConfigValue.create("Settings.Max_Ticks_To_Skip",
            this.maxTicksForSkip,
            "Sets max. amount of the opening ticks while players can skip the opening animation.",
            "Set to -1 to disable (no skip)."
        ).read(config);

        this.completionPauseTicks = ConfigValue.create("Settings.Completion_Pause_Ticks",
            this.completionPauseTicks,
            "Sets how soon (in ticks) animation GUI will be closed when completed.",
            "[1 second = 20 ticks]",
            "[Default is 20 ticks]"
        ).read(config);

        if (config.getSection("Content.Default").isEmpty()) {
            this.defaultItems.forEach((id, menuItem) -> {
                config.set("Content.Default." + id + ".Item", menuItem.getItem());
                config.setIntArray("Content.Default." + id + ".Slots", menuItem.getSlots());
            });
        }

        this.defaultItems.clear();

        config.getSection("Content.Default").forEach(sId -> {
            NightItem item = config.getCosmeticItem("Content.Default." + sId + ".Item");
            int[] slots = config.getIntArray("Content.Default." + sId + ".Slots");

            this.defaultItems.put(sId.toLowerCase(), item.toMenuItem().setSlots(slots).build());
        });

        String configsPath = "Settings.RunOnLaunch";
        String providersPath = "Spinners";

        if (!config.contains(configsPath)) {
            this.spinners.forEach((id, holder) -> {
                config.set(configsPath + "." + holder.getType().name() + "." + holder.getId(), holder.getConfig());
            });
        }

        if (!config.contains(providersPath)) {
            this.spinners.forEach((id, holder) -> {
                config.set(providersPath + "." + holder.getType().name() + "." + holder.getConfig().getSpinnerId(), holder.getProvider());
            });
        }

        this.spinners.clear();

        for (SpinnerType type : SpinnerType.values()) {
            String typedConfigsPath = configsPath + "." + type.name();
            String typedProvidersPath = providersPath + "." + type.name();

            config.getSection(typedConfigsPath).forEach(sId -> {
                SpinnerData data = SpinnerData.read(config, typedConfigsPath + "." + sId);
                if (data == null) return;

                String spinnerId = data.getSpinnerId();
                String providerIdPath = typedProvidersPath + "." + spinnerId;
                if (!config.contains(providerIdPath)) {
                    this.plugin.error("Spinner '" + spinnerId + "' not present in the '" + config.getFile().getPath() + "' for the '" + sId + "' run configuration.");
                    return;
                }

                SpinnerProvider provider = switch (type) {
                    case REWARD -> RewardProvider.read(config, providerIdPath);
                    case ANIMATION -> AnimationProvider.read(config, providerIdPath);
                };

                SpinnerHolder holder = new SpinnerHolder(sId, type, data, provider);
                this.addSpinner(holder);
            });
        }
    }

    @Override
    @NotNull
    public InventoryOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        InventoryView view = this.invType.typed().create(player, NightMessage.asLegacy(source.getCrate().replacePlaceholders().apply(this.invTitle)));

        return new InventoryOpening(this.plugin, this, view, player, source, cost);
    }

    @NotNull
    public Set<SpinnerHolder> getSpinners() {
        return new HashSet<>(this.spinners.values());
    }

    @Nullable
    public SpinnerHolder getSpinner(@NotNull String id) {
        return this.spinners.get(id.toLowerCase());
    }

    public void addSpinner(@NotNull SpinnerHolder holder) {
        this.spinners.put(holder.getId(), holder);
    }

    public void setInvType(@NotNull MenuType invType) {
        this.invType = invType;
    }

    public void setInvTitle(@NotNull String invTitle) {
        this.invTitle = invTitle;
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

    @NotNull
    public Map<String, MenuItem> getDefaultItems() {
        return this.defaultItems;
    }
}
