package su.nightexpress.excellentcrates.crates.hologram.component;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramComponent;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramOffset;
import su.nightexpress.excellentcrates.crates.hologram.component.data.StandardHologramOffset;

@NullMarked
public class StandardHologramComponent implements HologramComponent {

    private boolean        enabled;
    private List<String>   text;
    private HologramOffset offset;

    public StandardHologramComponent(boolean enabled, List<String> text, HologramOffset offset) {
        this.enabled = enabled;
        this.text = List.copyOf(text);
        this.offset = offset;
    }

    public static StandardHologramComponent createDefault() {
        return new StandardHologramComponent(true, List.of("Crate"), StandardHologramOffset.DEFAULT);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = List.copyOf(text);
    }

    public HologramOffset getOffset() {
        return offset;
    }

    public void setOffset(HologramOffset offset) {
        this.offset = offset;
    }
}
