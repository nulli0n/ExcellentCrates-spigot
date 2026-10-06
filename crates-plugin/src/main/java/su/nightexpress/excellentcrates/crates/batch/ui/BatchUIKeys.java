package su.nightexpress.excellentcrates.crates.batch.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.batch.ui.menu.context.BatchAmountSelectionMenuContext;

@NullMarked
public final class BatchUIKeys {

    public static final MenuKey<BatchAmountSelectionMenuContext> AMOUNT_SELECTION = MenuKey.of(
        "crates.batch.amount_selection"
    );

    private BatchUIKeys() {
    }
}
