package su.nightexpress.excellentcrates.keys.cost.evaluator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.type.CostType;

@NullMarked
public class KeyCostType implements CostType<KeyCostOption> {

    private static final Identifier ID = new Identifier("key");

    private final KeyCostLogicProvider   logic;
    private final KeyCostDisplayProvider display;

    public KeyCostType(KeyCostLogicProvider logic, KeyCostDisplayProvider display) {
        this.logic = logic;
        this.display = display;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public KeyCostDisplayProvider getDisplay() {
        return this.display;
    }

    @Override
    public KeyCostLogicProvider getLogic() {
        return this.logic;
    }
}
