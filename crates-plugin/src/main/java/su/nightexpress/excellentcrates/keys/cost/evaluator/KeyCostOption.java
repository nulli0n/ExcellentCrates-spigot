package su.nightexpress.excellentcrates.keys.cost.evaluator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.type.CostOption;

@NullMarked
public class KeyCostOption implements CostOption {

    private final Identifier keyId;

    public KeyCostOption(Identifier keyId) {
        this.keyId = keyId;
    }

    @Override
    public String getIdentifier() {
        return keyId.value();
    }

    public Identifier getKeyId() {
        return keyId;
    }
}
