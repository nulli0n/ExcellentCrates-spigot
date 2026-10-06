package su.nightexpress.engine.bukkit.particle.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleDataAdapter;

@NullMarked
public class FloatParticleDataAdapter implements ParticleDataAdapter<Float, Double> {

    @Override
    public Float convertToSource(Double target) {
        return target.floatValue();
    }

    @Override
    public Double convertToTarget(Float source) {
        return source.doubleValue();
    }

    @Override
    public Class<Float> getSourceType() {
        return Float.class;
    }

    @Override
    public Class<Double> getTargetType() {
        return Double.class;
    }
}
