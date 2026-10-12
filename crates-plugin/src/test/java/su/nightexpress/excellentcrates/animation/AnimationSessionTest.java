package su.nightexpress.excellentcrates.animation;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import su.nightexpress.excellentcrates.animation.session.AnimationSessionManager;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionService;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;
import su.nightexpress.excellentcrates.core.animation.BaseAnimationInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AnimationSessionTest extends BukkitTestSupport {

    private AnimationContext context() {
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        return new AnimationContext(player, new Location(null, 0, 0, 0), mock(Crate.class),
            mock(PipelineReward.class), List.of(), mock(RewardPreviewAPI.class));
    }

    private AnimationProfile profile() {
        AnimationProfile profile = mock(AnimationProfile.class);
        when(profile.createInstance(any(), any())).thenAnswer(call -> new BaseAnimationInstance(call.getArgument(0), call.getArgument(1)) {
            @Override
            protected void onStart() {}

            @Override
            protected void onStop() {}

            @Override
            protected boolean onTick() { return false; }
        });
        return profile;
    }

    @Test
    void differentPlayersCanAnimateAtTheSameCoordinatesButOnePlayerCannotStartTwice() {
        AnimationSessionManager manager = new AnimationSessionManager();
        AnimationSessionService service = new AnimationSessionService(manager);
        AnimationProfile profile = this.profile();
        AnimationContext first = this.context();
        assertTrue(service.startSession(profile, first, () -> {}).success());
        assertTrue(service.startSession(profile, this.context(), () -> {}).success());
        assertTrue(service.startSession(profile, this.context(), () -> {}).success());
        assertFalse(service.startSession(profile, first, () -> {}).success());
        assertEquals(3, manager.getSessions().size());
    }

    @Test
    void normalCompletionCanAdvanceToTheNextRewardAndOnlyCallsBackOnce() {
        AnimationSessionManager manager = new AnimationSessionManager();
        AnimationSessionService service = new AnimationSessionService(manager);
        AnimationContext context = this.context();
        AnimationProfile profile = this.profile();
        Runnable finalCallback = mock(Runnable.class);
        Runnable next = () -> assertTrue(service.startSession(profile, context, finalCallback).success());
        assertTrue(service.startSession(profile, context, next).success());
        AnimationInstance first = manager.getSession(context.player().getUniqueId());
        assertNotNull(first);
        first.stop();
        AnimationInstance second = manager.getSession(context.player().getUniqueId());
        assertNotSame(first, second);
        first.stop();
        assertSame(second, manager.getSession(context.player().getUniqueId()));
        second.stop();
        second.stop();
        verify(finalCallback).run();
        manager.purgeStoppedSessions();
        assertTrue(manager.getSessions().isEmpty());
    }

    @Test
    void interruptionCompletesWithoutStartingAnotherRewardAnimation() {
        AnimationSessionManager manager = new AnimationSessionManager();
        AnimationSessionService service = new AnimationSessionService(manager);
        AnimationContext context = this.context();
        AnimationProfile profile = this.profile();
        Runnable callback = mock(Runnable.class);
        assertTrue(service.startSession(profile, context, () -> {
            assertFalse(service.startSession(profile, context, callback).success());
            callback.run();
        }).success());
        manager.stopSession(context.player().getUniqueId());
        verify(callback).run();
        assertTrue(manager.getSessions().isEmpty());
        assertTrue(manager.canStartSession(context.player().getUniqueId()));
    }

    @Test
    void shutdownRejectsNewAnimationsFromCompletionCallbacks() {
        AnimationSessionManager manager = new AnimationSessionManager();
        AnimationSessionService service = new AnimationSessionService(manager);
        AnimationContext context = this.context();
        AnimationProfile profile = this.profile();
        Runnable callback = mock(Runnable.class);
        assertTrue(service.startSession(profile, context, () -> {
            assertFalse(service.startSession(profile, this.context(), callback).success());
            callback.run();
        }).success());
        manager.beginShutdown();
        manager.stopSession(manager.getSession(context.player().getUniqueId()));
        verify(callback).run();
        assertTrue(manager.getSessions().isEmpty());
    }
}
