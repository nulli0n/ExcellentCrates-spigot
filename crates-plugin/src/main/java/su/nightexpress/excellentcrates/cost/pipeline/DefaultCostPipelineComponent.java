package su.nightexpress.excellentcrates.cost.pipeline;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.pipeline.CostPipelineComponent;

@NullMarked
public class DefaultCostPipelineComponent implements CostPipelineComponent {

    private final Identifier costTypeId;
    private final String     costOptionId;

    public DefaultCostPipelineComponent(Identifier costTypeId, String costOptionId) {
        this.costTypeId = costTypeId;
        this.costOptionId = costOptionId;
    }

    @Override
    public Identifier getCostTypeId() {
        return this.costTypeId;
    }

    @Override
    public String getCostOptionId() {
        return this.costOptionId;
    }
}
