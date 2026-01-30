package su.nightexpress.excellentcrates.crate.cost;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.cost.CostEntry;
import su.nightexpress.excellentcrates.crate.cost.entry.AbstractCostEntry;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.problem.ProblemCollector;
import su.nightexpress.nightcore.util.problem.ProblemReporter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Cost implements Writeable {

    private final String          id;
    private final List<CostEntry> entries;

    private boolean enabled;
    private String name;
    private AdaptedItem icon;

    public Cost(@NotNull String id, boolean enabled, @NotNull String name, @NotNull AdaptedItem icon, @NotNull List<CostEntry> entries) {
        this.id = id;
        this.setEnabled(enabled);
        this.setName(name);
        this.setIcon(icon);
        this.entries = entries;
    }

    @NotNull
    public static Cost read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        boolean enabled = ConfigValue.create(path + ".Enabled", true).read(config);
        String name = ConfigValue.create(path + ".Name", StringUtil.capitalizeUnderscored(id)).read(config);
        AdaptedItem icon = ItemHelper.readOrPlaceholder(config, path + ".Icon");

        List<CostEntry> entries = new ArrayList<>();
        config.getSection(path + ".Entries").forEach(sId -> {
            try {
                CostEntry entry = AbstractCostEntry.read(config, path + ".Entries." + sId);
                entries.add(entry);
            }
            catch (IllegalStateException exception) {
                exception.printStackTrace();
            }
        });

        return new Cost(id, enabled, name, icon, entries);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Enabled", this.enabled);
        config.set(path + ".Name", this.name);
        config.set(path + ".Icon", this.icon);
        config.remove(path + ".Entries");
        for (int i = 0; i < this.entries.size(); i++) {
            config.set(path + ".Entries." + i, this.entries.get(i));
        }
    }

    @NotNull
    public ProblemReporter collectProblems() {
        ProblemReporter reporter = new ProblemCollector("Cost Option '" + this.id + "'", this.id);

        this.getEntries().forEach(entry -> {
            if (!entry.isValid()) {
                reporter.report("Entry with invalid setting(s): " + entry);
            }
        });

        return reporter;
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.COST.replacer(this);
    }

    public boolean canAfford(@NotNull Player player) {
        return this.entries.stream().allMatch(entry -> entry.hasEnough(player));
    }

    public void takeAll(@NotNull Player player) {
        this.entries.forEach(entry -> entry.take(player));
    }

    public void refundAll(@NotNull Player player) {
        this.entries.forEach(entry -> entry.refund(player));
    }

    public int countMaxOpenings(@NotNull Player player) {
        return this.entries.stream().mapToInt(entry -> entry.countPossibleOpenings(player)).max().orElse(0);
    }

    @NotNull
    public String formatInline(@NotNull String delimiter) {
        return this.entries.stream().map(CostEntry::format).collect(Collectors.joining(delimiter));
    }

    @NotNull
    public ItemStack getIconStack() {
        return ItemHelper.toItemStack(this.icon);
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public boolean isValid() {
        return !this.isEmpty() && this.entries.stream().anyMatch(CostEntry::isValid);
    }

    public boolean isAvailable() {
        return this.isEnabled() && this.isValid();
    }

    public boolean hasInvalids() {
        return this.entries.stream().anyMatch(Predicate.not(CostEntry::isValid));
    }

    public void addEntry(@NotNull CostEntry entry) {
        this.entries.add(entry);
    }

    public void removeEntry(@NotNull CostEntry entry) {
        this.entries.remove(entry);
    }

    public void removeEntry(int index) {
        if (index >= 0 && index < this.entries.size()) {
            this.entries.remove(index);
        }
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public AdaptedItem getIcon() {
        return this.icon;
    }

    public void setIcon(@NotNull AdaptedItem icon) {
        this.icon = icon;
    }

    @NotNull
    public List<CostEntry> getEntries() {
        return this.entries;
    }
}
