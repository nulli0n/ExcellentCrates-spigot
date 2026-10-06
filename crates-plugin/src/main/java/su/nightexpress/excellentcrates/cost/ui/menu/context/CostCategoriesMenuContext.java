package su.nightexpress.excellentcrates.cost.ui.menu.context;

import java.util.List;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record CostCategoriesMenuContext(Identifier crateId,
                                        List<Identifier> costTypes,
                                        BackwardNavigator backwardNavigator,
                                        Runnable onAbort,
                                        Consumer<Identifier> onChoice) implements BackwardSupport {

}
