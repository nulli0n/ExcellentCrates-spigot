package su.nightexpress.excellentcrates.api.crate.interact;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public final class InteractionKeys {

    public static final Identifier PREVIEW_CRATE = new Identifier("preview_crate");
    public static final Identifier OPEN_CRATE    = new Identifier("open_crate");

    private InteractionKeys() {
    }
}
