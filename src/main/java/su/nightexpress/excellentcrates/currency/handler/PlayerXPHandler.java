package su.nightexpress.excellentcrates.currency.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;

public class PlayerXPHandler implements CurrencyHandler {

    public static final String ID = "xp";

    @Override
    @NotNull
    public String getDefaultName() {
        return "XP";
    }

    private int getExpRequired(int level) {
        if (level <= 15) return 2 * level + 7;
        if (level <= 30) return 5 * level - 38;
        return 9 * level - 158;
    }

    private void addXP(@NotNull Player player, int amount) {
        int levelHas = player.getLevel();
        int xpHas = player.getTotalExperience();

        xpHas = Math.max(0, xpHas + amount);
        player.setExp(0F);
        player.setTotalExperience(0);
        player.setLevel(0);
        player.giveExp(xpHas);
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return player.getTotalExperience();
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        this.addXP(player, (int) amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        this.addXP(player, (int) -amount);
    }

    @Override
    public void set(@NotNull Player player, double amount) {
        this.addXP(player, -player.getTotalExperience());
        this.addXP(player, (int) amount);
    }
}
