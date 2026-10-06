package su.nightexpress.excellentcrates.api.cost.type;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;

@NullMarked
public interface CostType<T extends CostOption> extends Identifiable {

    CostLogicProvider<T> getLogic();

    CostDisplayProvider<T> getDisplay();
}
