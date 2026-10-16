package su.nightexpress.excellentcrates.animation.session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.animation.AnimationInstance;

@NullMarked
public class AnimationSessionManager {

    private final Map<UUID, AnimationInstance> sessions;
    private final Set<UUID> stoppingPlayers = new HashSet<>();

    private boolean shuttingDown;

    public AnimationSessionManager() {
        this.sessions = new HashMap<>();
    }

    public void purgeStoppedSessions() {
        this.sessions.values().removeIf(instance -> !instance.isRunning());
    }

    public void stopSession(UUID playerId) {
        AnimationInstance instance = this.sessions.remove(playerId);
        if (instance != null && instance.isRunning()) {
            // Completion callbacks may try to start the next reward in a chain.
            // An interruption must finish the pipeline without spawning another animation.
            this.stoppingPlayers.add(playerId);
            try {
                instance.stop();
            }
            finally {
                this.stoppingPlayers.remove(playerId);
            }
        }
    }

    public boolean canStartSession(UUID playerId) {
        return !this.shuttingDown && !this.stoppingPlayers.contains(playerId);
    }

    public void stopSession(UUID playerId, AnimationInstance instance) {
        if (this.sessions.get(playerId) == instance) {
            this.stopSession(playerId);
        }
    }

    public void setShuttingDown(boolean shuttingDown) {
        this.shuttingDown = shuttingDown;
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

    public Set<Map.Entry<UUID, AnimationInstance>> getSessionEntries() {
        return Map.copyOf(this.sessions).entrySet();
    }
}
