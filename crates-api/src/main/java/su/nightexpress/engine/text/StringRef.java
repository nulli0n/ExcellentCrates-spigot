package su.nightexpress.engine.text;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.locale.entry.TextLocale;

@NullMarked
@FunctionalInterface
public interface StringRef {

    String resolve();

    static StringRef raw(String title) {
        return () -> title;
    }

    static StringRef localized(TextLocale key) {
        return () -> key.text();
    }
}
