package su.nightexpress.excellentcrates.crate.cost.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.cost.CostEntry;
import su.nightexpress.excellentcrates.api.cost.CostType;
import su.nightexpress.excellentcrates.registry.CratesRegistries;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.config.FileConfig;

public abstract class AbstractCostEntry<T extends CostType> implements CostEntry {

    protected final T type;

    public AbstractCostEntry(@NotNull T type) {
        this.type = type;
    }

    @NotNull
    public static CostEntry read(@NotNull FileConfig config, @NotNull String path) throws IllegalStateException {
        String typeId = config.getOrSet(path + ".Type", ConfigCodecs.STRING, "null");
        CostType type = CratesRegistries.COST_TYPE.byKey(typeId);
        if (type == null) throw new IllegalStateException("Unkwnown cost type: " + typeId);

        return type.load(config, path);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Type", this.type.getId());
        this.writeAdditional(config, path);
    }

    protected abstract void writeAdditional(@NotNull FileConfig config, @NotNull String path);

    @Override
    @NotNull
    public T getType() {
        return this.type;
    }
}
