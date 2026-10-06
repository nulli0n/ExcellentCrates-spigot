package su.nightexpress.engine.sql;

import org.jspecify.annotations.NonNull;

@NonNull
public record RemoveContext<P, K>(P parentId, K key) {

}
