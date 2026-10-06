package su.nightexpress.excellentcrates.keys.cost.component.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;

@NullMarked
public class StandardKeyRequirementEntry implements KeyRequirementEntry {

    private int amount;

    public StandardKeyRequirementEntry(int amount) {
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
