package su.nightexpress.excellentcrates.rarity.reward.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.rarity.reward.component.DefaultRarityComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RarityComponentCodec implements ConfigCodec<DefaultRarityComponent> {

    public static final RarityComponentCodec INSTANCE = new RarityComponentCodec();

    @Override
    public DefaultRarityComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, true);

        String idName = config.getOrSet(path + ".rarity_id", ConfigCodecs.STRING, "null");
        Identifier rarityId = IdentifierParser.parse(idName)
            .orElseThrow(() -> new CodecReadException("Invalid rarity ID syntax"));

        return new DefaultRarityComponent(enabled, rarityId);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRarityComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".rarity_id", value.getRarityId().value());
    }
}
