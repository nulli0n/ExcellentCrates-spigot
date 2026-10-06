package su.nightexpress.excellentcrates.api.rarity.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;

@NullMarked
public interface RarityComponent extends RewardComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    Identifier getRarityId();

    void setRarityId(Identifier id);
}
