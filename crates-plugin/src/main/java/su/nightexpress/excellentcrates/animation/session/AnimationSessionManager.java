package su.nightexpress.excellentcrates.animation.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.animation.AnimationInstance;

@NullMarked
public class AnimationSessionManager {

    private final Map<UUID, AnimationInstance> sessions;

    public AnimationSessionManager() {
        this.sessions = new HashMap<>();
    }

    public void purgeStoppedSessions() {
        this.sessions.values().removeIf(instance -> !instance.isRunning());
    }

    public void stopSession(UUID playerId) {
        AnimationInstance instance = this.sessions.remove(playerId);
        if (instance != null) {
            instance.stop();
        }
    }

    public void registerSession(UUID playerId, AnimationInstance instance) {
        this.sessions.put(playerId, instance);
    }

    public void unregisterSession(UUID playerId) {
        this.sessions.remove(playerId);
    }

    public @Nullable AnimationInstance getSession(UUID playerId) {
        return this.sessions.get(playerId);
    }

    public boolean hasSession(UUID playerId) {
        AnimationInstance instance = this.sessions.get(playerId);
        return instance != null && instance.isRunning();
    }

    public Set<AnimationInstance> getSessions() {
        return Set.copyOf(this.sessions.values());
    }
}
