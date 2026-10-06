package su.nightexpress.excellentcrates.keys.cost.settings;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public record KeyCostSettings(NightItem categoryIcon,
                              String categoryName,
                              List<String> categoryDescription) {

    public static KeyCostSettings defaultSettings() {
        return new KeyCostSettings(
            Schema.CATEGORY_ICON.getDefaultValue(),
            Schema.CATEGORY_NAME.getDefaultValue(),
            Schema.CATEGORY_DESCRIPTION.getDefaultValue()
        );
    }

    public static KeyCostSettings loadFrom(FileConfig config) {
        NightItem categoryIcon = config.getOrSet(Schema.CATEGORY_ICON);
        String categoryName = config.getOrSet(Schema.CATEGORY_NAME);
        List<String> categoryDescription = config.getOrSet(Schema.CATEGORY_DESCRIPTION);

        return new KeyCostSettings(
            categoryIcon,
            categoryName,
            categoryDescription
        );
    }

    private static final class Schema {

        static final ConfigProperty<NightItem> CATEGORY_ICON = ConfigProperty.of(ConfigCodecs.NIGHT_ITEM,
            "display.category.icon",
            NightItem.fromType(Material.TRIAL_KEY),
            "Set the icon for the category."
        );

        static final ConfigProperty<String> CATEGORY_NAME = ConfigProperty.of(ConfigCodecs.STRING,
            "display.category.name",
            "Keys",
            "Set the name for the category."
        );

        static final ConfigProperty<List<String>> CATEGORY_DESCRIPTION = ConfigProperty.of(ConfigCodecs.STRING_LIST,
            "display.category.description",
            List.of("Open crate using keys"),
            "Set the description for the category."
        );
    }
}
