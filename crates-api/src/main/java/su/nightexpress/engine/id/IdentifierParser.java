package su.nightexpress.engine.id;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public final class IdentifierParser {

    private IdentifierParser() {
    }

    public static Optional<Identifier> parse(String input) {
        if (IdentifierValidator.isValid(input)) {
            return Optional.of(new Identifier(input));
        }
        return Optional.empty();
    }

    public static Identifier ofSanitized(String input) {
        return parseSanitized(input)
            .orElseThrow(() -> new IllegalArgumentException("Unable to sanitize input: '" + input + "'"));
    }

    public static Optional<Identifier> parseSanitized(String input) {
        if (input.isBlank()) {
            return Optional.empty();
        }

        char[] chars = LowerCase.internal(input).toCharArray();
        StringBuilder builder = new StringBuilder();

        for (char letter : chars) {
            if (Character.isWhitespace(letter)) {
                builder.append('_');
            }
            else if (IdentifierValidator.isAllowed(letter)) {
                builder.append(letter);
            }
        }

        String result = builder.toString();

        if (IdentifierValidator.isValid(result)) {
            return Optional.of(new Identifier(result));
        }

        return Optional.empty();
    }
}