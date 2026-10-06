package su.nightexpress.engine.id;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Identifier(String value) {

    /**
     * Creates a new identifier with the given value.
     *
     * @param value The value of the identifier.
     * @throws IllegalArgumentException If the value is empty or invalid.
     */
    public Identifier {
        if (!IdentifierValidator.isValid(value)) {
            throw new IllegalArgumentException("Identifier value is empty or invalid: '" + value + "'");
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
