package su.nightexpress.excellentcrates.api.cost.type;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CostLogicProvider<T extends CostOption> {

    boolean hasCost(Crate crate);

    List<T> getOptions(Crate crate);

    @Nullable
    T getOptionById(Crate crate, String identifier);

    BigDecimal getBalance(Player player, Crate crate, @NonNull T option);

    BigDecimal getCost(Crate crate, @NonNull T option, int amount);

    int getMaxAffordableOpens(Player player, Crate crate, @NonNull T option);

    boolean canAfford(Player player, Crate crate, @NonNull T option, int amount);

    void take(Player player, Crate crate, @NonNull T option, int amount);

    void refund(Player player, Crate crate, @NonNull T option, int amount);
}
