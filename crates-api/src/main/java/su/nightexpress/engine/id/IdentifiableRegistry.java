package su.nightexpress.engine.id;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class IdentifiableRegistry<T extends Identifiable> {

    protected final Map<Identifier, T> registryMap;

    public IdentifiableRegistry() {
        this.registryMap = new ConcurrentHashMap<>();
    }

    public void clear() {
        this.registryMap.clear();
    }

    public int size() {
        return this.registryMap.size();
    }

    public void register(@NonNull T item) {
        this.registryMap.put(item.id(), item);
    }

    public @Nullable T remove(@NonNull T item) {
        return this.remove(item.id());
    }

    public @Nullable T remove(Identifier id) {
        return this.registryMap.remove(id);
    }

    public @Nullable T get(Identifier key) {
        return this.registryMap.get(key);
    }

    public Optional<T> lookup(Identifier key) {
        return Optional.ofNullable(this.get(key));
    }

    public Set<T> values() {
        return Set.copyOf(this.registryMap.values());
    }

    public Set<Identifier> ids() {
        return Set.copyOf(this.registryMap.keySet());
    }

    public Set<String> idValues() {
        return this.registryMap.keySet().stream().map(Identifier::value).collect(Collectors.toSet());
    }

    public Stream<T> stream() {
        return this.values().stream();
    }
}