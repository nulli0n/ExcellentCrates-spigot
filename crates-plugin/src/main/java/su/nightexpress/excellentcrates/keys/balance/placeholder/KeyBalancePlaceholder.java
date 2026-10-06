package su.nightexpress.excellentcrates.keys.balance.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class KeyBalancePlaceholder implements KeyPlaceholder {

    private final KeyBalanceService balanceService;

    public KeyBalancePlaceholder(KeyBalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    public Consumer<Builder> applyBase(CrateKey key, @Nullable Player player) {
        return ctx -> {
            if (player != null) {
                ctx.with(SharedPlaceholders.KEY_BALANCE, () -> {
                    return NumberUtil.format(this.balanceService.countKeys(player, key));
                });
            }
        };
    }

}
