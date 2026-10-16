package su.nightexpress.engine.data;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface OwnableData<P, K> {

    P getParentId();

    K getKey();
}
