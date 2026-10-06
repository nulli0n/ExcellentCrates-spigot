package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class IdentifiableComponentBundle extends ComponentBundle implements Identifiable {

    private final Identifier id;

    public IdentifiableComponentBundle(Identifier id) {
        super();
        this.id = id;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }
}