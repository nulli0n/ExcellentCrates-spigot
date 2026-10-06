package su.nightexpress.excellentcrates.cost.ui.menu.context;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record CostOptionsMenuContext(Identifier crateId,
                                     Identifier costType,
                                     List<String> costOptions,
                                     AtomicBoolean isChosen,
                                     BackwardNavigator backwardNavigator,
                                     Runnable onAbort,
                                     Consumer<String> onChoice) implements BackwardSupport {

}
