package su.nightexpress.excellentcrates.crates.block.component.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.crates.block.component.DefaultBlockComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class BlockComponentCodec implements ConfigCodec<DefaultBlockComponent> {

    public static final BlockComponentCodec INSTANCE = new BlockComponentCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockComponentCodec.class);

    @Override
    public DefaultBlockComponent read(FileConfig config, String path) throws CodecReadException {
        Map<AdaptedKey, Set<ExactPos>> blockPositions = new HashMap<>();

        String positionsPath = path + ".positions";

        config.getSection(positionsPath).forEach(worldKeyStr -> {
            AdaptedKey worldKey = BukkitKeys.parse(worldKeyStr).orElse(null);
            if (worldKey == null) {
                LOGGER.warn("Invalid world key '{}' in path '{}'", worldKeyStr, positionsPath);
                return;
            }

            List<String> rawPositions = config.getOrSet(positionsPath + "." + worldKeyStr, ConfigCodecs.STRING_LIST,
                List.of());

            Set<ExactPos> positions = rawPositions.stream()
                .map(ExactPos::deserialize)
                .collect(Collectors.toSet());

            blockPositions.put(worldKey, positions);
        });

        return new DefaultBlockComponent(blockPositions);
    }

    @Override
    public void write(FileConfig config, String path, DefaultBlockComponent value) {
        String positionsPath = path + ".positions";

        config.remove(positionsPath); // Clear existing positions before writing new ones

        value.iterateBlockPositions((worldKey, positions) -> {
            List<String> serializedPositions = positions.stream()
                .map(ExactPos::serialize)
                .toList();

            config.set(positionsPath + "." + worldKey.asString(), serializedPositions);
        });
    }
}
