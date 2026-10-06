package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public class NamedComponentBundle extends IdentifiableComponentBundle {

    private final String name;

    public NamedComponentBundle(Identifier id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}