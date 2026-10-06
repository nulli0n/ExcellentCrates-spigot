package su.nightexpress.excellentcrates.crates.block.vanilla.provider;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.excellentcrates.crates.block.vanilla.block.VanillaBlock;
import su.nightexpress.excellentcrates.crates.block.vanilla.settings.VanillaBlockSettings;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;

@NullMarked
public class VanillaBlockProvider implements BlockProvider<VanillaBlock> {

    public static final Identifier ID         = new Identifier("vanilla");
    private static final KeyDomain KEY_DOMAIN = KeyDomain.of(ID.value());

    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaBlockProvider.class);

    private final ReadOnlySettings<VanillaBlockSettings> settings;

    public VanillaBlockProvider(ReadOnlySettings<VanillaBlockSettings> settings) {
        this.settings = settings;
    }

    @Override
    public Set<VanillaBlock> fetchBlocks() {
        Set<VanillaBlock> blocks = new HashSet<>();

        this.settings.get().blockDefinitions().forEach((id, definition) -> {
            String key = id.value();

            // Ensure that the block key is valid and can be used as a Bukkit key.
            if (!BukkitKeys.isValidValue(key)) {
                LOGGER.warn("Invalid block key: '{}'", key);
                return; // Skip invalid keys
            }

            AdaptedKey adaptedKey = KEY_DOMAIN.make(key);
            blocks.add(new VanillaBlock(adaptedKey, definition));
        });

        return blocks;
    }

    @Override
    public boolean canHandle(Location location) {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
