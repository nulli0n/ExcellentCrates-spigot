package su.nightexpress.excellentcrates.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;

import su.nightexpress.excellentcrates.animation.session.AnimationSessionManager;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionService;
import su.nightexpress.excellentcrates.animation.style.csgo.CSGOAnimationInstance;
import su.nightexpress.excellentcrates.animation.style.csgo.CSGOAnimationSettings;
import su.nightexpress.excellentcrates.animation.style.simpleroll.SimpleRollAnimation;
import su.nightexpress.excellentcrates.animation.style.simpleroll.SimpleRollSettings;
import su.nightexpress.excellentcrates.animation.visibility.AnimationVisibility;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.common.preview.NameAndLore;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;
import su.nightexpress.excellentcrates.core.util.FireworkUtil;
import su.nightexpress.nightcore.bridge.entity.WrappedTextDisplay;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.sound.VanillaSound;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class AnimationLifecycleTest extends BukkitTestSupport {

    private AnimationContext context(World world) {
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        Location location = new Location(world, 0, 0, 0);
        Block block = mock(Block.class);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
        when(world.getBlockAt(any(Location.class))).thenReturn(block);
        when(block.getLocation()).thenAnswer(call -> location.clone());
        when(block.getBoundingBox()).thenReturn(new BoundingBox(0, 0, 0, 1, 1, 1));
        Reward reward = mock(Reward.class);
        PipelineReward pipelineReward = mock(PipelineReward.class);
        when(pipelineReward.getRolled()).thenReturn(reward);
        when(pipelineReward.getGranted()).thenReturn(reward);
        RewardPreviewAPI preview = mock(RewardPreviewAPI.class);
        when(preview.getDisplayInfo(reward)).thenReturn(new NameAndLore("Reward", List.of()));
        when(preview.getColor(reward)).thenReturn(Color.WHITE);
        return new AnimationContext(player, location, mock(Crate.class), pipelineReward, List.of(reward), preview);
    }

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void simpleRollInterruptionRemovesTheItemAndCompletes(AnimationVisibility visibility) {
        World world = mock(World.class);
        Item item = mock(Item.class);
        when(world.spawn(any(Location.class), eq(Item.class), any(Consumer.class))).thenAnswer(call -> {
            call.<Consumer<Item>>getArgument(2).accept(item);
            return item;
        });
        AnimationContext context = this.context(world);
        Plugin plugin = mock(Plugin.class);
        Runnable callback = mock(Runnable.class);
        SimpleRollAnimation animation = new SimpleRollAnimation(plugin,
            new SimpleRollSettings("Test", 12, 5, 40, visibility), context, callback);
        verifyNoInteractions(item);
        animation.start();
        assertTrue(animation.isRunning());
        verify(item, times(visibility == AnimationVisibility.PRIVATE ? 1 : 0)).setVisibleByDefault(false);
        verify(context.player(), times(visibility == AnimationVisibility.PRIVATE ? 1 : 0)).showEntity(plugin, item);
        verify(item).setPickupDelay(Integer.MAX_VALUE);
        verify(item).setUnlimitedLifetime(true);
        animation.stop();
        verify(item).remove();
        verify(callback).run();
        assertFalse(animation.isRunning());
    }

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void simpleRollKeepsItsTimingAndRoutesEffectsToItsAudience(AnimationVisibility visibility) {
        try (MockedStatic<EntityUtil> entityUtil = mockStatic(EntityUtil.class);
             MockedStatic<FireworkUtil> fireworks = mockStatic(FireworkUtil.class);
             MockedStatic<VanillaSound> sounds = mockStatic(VanillaSound.class)) {
            VanillaSound sound = mock(VanillaSound.class);
            sounds.when(() -> VanillaSound.of(any(Sound.class), anyFloat())).thenReturn(sound);
            World world = mock(World.class);
            Item item = mock(Item.class);
            when(world.spawn(any(Location.class), eq(Item.class), any(Consumer.class))).thenAnswer(call -> {
                call.<Consumer<Item>>getArgument(2).accept(item);
                return item;
            });
            AnimationContext context = this.context(world);
            Runnable callback = mock(Runnable.class);
            SimpleRollAnimation animation = new SimpleRollAnimation(mock(Plugin.class),
                new SimpleRollSettings("Test", 3, 2, 4, visibility), context, callback);
            animation.start();
            int ticks = 0;
            while (animation.tick()) {
                assertTrue(++ticks < 20);
            }
            assertEquals(9, ticks);
            verify(context.reward()).getRolled();
            verify(context.reward(), never()).setGranted(any());
            entityUtil.verify(() -> EntityUtil.setCustomName(item, "Reward"), times(3));
            if (visibility == AnimationVisibility.PRIVATE) {
                verify(context.player(), times(3)).playSound(any(Location.class), eq(Sound.UI_BUTTON_CLICK), eq(0.5f), eq(1f));
                verify(context.player(), times(2)).playSound(any(Location.class), eq(Sound.BLOCK_NOTE_BLOCK_BELL), eq(0.5f), eq(1f));
                verify(context.player()).playSound(any(Location.class), eq(Sound.ENTITY_GENERIC_EXPLODE), eq(0.7f), eq(1f));
                verify(context.player()).spawnParticle(eq(Particle.FIREWORK), any(Location.class), eq(40), eq(0.3), eq(0.3), eq(0.3), eq(0.1));
                fireworks.verifyNoInteractions();
                verifyNoInteractions(sound);
            }
            else {
                verify(sound, times(6)).play(any(Location.class));
                fireworks.verify(() -> FireworkUtil.createRandom(any(Location.class)));
                verify(context.player(), never()).playSound(any(Location.class), any(Sound.class), anyFloat(), anyFloat());
            }
            animation.stop();
            verify(item).remove();
            verify(callback).run();
        }
    }

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void csgoDynamicEntriesHighlightAndCleanUpAfterCompletion(AnimationVisibility visibility) {
        try (MockedStatic<WrappedTextDisplay> wrapped = mockStatic(WrappedTextDisplay.class)) {
            wrapped.when(() -> WrappedTextDisplay.wrap(any(TextDisplay.class)))
                .thenAnswer(call -> mock(WrappedTextDisplay.class));
            World world = mock(World.class);
            List<Entity> entities = new ArrayList<>();
            when(world.spawn(any(Location.class), any(Class.class), any(Consumer.class))).thenAnswer(call -> {
                Entity entity = mock(call.<Class<? extends Entity>>getArgument(1));
                call.<Consumer<Entity>>getArgument(2).accept(entity);
                verify(entity, times(visibility == AnimationVisibility.PRIVATE ? 1 : 0)).setVisibleByDefault(false);
                entities.add(entity);
                return entity;
            });
            AnimationContext context = this.context(world);
            Plugin plugin = mock(Plugin.class);
            Runnable callback = mock(Runnable.class);
            CSGOAnimationInstance animation = new CSGOAnimationInstance(plugin, context, callback,
                new CSGOAnimationSettings("Test", 16, 50, 20, visibility));
            animation.start();
            assertTrue(animation.tick());
            int firstTickEntities = entities.size();
            int ticks = 1;
            while (animation.tick()) {
                assertTrue(++ticks < 100, "Animation must complete within its configured duration");
            }
            assertTrue(entities.size() > firstTickEntities, "Roulette must create entries after the first tick");
            assertEquals(70, ticks);
            assertTrue(entities.stream().filter(ItemDisplay.class::isInstance)
                .anyMatch(entity -> mockingDetails(entity).getInvocations().stream()
                    .anyMatch(call -> call.getMethod().getName().equals("setGlowing"))));
            animation.stop();
            for (Entity entity : entities) {
                verify(context.player(), times(visibility == AnimationVisibility.PRIVATE ? 1 : 0)).showEntity(plugin, entity);
                verify(entity).remove();
            }
            verify(callback).run();
            // Rendering uses the already selected reward; it never changes its grant.
            verify(context.reward(), never()).setGranted(any());
        }
    }

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void failedSimpleRollStartupCleansEntitiesWithoutCallingCompletionTwice(AnimationVisibility visibility) {
        World world = mock(World.class);
        Item item = mock(Item.class);
        when(world.spawn(any(Location.class), eq(Item.class), any(Consumer.class))).thenAnswer(call -> {
            call.<Consumer<Item>>getArgument(2).accept(item);
            throw new IllegalStateException("World insertion failed");
        });
        AnimationContext context = this.context(world);
        AnimationSessionManager manager = new AnimationSessionManager();
        AnimationSessionService service = new AnimationSessionService(manager);
        AnimationProfile profile = mock(AnimationProfile.class);
        when(profile.createInstance(eq(context), any())).thenAnswer(call -> new SimpleRollAnimation(mock(Plugin.class),
            new SimpleRollSettings("Test", 12, 5, 40, visibility), context, call.getArgument(1)));
        Runnable callback = mock(Runnable.class);
        assertFalse(service.startSession(profile, context, callback).success());
        verify(item).remove();
        verifyNoInteractions(callback); // A failed start is completed by AnimationPlayService.
        assertTrue(manager.getSessions().isEmpty());
    }
}
