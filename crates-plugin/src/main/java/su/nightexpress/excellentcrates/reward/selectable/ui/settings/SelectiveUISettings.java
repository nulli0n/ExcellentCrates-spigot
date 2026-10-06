package su.nightexpress.excellentcrates.reward.selectable.ui.settings;

import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.text.layout.LayoutComponent;
import su.nightexpress.engine.text.layout.TextLayout;
import su.nightexpress.engine.text.layout.TextLayoutConstants;
import su.nightexpress.engine.text.layout.codec.TextLayoutCodec;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.ui.inventory.item.populator.SlotPattern;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.sound.VanillaSound;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public record SelectiveUISettings(String rewardName,
                                  TextLayout rewardPickLore,
                                  TextLayout rewardUnpickLore,
                                  int[] rewardSlots,
                                  SlotPattern selectedSlots,
                                  NightSound pickSound,
                                  NightSound unpickSound) {

    public static SelectiveUISettings defaults() {
        return new SelectiveUISettings(
            Schema.REWARD_NAME.getDefaultValue(),
            Schema.REWARD_LORE_PICK.getDefaultValue(),
            Schema.REWARD_LORE_UNPICK.getDefaultValue(),
            Schema.REWARD_DISPLAY_SLOTS.getDefaultValue(),
            Schema.REWARD_SELECTED_SLOTS.getDefaultValue(),
            Schema.REWARD_PICK_SOUND.getDefaultValue(),
            Schema.REWARD_UNPICK_SOUND.getDefaultValue()
        );
    }

    public static SelectiveUISettings loadFrom(FileConfig config) {
        String rewardName = config.getOrSet(Schema.REWARD_NAME);
        TextLayout rewardLorePick = config.getOrSet(Schema.REWARD_LORE_PICK);
        TextLayout rewardLoreUnpick = config.getOrSet(Schema.REWARD_LORE_UNPICK);
        int[] rewardSlots = config.getOrSet(Schema.REWARD_DISPLAY_SLOTS);
        SlotPattern selectedSlots = config.getOrSet(Schema.REWARD_SELECTED_SLOTS);
        NightSound pickSound = config.getOrSet(Schema.REWARD_PICK_SOUND);
        NightSound unpickSound = config.getOrSet(Schema.REWARD_UNPICK_SOUND);

        return new SelectiveUISettings(
            rewardName,
            rewardLorePick,
            rewardLoreUnpick,
            rewardSlots,
            selectedSlots,
            pickSound,
            unpickSound
        );
    }

    private static final class Schema {

        private static final int[]       DEFAULT_SLOTS          = IntStream.range(0, 27).toArray();
        private static final SlotPattern DEFAULT_SELECTED_SLOTS = new SlotPattern()
            .with(1, 40)
            .with(2, 39, 41)
            .with(3, 39, 40, 41)
            .with(4, 37, 39, 41, 43)
            .with(5, 38, 39, 40, 41, 42)
            .with(6, 37, 38, 39, 41, 42, 43)
            .with(7, 37, 38, 39, 40, 41, 42, 43)
            .with(8, 36, 37, 38, 39, 41, 42, 43, 44)
            .with(9, 36, 37, 38, 39, 40, 41, 42, 43, 44);

        private static final TextLayout DEFAULT_LORE_PICK = TextLayout.builder()
            .withTextTemplate(Lists.newList(
                "%rarity%",
                "%description%",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.BUNDLE) + " " +
                    TagWrappers.ORANGE.wrap("Total Available: ") + SharedPlaceholders.REWARD_QUOTA_THRESHOLD),
                TagWrappers.DARK_GRAY.and(TagWrappers.ITALIC).wrap("(A cooldown or limit may apply)"),
                "",
                TagWrappers.GREEN.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to select"))
            ))
            .withComponent("rarity", new LayoutComponent(
                SharedPlaceholders.REWARD_HAS_RARITY_MARKER,
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.AMETHYST_SHARD) + " " +
                    TagWrappers.GRAY.wrap("Rarity: ") + SharedPlaceholders.REWARD_RARITY) + TagWrappers.BR,
                ""
            ))
            .withComponent("description", new LayoutComponent(
                SharedPlaceholders.REWARD_DESCRIPTION,
                TextLayoutConstants.VALUE + TagWrappers.BR,
                ""
            ))
            .build();

        private static final TextLayout DEFAULT_LORE_UNPICK = TextLayout.builder()
            .withTextTemplate(Lists.newList(
                "%rarity%",
                "%description%",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.BUNDLE) + " " +
                    TagWrappers.ORANGE.wrap("Total Available: ") + SharedPlaceholders.REWARD_QUOTA_THRESHOLD),
                TagWrappers.DARK_GRAY.and(TagWrappers.ITALIC).wrap("(A cooldown or limit may apply)"),
                "",
                TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to deselect"))
            ))
            .withComponent("rarity", new LayoutComponent(
                SharedPlaceholders.REWARD_HAS_RARITY_MARKER,
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.AMETHYST_SHARD) + " " +
                    TagWrappers.GRAY.wrap("Rarity: ") + SharedPlaceholders.REWARD_RARITY) + TagWrappers.BR,
                ""
            ))
            .withComponent("description", new LayoutComponent(
                SharedPlaceholders.REWARD_DESCRIPTION,
                TextLayoutConstants.VALUE + TagWrappers.BR,
                ""
            ))
            .build();

        static final ConfigProperty<String> REWARD_NAME = ConfigProperty.of(
            ConfigCodecs.STRING,
            "reward.name",
            SharedPlaceholders.REWARD_NAME,
            ""
        );

        static final ConfigProperty<TextLayout> REWARD_LORE_PICK = ConfigProperty.of(
            TextLayoutCodec.INSTANCE,
            "reward.lore-pick",
            DEFAULT_LORE_PICK,
            ""
        );

        static final ConfigProperty<TextLayout> REWARD_LORE_UNPICK = ConfigProperty.of(
            TextLayoutCodec.INSTANCE,
            "reward.lore-unpick",
            DEFAULT_LORE_UNPICK,
            ""
        );

        static final ConfigProperty<int[]> REWARD_DISPLAY_SLOTS = ConfigProperty.of(
            ConfigCodecs.INT_ARRAY,
            "reward.display-slots",
            DEFAULT_SLOTS,
            ""
        );

        static final ConfigProperty<SlotPattern> REWARD_SELECTED_SLOTS = ConfigProperty.of(
            ConfigCodecs.SLOT_PATTERN,
            "reward.selected-slots",
            DEFAULT_SELECTED_SLOTS,
            ""
        );

        static final ConfigProperty<NightSound> REWARD_PICK_SOUND = ConfigProperty.of(
            ConfigCodecs.NIGHT_SOUND,
            "reward.pick-sound",
            VanillaSound.of(Sound.ENTITY_ITEM_PICKUP),
            ""
        );

        static final ConfigProperty<NightSound> REWARD_UNPICK_SOUND = ConfigProperty.of(
            ConfigCodecs.NIGHT_SOUND,
            "reward.unpick-sound",
            VanillaSound.of(Sound.ITEM_ARMOR_EQUIP_LEATHER),
            ""
        );
    }
}
