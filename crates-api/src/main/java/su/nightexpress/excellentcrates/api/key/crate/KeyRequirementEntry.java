package su.nightexpress.excellentcrates.api.key.crate;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface KeyRequirementEntry {

    int getAmount();

    void setAmount(int amount);
}
