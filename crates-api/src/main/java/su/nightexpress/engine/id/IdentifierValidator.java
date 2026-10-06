package su.nightexpress.engine.id;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class IdentifierValidator {

    private IdentifierValidator() {
    }

    public static boolean isAllowed(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.';
    }

    public static boolean isValid(String name) {
        if (name.isBlank()) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            if (!isAllowed(name.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
