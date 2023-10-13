package su.nightexpress.excellentcrates.api.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CurrencyHandler {

    double getBalance(@NotNull Player player);

    void give(@NotNull Player player, double amount);

    void take(@NotNull Player player, double amount);

    void set(@NotNull Player player, double amount);
}
