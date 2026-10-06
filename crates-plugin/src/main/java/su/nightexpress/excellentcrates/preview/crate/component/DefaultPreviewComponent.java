package su.nightexpress.excellentcrates.preview.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.preview.crate.PreviewComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class DefaultPreviewComponent implements PreviewComponent {

    public static final AdaptedKey DEFAULT_PREVIEW_KEY = BukkitKeys.create("inventory", "default");

    private boolean    enabled;
    private AdaptedKey previewKey;

    public DefaultPreviewComponent(boolean enabled, AdaptedKey previewKey) {
        this.enabled = enabled;
        this.previewKey = previewKey;
    }

    public static DefaultPreviewComponent defaults() {
        return new DefaultPreviewComponent(true, DEFAULT_PREVIEW_KEY);
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
    public AdaptedKey getPreviewKey() {
        return previewKey;
    }

    @Override
    public void setPreviewKey(AdaptedKey previewKey) {
        this.previewKey = previewKey;
    }
}
