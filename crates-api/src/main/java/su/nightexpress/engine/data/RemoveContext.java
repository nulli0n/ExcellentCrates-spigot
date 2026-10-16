package su.nightexpress.engine.data;

import org.jspecify.annotations.NonNull;

@NonNull
public record RemoveContext<P, K>(P parentId, K key) {

}
