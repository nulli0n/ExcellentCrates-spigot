package su.nightexpress.engine.sql;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface SQLRepository<P, K, V> {

    CompletableFuture<Optional<V>> loadById(@NonNull P parent, @NonNull K key);

    CompletableFuture<List<V>> loadAllByParent(@NonNull P key);

    CompletableFuture<List<V>> loadAll();

    CompletableFuture<Void> upsert(Collection<V> data);

    CompletableFuture<Void> delete(Collection<RemoveContext<P, K>> data);
}
