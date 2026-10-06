package su.nightexpress.excellentcrates.crates.open.component;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.open.OpenActionsComponent;

@NullMarked
public class DefaultOpeningComponent implements OpenActionsComponent {

    private boolean      enabled;
    private List<String> openingCommands;

    public DefaultOpeningComponent(boolean enabled, List<String> openingCommands) {
        this.enabled = enabled;
        this.openingCommands = List.copyOf(openingCommands);
    }

    public static DefaultOpeningComponent createDefault() {
        return new DefaultOpeningComponent(true, List.of());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<String> getCommands() {
        return openingCommands;
    }

    @Override
    public void setCommands(List<String> commands) {
        this.openingCommands = List.copyOf(commands);
    }
}
