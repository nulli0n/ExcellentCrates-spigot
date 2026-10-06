package su.nightexpress.excellentcrates.api.preview.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface PreviewComponent extends CrateComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    AdaptedKey getPreviewKey();

    void setPreviewKey(AdaptedKey previewKey);
}
