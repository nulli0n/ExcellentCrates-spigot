package su.nightexpress.excellentcrates.api.cost.type;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public interface CostTypeOption {

    Identifier typeId();

    String optionId();
}
