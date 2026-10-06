package su.nightexpress.excellentcrates.engine;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.codec.BukkitColorCodec;
import su.nightexpress.engine.bukkit.particle.ParticleDataConverter;
import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.engine.bukkit.particle.codec.DustOptionsCodec;
import su.nightexpress.engine.bukkit.particle.codec.DustTransitionCodec;
import su.nightexpress.engine.bukkit.particle.codec.ParticleEffectCodec;
import su.nightexpress.engine.bukkit.particle.codec.SpellCodec;
import su.nightexpress.engine.bukkit.particle.data.BlockDataParticleDataAdapter;
import su.nightexpress.engine.bukkit.particle.data.FloatParticleDataAdapter;
import su.nightexpress.engine.bukkit.particle.data.ItemStackParticleDataAdapter;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.text.layout.LayoutComponent;
import su.nightexpress.engine.text.layout.LayoutComponentGroup;
import su.nightexpress.engine.text.layout.TextLayout;
import su.nightexpress.engine.text.layout.codec.LayoutComponentCodec;
import su.nightexpress.engine.text.layout.codec.LayoutComponentGroupCodec;
import su.nightexpress.engine.text.layout.codec.TextLayoutCodec;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.core.codec.IdentifierCodec;
import su.nightexpress.excellentcrates.core.common.limit.DefaultLimitOptions;
import su.nightexpress.excellentcrates.core.common.limit.codec.LimitOptionsCodec;
import su.nightexpress.excellentcrates.engine.codec.CooldownOptionsCodec;
import su.nightexpress.nightcore.bridge.reflect.TypeReference;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.Version;

@NullMarked
public final class CodecRegistrar {

    private CodecRegistrar() {
    }

    public static void register() {
        ConfigCodecs.register(DefaultLimitOptions.class, LimitOptionsCodec.INSTANCE);
        ConfigCodecs.register(CooldownOptions.class, CooldownOptionsCodec.INSTANCE);
        ConfigCodecs.register(Identifier.class, IdentifierCodec.INSTANCE);
        ConfigCodecs.register(DefaultLimitOptions.class, LimitOptionsCodec.INSTANCE);

        ConfigCodecs.register(TextLayout.class, TextLayoutCodec.INSTANCE);
        ConfigCodecs.register(LayoutComponent.class, LayoutComponentCodec.INSTANCE);
        ConfigCodecs.register(LayoutComponentGroup.class, LayoutComponentGroupCodec.INSTANCE);

        ConfigCodecs.register(Color.class, BukkitColorCodec.INSTANCE);
        ConfigCodecs.register(Particle.DustOptions.class, DustOptionsCodec.INSTANCE);
        if (Version.isAtLeast(Version.MC_1_21_10)) {
            ConfigCodecs.register(Particle.Spell.class, SpellCodec.INSTANCE);
        }
        ConfigCodecs.register(Particle.DustTransition.class, DustTransitionCodec.INSTANCE);
        ConfigCodecs.register(new TypeReference<ParticleEffect<?>>() {

        }, ParticleEffectCodec.INSTANCE);

        ParticleDataConverter.registerAdapter(new BlockDataParticleDataAdapter());
        ParticleDataConverter.registerAdapter(new FloatParticleDataAdapter());
        ParticleDataConverter.registerAdapter(new ItemStackParticleDataAdapter());
    }
}
