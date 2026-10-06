package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ComponentHolder {

    PluginComponent getComponent();
}
