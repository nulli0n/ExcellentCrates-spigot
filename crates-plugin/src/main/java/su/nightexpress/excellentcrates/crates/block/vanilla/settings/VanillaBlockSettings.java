package su.nightexpress.excellentcrates.crates.block.vanilla.settings;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.crates.block.vanilla.block.VanillaBlockDefinition;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;

@NullMarked
public record VanillaBlockSettings(Map<Identifier, VanillaBlockDefinition> blockDefinitions) {

    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaBlockSettings.class);

    public static VanillaBlockSettings defaults() {
        Map<Identifier, VanillaBlockDefinition> defaultDefinitions = getDefaultDefinitions();

        return new VanillaBlockSettings(defaultDefinitions);
    }

    public static VanillaBlockSettings empty() {
        return new VanillaBlockSettings(new HashMap<>());
    }

    public static VanillaBlockSettings loadFrom(FileConfig config) {
        Map<Identifier, VanillaBlockDefinition> definitions = new HashMap<>();

        String path = "blocks";

        // Populate default definitions if the configuration does not contain 'Blocks' section.
        // Do not populate defaults if the section exists, even if it's empty, to avoid overwriting user-defined settings.
        if (!config.contains(path)) {
            getDefaultDefinitions().forEach((id, definition) -> {
                config.set(path + "." + id.value(), definition);
            });
        }

        config.getSection(path).forEach(key -> {
            Identifier idKey = IdentifierParser.parse(key).orElse(null);

            // Ensure that the block key is valid and can be used as a Bukkit key. 
            if (idKey == null || !BukkitKeys.isValidValue(idKey.value())) {
                LOGGER.warn("Invalid block key: '{}'", key);
                return; // Skip invalid keys
            }

            VanillaBlockDefinition definition = config.get(path + "." + key, VanillaBlockDefinition.class);
            if (definition != null) {
                definitions.put(idKey, definition);
            }
            else {
                LOGGER.warn("Failed to load vanilla block definition for key '{}'.", key);
            }
        });

        return new VanillaBlockSettings(definitions);
    }

    private static Map<Identifier, VanillaBlockDefinition> getDefaultDefinitions() {
        Map<Identifier, VanillaBlockDefinition> defaultDefinitions = new HashMap<>();

        Set<Material> materials = EnumSet.noneOf(Material.class);
        materials.add(Material.CHEST);
        materials.add(Material.TRAPPED_CHEST);
        materials.add(Material.ENDER_CHEST);
        materials.add(Material.BARREL);
        materials.addAll(Tag.SHULKER_BOXES.getValues());

        materials.forEach(material -> {
            Identifier key = new Identifier(BukkitThing.getValue(material));
            VanillaBlockDefinition definition = new VanillaBlockDefinition(material, material);

            defaultDefinitions.put(key, definition);
        });

        return defaultDefinitions;
    }
}
