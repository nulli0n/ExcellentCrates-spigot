package su.nightexpress.excellentcrates.api.crate.open;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface OpenActionsComponent extends CrateComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    List<String> getCommands();

    void setCommands(List<String> commands);
}
