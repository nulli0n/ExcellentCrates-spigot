package su.nightexpress.excellentcrates.api.crate.hologram.component;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface HologramComponent extends CrateComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    List<String> getText();

    void setText(List<String> text);

    HologramOffset getOffset();

    void setOffset(HologramOffset offset);
}
