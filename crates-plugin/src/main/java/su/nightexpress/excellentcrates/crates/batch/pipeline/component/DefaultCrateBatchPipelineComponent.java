package su.nightexpress.excellentcrates.crates.batch.pipeline.component;

import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;

@NullMarked
public class DefaultCrateBatchPipelineComponent implements BatchPipelineComponent {

    private int maxAllowed;
    private int selectedAmount;

    public DefaultCrateBatchPipelineComponent(int maxAllowed) {
        this.maxAllowed = maxAllowed;
        this.selectedAmount = 0;
    }

    @Override
    public Map<String, String> getLogData() {
        return Map.of(
            "maxAllowed", String.valueOf(this.maxAllowed),
            "selectedAmount", String.valueOf(this.selectedAmount)
        );
    }

    @Override
    public int getMaxAllowed() {
        return this.maxAllowed;
    }

    @Override
    public void limitMaxAllowed(int limit) {
        this.maxAllowed = Math.min(this.maxAllowed, limit);
    }

    @Override
    public int getSelectedAmount() {
        return this.selectedAmount;
    }

    @Override
    public void setSelectedAmount(int amount) {
        this.selectedAmount = Math.max(1, amount);
    }
}
