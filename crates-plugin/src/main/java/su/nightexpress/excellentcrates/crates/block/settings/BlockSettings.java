package su.nightexpress.excellentcrates.crates.block.settings;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.crate.block.interact.BlockInteractionType;
import su.nightexpress.excellentcrates.api.crate.interact.InteractionKeys;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public record BlockSettings(Set<String> disabledProviders,
                            Map<BlockInteractionType, Identifier> clickActions,
                            String blockName,
                            List<String> blockLore) {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockSettings.class);

    public boolean isDisabledProvider(String provider) {
        return this.disabledProviders.contains(LowerCase.INTERNAL.apply(provider));
    }

    public static BlockSettings defaults() {
        return new BlockSettings(
            Schema.DISABLED_PROVIDERS.getDefaultValue(),
            Schema.DEFAULT_CLICK_ACTIONS,
            Schema.BLOCK_ITEM_NAME.getDefaultValue(),
            Schema.BLOCK_ITEM_LORE.getDefaultValue()
        );
    }

    public static BlockSettings loadFrom(FileConfig config) {
        Set<String> disabledProviders = config.getOrSet(Schema.DISABLED_PROVIDERS);
        Map<BlockInteractionType, Identifier> clickActions = loadClickActions(config, "interaction.click_actions");

        String blockName = config.getOrSet(Schema.BLOCK_ITEM_NAME);
        List<String> blockLore = config.getOrSet(Schema.BLOCK_ITEM_LORE);

        return new BlockSettings(disabledProviders, clickActions, blockName, blockLore);
    }

    private static Map<BlockInteractionType, Identifier> loadClickActions(FileConfig config, String path) {
        Map<BlockInteractionType, Identifier> clickActions = new EnumMap<>(BlockInteractionType.class);

        // Set default values if the config does not contain the path.
        // But do not touch empty sections, allowing users to remove all click actions by setting an empty section.
        if (!config.contains(path)) {
            Schema.DEFAULT_CLICK_ACTIONS.forEach((type, actionId) -> {
                config.set(path + "." + type.name(), actionId.toString());
                clickActions.put(type, actionId);
            });

            return clickActions;
        }

        config.getSection(path).forEach(key -> {
            Optional<BlockInteractionType> typeOpt = Enums.parse(key, BlockInteractionType.class);
            BlockInteractionType type = typeOpt.orElse(null);
            if (type == null) {
                LOGGER.warn("Invalid block interaction type '{}' in config '{}'", key, path);
                return;
            }

            String value = config.getString(path + "." + key);
            if (value == null) return;

            Identifier actionId = IdentifierParser.parse(value).orElse(null);
            if (actionId == null) {
                LOGGER.warn("Invalid action identifier '{}' for block interaction type '{}' in config '{}'", value, key,
                    path);
                return;
            }

            clickActions.put(type, actionId);
        });

        return clickActions;
    }

    private static final class Schema {

        static final Map<BlockInteractionType, Identifier> DEFAULT_CLICK_ACTIONS = Map.of(
            BlockInteractionType.LEFT, InteractionKeys.PREVIEW_CRATE,
            BlockInteractionType.SNEAK_LEFT, InteractionKeys.PREVIEW_CRATE,
            BlockInteractionType.RIGHT, InteractionKeys.OPEN_CRATE,
            BlockInteractionType.SNEAK_RIGHT, InteractionKeys.OPEN_CRATE
        );

        static final ConfigProperty<Set<String>> DISABLED_PROVIDERS = ConfigProperty.of(
            ConfigCodecs.STRING_SET,
            "disabled_providers",
            Set.of(),
            "The set of disabled providers for the crate block."
        );

        static final ConfigProperty<String> BLOCK_ITEM_NAME = ConfigProperty.of(
            ConfigCodecs.STRING,
            "block_item.name",
            TagWrappers.GOLD.wrap("Crate Block") +
                " " +
                TagWrappers.GRAY.wrap("-") +
                " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.NAME),
            "The name of the crate block item."
        );

        static final ConfigProperty<List<String>> BLOCK_ITEM_LORE = ConfigProperty.of(
            ConfigCodecs.STRING_LIST,
            "block_item.lore",
            Lists.newList(
                SharedPlaceholders.LORE,
                TagWrappers.DARK_GRAY.and(TagWrappers.STRIKETHROUGH).wrap("-".repeat(20)),
                TagWrappers.GRAY.wrap("Place this block at any location"),
                TagWrappers.GRAY.wrap("to create a " + TagWrappers.GOLD.wrap("usable") + " crate block."),
                "",
                TagWrappers.GRAY.wrap("Crate: ") + SharedPlaceholders.CRATE_NAME
            )
        );
    }
}
