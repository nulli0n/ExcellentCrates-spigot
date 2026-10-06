package su.nightexpress.excellentcrates.cost;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class CostModule extends NamedComponentBundle {

    private static final Identifier ID   = new Identifier("cost");
    private static final String     NAME = "Cost";

    public CostModule() {
        super(ID, NAME);
    }
}
