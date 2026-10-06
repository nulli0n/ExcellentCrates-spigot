package su.nightexpress.excellentcrates.keys.cost.component.codec;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;
import su.nightexpress.excellentcrates.keys.cost.component.model.DefaultKeyRequirementComponent;
import su.nightexpress.excellentcrates.keys.cost.component.model.StandardKeyRequirementEntry;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class KeyCostComponentCodec implements ConfigCodec<DefaultKeyRequirementComponent> {

    public static final KeyCostComponentCodec INSTANCE = new KeyCostComponentCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyCostComponentCodec.class);

    @Override
    public DefaultKeyRequirementComponent read(FileConfig config, String path) throws CodecReadException {
        Map<Identifier, KeyRequirementEntry> keyEntryMap = new HashMap<>();

        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, false);

        config.getSection(path + ".keys").forEach(sId -> {
            String keyPath = path + ".keys." + sId;
            Identifier id = IdentifierParser.parse(sId).orElse(null);
            if (id == null) {
                LOGGER.error("Invalid key identifier: '{}' in path '{}'", sId, keyPath);
                return;
            }

            StandardKeyRequirementEntry entry = config.getOrSet(keyPath, KeyCostEntryCodec.INSTANCE,
                new StandardKeyRequirementEntry(1));
            keyEntryMap.put(id, entry);
        });

        return new DefaultKeyRequirementComponent.Builder()
            .setKeyEntryMap(keyEntryMap)
            .setEnabled(enabled)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, DefaultKeyRequirementComponent value) {
        config.set(path + ".enabled", value.isEnabled());

        config.set(path + ".keys", null);
        value.getKeyEntryMap().forEach((id, entry) -> {
            config.set(path + ".keys." + id.toString(), entry);
        });
    }

}
