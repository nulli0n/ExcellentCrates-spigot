package su.nightexpress.excellentcrates.api.cost.type;

import java.math.BigDecimal;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CostDisplayProvider<T extends CostOption> {

    String formatBalance(BigDecimal balance, @NonNull T option);

    CostDisplayInfo getCategoryDisplay(Crate crate);

    CostDisplayInfo getOptionDisplay(Crate crate, @NonNull T option);
}
