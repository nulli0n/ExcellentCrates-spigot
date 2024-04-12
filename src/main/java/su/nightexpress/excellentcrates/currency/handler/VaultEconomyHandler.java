package su.nightexpress.excellentcrates.currency.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;
import su.nightexpress.nightcore.integration.VaultHook;

public class VaultEconomyHandler implements CurrencyHandler {

    public static final String ID = "money";

    @Override
    @NotNull
    public String getDefaultName() {
        return "Money";
    }

    @Override
    @NotNull
    public String getDefaultFormat() {
        return "$" + Placeholders.GENERIC_AMOUNT;
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return VaultHook.getBalance(player);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        VaultHook.addMoney(player, amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        VaultHook.takeMoney(player, amount);
    }

    @Override
    public void set(@NotNull Player player, double amount) {
        this.take(player, this.getBalance(player));
        this.give(player,amount);
    }
}
