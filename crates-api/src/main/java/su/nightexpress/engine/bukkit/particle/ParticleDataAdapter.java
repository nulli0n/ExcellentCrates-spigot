package su.nightexpress.engine.bukkit.particle;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ParticleDataAdapter<A, B> {

    Class<A> getSourceType();

    Class<B> getTargetType();

    @NonNull
    B convertToTarget(@NonNull A source);

    @NonNull
    A convertToSource(@NonNull B target);
}
