package su.nightexpress.engine.bukkit;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EventPublisher {

    boolean fireEvent(Event event);
}