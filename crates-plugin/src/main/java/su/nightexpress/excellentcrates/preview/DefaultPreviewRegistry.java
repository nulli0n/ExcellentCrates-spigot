package su.nightexpress.excellentcrates.preview;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewProvider;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyedRegistry;

@NullMarked
public class DefaultPreviewRegistry implements PreviewRegistry {

    private final IdentifiableRegistry<PreviewProvider> providers = new IdentifiableRegistry<>();
    private final KeyedRegistry<Preview>                previews  = new KeyedRegistry<>();

    @Override
    public void clear() {
        this.clearProviders();
        this.clearPreviews();
    }

    @Override
    public void clearProviders() {
        this.providers.clear();
    }

    @Override
    public void clearPreviews() {
        this.previews.clear();
    }

    @Override
    public void registerProvider(PreviewProvider provider) {
        this.providers.register(provider);
    }

    @Override
    public void registerPreview(Preview preview) {
        this.previews.register(preview);
    }

    @Override
    public Set<PreviewProvider> getProviders() {
        return this.providers.values();
    }

    @Override
    public Set<Preview> getPreviews() {
        return this.previews.values();
    }

    @Override
    public @Nullable PreviewProvider getProviderById(Identifier providerId) {
        return this.providers.get(providerId);
    }

    @Override
    public @Nullable Preview getPreviewByKey(AdaptedKey previewKey) {
        return this.previews.get(previewKey);
    }
}
