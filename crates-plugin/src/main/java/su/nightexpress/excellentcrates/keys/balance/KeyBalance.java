package su.nightexpress.excellentcrates.keys.balance;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.balance.IKeyBalance;

@NullMarked
public record KeyBalance(int physical, int virtual, int unclaimed) implements IKeyBalance {

    public static KeyBalance empty() {
        return new KeyBalance(0, 0, 0);
    }

    public boolean isEmpty() {
        return this.physical == 0 && this.virtual == 0 && this.unclaimed == 0;
    }
}
