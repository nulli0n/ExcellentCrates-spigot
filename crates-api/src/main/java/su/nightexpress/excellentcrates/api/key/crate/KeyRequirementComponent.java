package su.nightexpress.excellentcrates.api.key.crate;

import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface KeyRequirementComponent extends CrateComponent {

    boolean hasKeyEntries();

    void addKeyEntry(Identifier keyId, KeyRequirementEntry entry);

    void removeKeyEntry(Identifier keyId);

    Map<Identifier, KeyRequirementEntry> getKeyEntryMap();

    @Nullable
    KeyRequirementEntry getKeyEntry(Identifier keyId);

    boolean isEnabled();

    void setEnabled(boolean enabled);

}
