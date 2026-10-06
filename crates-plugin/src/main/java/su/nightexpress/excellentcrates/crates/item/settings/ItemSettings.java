package su.nightexpress.excellentcrates.crates.item.settings;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.crate.interact.InteractionKeys;
import su.nightexpress.excellentcrates.api.crate.item.interact.ItemInteractionType;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Enums;

@NullMarked
public record ItemSettings(Map<ItemInteractionType, Identifier> clickActions) {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemSettings.class);

    public static ItemSettings defaults() {
        return new ItemSettings(Schema.DEFAULT_CLICK_ACTIONS);
    }

    public static ItemSettings loadFrom(FileConfig config) {
        Map<ItemInteractionType, Identifier> clickActions = loadClickActions(config, "click_actions");

        return new ItemSettings(clickActions);
    }

    private static Map<ItemInteractionType, Identifier> loadClickActions(FileConfig config, String path) {
        Map<ItemInteractionType, Identifier> clickActions = new EnumMap<>(ItemInteractionType.class);

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
            Optional<ItemInteractionType> typeOpt = Enums.parse(key, ItemInteractionType.class);
            ItemInteractionType type = typeOpt.orElse(null);
            if (type == null) {
                LOGGER.warn("Invalid item interaction type '{}' in config '{}'", key, path);
                return;
            }

            String value = config.getString(path + "." + key);
            if (value == null) return;

            Identifier actionId = IdentifierParser.parse(value).orElse(null);
            if (actionId == null) {
                LOGGER.warn("Invalid action identifier '{}' for item interaction type '{}' in config '{}'", value, key,
                    path);
                return;
            }

            clickActions.put(type, actionId);
        });

        return clickActions;
    }

    private static final class Schema {

        private static final Map<ItemInteractionType, Identifier> DEFAULT_CLICK_ACTIONS = Map.of(
            ItemInteractionType.LEFT, InteractionKeys.PREVIEW_CRATE,
            ItemInteractionType.RIGHT, InteractionKeys.OPEN_CRATE
        );

    }
}
