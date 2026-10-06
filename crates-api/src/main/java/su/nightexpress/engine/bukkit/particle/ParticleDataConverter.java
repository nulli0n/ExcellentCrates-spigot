package su.nightexpress.engine.bukkit.particle;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ParticleDataConverter {

    private static final Map<Class<?>, ParticleDataAdapter<?, ?>> ADAPTERS = new HashMap<>();

    private ParticleDataConverter() {
    }

    public static void registerAdapter(ParticleDataAdapter<?, ?> adapter) {
        ADAPTERS.put(adapter.getSourceType(), adapter);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> ParticleDataAdapter<A, B> getAdapter(Class<A> sourceType, Class<B> targetType) {
        return (ParticleDataAdapter<A, B>) ADAPTERS.get(sourceType);
    }
}
