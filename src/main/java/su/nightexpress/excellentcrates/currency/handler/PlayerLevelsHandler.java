package su.nightexpress.excellentcrates.currency.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;

public class PlayerLevelsHandler implements CurrencyHandler {

    public static final String ID = "levels";

    @Override
    @NotNull
    public String getDefaultName() {
        return "XP Levels";
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return player.getLevel();
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        player.setLevel(player.getLevel() + (int) amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        player.setLevel(Math.max(0, player.getLevel() - (int) amount));
    }

    @Override
    public void set(@NotNull Player player, double amount) {
        player.setLevel((int) amount);
    }
}
