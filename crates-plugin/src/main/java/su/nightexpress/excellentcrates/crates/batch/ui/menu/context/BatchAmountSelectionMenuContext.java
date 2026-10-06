package su.nightexpress.excellentcrates.crates.batch.ui.menu.context;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public record BatchAmountSelectionMenuContext(Identifier crateId,
                                              int maxAllowed,
                                              AtomicBoolean isChosen,
                                              Runnable onAbort,
                                              Consumer<Integer> onSelect) {

}
