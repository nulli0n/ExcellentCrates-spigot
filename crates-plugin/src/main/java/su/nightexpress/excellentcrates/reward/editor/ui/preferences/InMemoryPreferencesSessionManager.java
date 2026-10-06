package su.nightexpress.excellentcrates.reward.editor.ui.preferences;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class InMemoryPreferencesSessionManager implements PreferencesSessionManager {

    private final Map<UUID, EditorPreferences> preferences;
    private final PreferencesFactory           preferencesFactory;

    public InMemoryPreferencesSessionManager(PreferencesFactory preferencesFactory) {
        this.preferences = new ConcurrentHashMap<>();
        this.preferencesFactory = preferencesFactory;
    }

    @Override
    public @Nullable EditorPreferences getPreferences(UUID playerId) {
        return this.preferences.get(playerId);
    }

    @Override
    public EditorPreferences getPreferencesOrCreate(UUID playerId) {
        return this.preferences.computeIfAbsent(playerId, k -> this.preferencesFactory.createDefault());
    }

    @Override
    public void clearSession(UUID playerId) {
        this.preferences.remove(playerId);
    }
}
