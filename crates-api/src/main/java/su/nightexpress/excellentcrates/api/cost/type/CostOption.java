package su.nightexpress.excellentcrates.api.cost.type;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CostOption {

    /**
     * @return Unique identifier of the cost option within its provider (e.g., "common_key")
     */
    String getIdentifier();
}
