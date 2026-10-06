package su.nightexpress.engine.sql;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface OwnableData<P, K> {

    P getParentId();

    K getKey();
}
