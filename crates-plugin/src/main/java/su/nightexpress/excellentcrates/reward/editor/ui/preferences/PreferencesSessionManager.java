package su.nightexpress.excellentcrates.reward.editor.ui.preferences;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PreferencesSessionManager {

    @Nullable
    EditorPreferences getPreferences(UUID playerId);

    EditorPreferences getPreferencesOrCreate(UUID playerId);

    void clearSession(UUID playerId);
}
