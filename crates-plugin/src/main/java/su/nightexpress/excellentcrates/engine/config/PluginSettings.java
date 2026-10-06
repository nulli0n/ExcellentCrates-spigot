package su.nightexpress.excellentcrates.engine.config;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public record PluginSettings(String prefix, String[] commandAliases) {

    public static PluginSettings defaults() {
        return new PluginSettings(
            Schema.PREFIX.getDefaultValue(),
            Schema.COMMAND_ALIASES.getDefaultValue()
        );
    }

    public static PluginSettings loadFrom(FileConfig config) {
        String prefix = config.getOrSet(Schema.PREFIX);
        String[] commandAliases = config.getOrSet(Schema.COMMAND_ALIASES);

        return new PluginSettings(prefix, commandAliases);
    }

    private static final class Schema {

        private static final String DEFAULT_PREFIX = TagWrappers.GRADIENT.with("#3A7BD5", "#00D2FF")
            .and(TagWrappers.BOLD).wrap("CRATES") + " " +
            TagWrappers.DARK_GRAY.wrap("»") + " " + TagWrappers.GRAY.opening();

        static final ConfigProperty<String> PREFIX = ConfigProperty.of(
            ConfigCodecs.STRING,
            "prefix",
            DEFAULT_PREFIX,
            "Settings for the plugin prefix"
        );

        static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(
            ConfigCodecs.STRING_ARRAY,
            "command_aliases",
            new String[]{"ecrates"},
            "Settings for the plugin command aliases"
        );
    }
}
