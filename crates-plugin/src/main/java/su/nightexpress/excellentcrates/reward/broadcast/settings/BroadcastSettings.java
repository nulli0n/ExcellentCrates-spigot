package su.nightexpress.excellentcrates.reward.broadcast.settings;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.broadcast.message.BroadcastMessage;
import su.nightexpress.excellentcrates.reward.broadcast.message.codec.BroadcastMessageCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.sound.VanillaSound;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public record BroadcastSettings(BroadcastMessage defaultMessage,
                                Map<Identifier, BroadcastMessage> rarityMessages) {

    public static BroadcastSettings defaults() {
        return new BroadcastSettings(
            Schema.DEFAULT_MESSAGE.getDefaultValue(),
            Schema.RARITY_MESSAGES.getDefaultValue()
        );
    }

    public static BroadcastSettings loadFrom(FileConfig config) {
        BroadcastMessage defaultMessage = config.getOrSet(Schema.DEFAULT_MESSAGE);
        Map<Identifier, BroadcastMessage> rarityMessages = config.getOrSet(Schema.RARITY_MESSAGES);

        return new BroadcastSettings(defaultMessage, rarityMessages);
    }

    private static final class Schema {

        static final ConfigCodec<Map<Identifier, BroadcastMessage>> RARITY_MESSAGES_CODEC = ConfigCodecs.forMap(
            Identifier::new,
            Identifier::value,
            BroadcastMessageCodec.INSTANCE,
            LinkedHashMap::new
        );

        static final ConfigProperty<BroadcastMessage> DEFAULT_MESSAGE = ConfigProperty.of(
            BroadcastMessageCodec.INSTANCE,
            "default_message",
            Defaults.DEFAULT_MESSAGE,
            "Default broadcast message"
        );

        static final ConfigProperty<Map<Identifier, BroadcastMessage>> RARITY_MESSAGES = ConfigProperty.of(
            RARITY_MESSAGES_CODEC,
            "rarity_messages",
            Defaults.rarityMessages(),
            "Broadcast messages for specific rarities"
        );
    }

    private static final class Defaults {

        static final BroadcastMessage DEFAULT_MESSAGE = new BroadcastMessage(
            Lists.newList(
                TagWrappers.GRAY.wrap("Player " + TagWrappers.GOLD.wrap(CommonPlaceholders.PLAYER_DISPLAY_NAME) +
                    " won " +
                    TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_NAME) + " from " +
                    TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + "!")
            ),
            VanillaSound.of(Sound.BLOCK_NOTE_BLOCK_BELL),
            true
        );

        static Map<Identifier, BroadcastMessage> rarityMessages() {
            Map<Identifier, BroadcastMessage> messages = new HashMap<>();

            messages.put(new Identifier("common"), new BroadcastMessage(
                Lists.newList(
                    TagWrappers.GRAY.wrap("Player " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_DISPLAY_NAME) +
                        " found " +
                        TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_NAME) + " in " +
                        TagWrappers.GRAY.wrap(SharedPlaceholders.CRATE_NAME) + ".")
                ),
                VanillaSound.of(Sound.ENTITY_ITEM_PICKUP),
                true
            ));

            messages.put(new Identifier("uncommon"), new BroadcastMessage(
                Lists.newList(
                    TagWrappers.GRAY.wrap("Player " + TagWrappers.GREEN.wrap(CommonPlaceholders.PLAYER_DISPLAY_NAME) +
                        " got an uncommon " +
                        TagWrappers.GREEN.wrap(SharedPlaceholders.REWARD_NAME) + " from " +
                        TagWrappers.GRAY.wrap(SharedPlaceholders.CRATE_NAME) + "!")
                ),
                VanillaSound.of(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
                true
            ));

            messages.put(new Identifier("rare"), new BroadcastMessage(
                Lists.newList(
                    TagWrappers.GRAY.wrap("Player " + TagWrappers.AQUA.wrap(CommonPlaceholders.PLAYER_DISPLAY_NAME) +
                        " unlocked a rare " +
                        TagWrappers.BLUE.wrap(SharedPlaceholders.REWARD_NAME) + " from " +
                        TagWrappers.GRAY.wrap(SharedPlaceholders.CRATE_NAME) + "!")
                ),
                VanillaSound.of(Sound.BLOCK_NOTE_BLOCK_CHIME),
                true
            ));

            messages.put(new Identifier("epic"), new BroadcastMessage(
                Lists.newList(
                    TagWrappers.LIGHT_PURPLE.wrap("Awesome! Player " + TagWrappers.GOLD.wrap(
                        CommonPlaceholders.PLAYER_DISPLAY_NAME) +
                        " just won the epic " +
                        TagWrappers.PINK.wrap(SharedPlaceholders.REWARD_NAME) + " from " +
                        TagWrappers.LIGHT_PURPLE.wrap(SharedPlaceholders.CRATE_NAME) + "!")
                ),
                VanillaSound.of(Sound.ENTITY_PLAYER_LEVELUP),
                true
            ));

            String yellowBundle = TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.YELLOW_BUNDLE))
                .repeat(3);

            messages.put(new Identifier("legendary"), new BroadcastMessage(
                Lists.newList(
                    TagWrappers.GOLD.wrap(yellowBundle + " LEGENDARY DROP " + yellowBundle),
                    TagWrappers.YELLOW.wrap("Player " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_DISPLAY_NAME) +
                        " has just obtained the legendary " +
                        TagWrappers.GOLD.wrap(SharedPlaceholders.REWARD_NAME) + " from " +
                        TagWrappers.YELLOW.wrap(SharedPlaceholders.CRATE_NAME) + "!!!")
                ),
                VanillaSound.of(Sound.UI_TOAST_CHALLENGE_COMPLETE),
                true
            ));

            return messages;
        }
    }
}
