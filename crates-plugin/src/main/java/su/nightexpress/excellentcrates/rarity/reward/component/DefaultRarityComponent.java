package su.nightexpress.excellentcrates.rarity.reward.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;

@NullMarked
public class DefaultRarityComponent implements RarityComponent {

    private boolean    enabled;
    private Identifier rarityId;

    public DefaultRarityComponent(boolean enabled, Identifier rarityId) {
        this.rarityId = rarityId;
        this.enabled = enabled;
    }

    @Override
    public Identifier getRarityId() {
        return rarityId;
    }

    @Override
    public void setRarityId(Identifier rarityId) {
        this.rarityId = rarityId;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
