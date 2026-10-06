package su.nightexpress.engine.service;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record ServiceKey<T extends PluginAPI>(Class<T> type, String name) {

}
