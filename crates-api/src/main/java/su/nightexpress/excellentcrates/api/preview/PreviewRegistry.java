package su.nightexpress.excellentcrates.api.preview;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface PreviewRegistry {

    void clear();

    void clearProviders();

    void clearPreviews();

    void registerProvider(PreviewProvider provider);

    void registerPreview(Preview preview);

    Set<PreviewProvider> getProviders();

    Set<Preview> getPreviews();

    @Nullable
    PreviewProvider getProviderById(Identifier providerId);

    @Nullable
    Preview getPreviewByKey(AdaptedKey previewKey);
}
