package su.nightexpress.excellentcrates.cost.core;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.type.CostTypeOption;

@NullMarked
public record DefaultCostTypeOption(Identifier typeId, String optionId) implements CostTypeOption {

}
