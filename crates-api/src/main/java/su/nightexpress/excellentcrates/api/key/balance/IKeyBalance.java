package su.nightexpress.excellentcrates.api.key.balance;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IKeyBalance {

    int physical();

    int virtual();

    int unclaimed();

    boolean isEmpty();
}
