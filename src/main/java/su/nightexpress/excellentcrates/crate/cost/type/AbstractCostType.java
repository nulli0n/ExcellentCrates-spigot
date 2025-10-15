package su.nightexpress.excellentcrates.crate.cost.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.cost.CostType;

public abstract class AbstractCostType implements CostType {

    protected final String id;

    public AbstractCostType(@NotNull String id) {
        this.id = id;
    }

    @Override
    @NotNull
    public String getId() {
        return this.id;
    }
}
