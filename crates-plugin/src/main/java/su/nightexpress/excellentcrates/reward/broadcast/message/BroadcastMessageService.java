package su.nightexpress.excellentcrates.reward.broadcast.message;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.reward.broadcast.settings.BroadcastSettings;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class BroadcastMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastMessageService.class);

    private final ReadOnlySettings<BroadcastSettings> settings;

    public BroadcastMessageService(ReadOnlySettings<BroadcastSettings> settings) {
        this.settings = settings;
    }

    private @Nullable Identifier getRarityId(Reward reward) {
        RarityComponent rarityComponent = reward.getComponentOrNull(RewardComponentKeys.RARITY);
        if (rarityComponent != null && rarityComponent.isEnabled()) {
            return rarityComponent.getRarityId();
        }
        return null;
    }

    public void broadcast(Player player, Crate crate, Reward reward, PlaceholderContext placeholders) {
        BroadcastMessage message = settings.get().defaultMessage();

        Identifier rarityId = this.getRarityId(reward);
        if (rarityId != null) {
            BroadcastMessage rarityMessage = settings.get().rarityMessages().get(rarityId);
            if (rarityMessage != null) {
                message = rarityMessage;
            }
            else {
                LOGGER.warn("No broadcast message found for rarity id: '{}' of reward '{}'", rarityId, reward
                    .getId());
            }
        }

        List<String> text = placeholders.apply(message.getText());
        List<NightComponent> components = Lists.modify(text, NightMessage::parse);
        boolean playSound = message.isPlaySound();
        NightSound sound = message.getSound();

        Players.getOnline().forEach(online -> {
            components.forEach(component -> Players.sendMessage(online, component));
            if (playSound) {
                sound.play(online);
            }
        });
    }
}
