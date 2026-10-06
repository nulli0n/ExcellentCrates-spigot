package su.nightexpress.excellentcrates.keys.cost.component.model;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;

@NullMarked
public class DefaultKeyRequirementComponent implements KeyRequirementComponent {

    private final Map<Identifier, KeyRequirementEntry> keyEntryMap;

    private boolean enabled;

    public DefaultKeyRequirementComponent(Builder builder) {
        this.keyEntryMap = new HashMap<>(builder.keyEntryMap);
        this.enabled = builder.enabled;
    }

    public DefaultKeyRequirementComponent() {
        this(new Builder());
    }

    public static DefaultKeyRequirementComponent createDefault() {
        return new DefaultKeyRequirementComponent();
    }

    @Override
    public void addKeyEntry(Identifier keyId, KeyRequirementEntry entry) {
        this.keyEntryMap.put(keyId, entry);
    }

    @Override
    public void removeKeyEntry(Identifier keyId) {
        this.keyEntryMap.remove(keyId);
    }

    @Override
    public boolean hasKeyEntries() {
        return !this.keyEntryMap.isEmpty();
    }

    @Override
    public Map<Identifier, KeyRequirementEntry> getKeyEntryMap() {
        return keyEntryMap;
    }

    @Override
    public @Nullable KeyRequirementEntry getKeyEntry(Identifier keyId) {
        return this.keyEntryMap.get(keyId);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "KeyCostComponent [keyEntryMap=" + keyEntryMap + ", enabled=" + enabled + "]";
    }

    public static class Builder {

        private final Map<Identifier, KeyRequirementEntry> keyEntryMap;

        private boolean enabled;

        public Builder() {
            this.keyEntryMap = new HashMap<>();
            this.enabled = false;
        }

        public Builder setKeyEntryMap(Map<Identifier, KeyRequirementEntry> keyEntryMap) {
            this.keyEntryMap.clear();
            this.keyEntryMap.putAll(keyEntryMap);
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public DefaultKeyRequirementComponent build() {
            return new DefaultKeyRequirementComponent(this);
        }
    }
}
