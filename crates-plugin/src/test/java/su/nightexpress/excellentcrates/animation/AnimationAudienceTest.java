package su.nightexpress.excellentcrates.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import su.nightexpress.excellentcrates.animation.visibility.AnimationAudience;
import su.nightexpress.excellentcrates.animation.visibility.AnimationVisibility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class AnimationAudienceTest {

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void visibilityIsAppliedBeforeInsertionForEveryEntityType(AnimationVisibility visibility) {
        Plugin plugin = mock(Plugin.class);
        Player opener = mock(Player.class);
        Player observer = mock(Player.class);
        World world = mock(World.class);
        Location location = new Location(world, 1, 2, 3);
        List<Entity> inserted = new ArrayList<>();

        when(world.spawn(eq(location), any(Class.class), any(Consumer.class))).thenAnswer(call -> {
            Entity entity = mock(call.<Class<? extends Entity>>getArgument(1));
            call.<Consumer<Entity>>getArgument(2).accept(entity);
            // This is the point at which the server can start tracking the entity.
            verify(entity, times(visibility == AnimationVisibility.PRIVATE ? 1 : 0)).setVisibleByDefault(false);
            verify(opener, never()).showEntity(plugin, entity);
            inserted.add(entity);
            return entity;
        });

        AnimationAudience audience = new AnimationAudience(plugin, opener, visibility);
        audience.spawn(location, Item.class, entity -> entity.setCustomNameVisible(true));
        audience.spawn(location, ItemDisplay.class, entity -> entity.setInterpolationDuration(1));
        audience.spawn(location, TextDisplay.class, entity -> entity.setInterpolationDuration(1));
        // A later roulette entry follows the same policy as the initial entries.
        audience.spawn(location, ItemDisplay.class, entity -> entity.setGlowing(true));
        for (Entity entity : inserted) {
            verify(opener, times(visibility == AnimationVisibility.PRIVATE ? 1 : 0)).showEntity(plugin, entity);
            verify(entity).setPersistent(false);
        }
        verifyNoInteractions(observer);

        audience.remove(inserted.getFirst());
        audience.cleanup();
        audience.cleanup();
        for (Entity entity : inserted) {
            verify(entity).remove();
        }
    }

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void partialSpawnFailureRemainsOwnedForCleanup(AnimationVisibility visibility) {
        Plugin plugin = mock(Plugin.class);
        Player opener = mock(Player.class);
        World world = mock(World.class);
        Location location = new Location(world, 0, 0, 0);
        ItemDisplay item = mock(ItemDisplay.class);
        TextDisplay text = mock(TextDisplay.class);
        when(world.spawn(eq(location), eq(ItemDisplay.class), any(Consumer.class))).thenAnswer(call -> {
            call.<Consumer<ItemDisplay>>getArgument(2).accept(item);
            return item;
        });
        when(world.spawn(eq(location), eq(TextDisplay.class), any(Consumer.class))).thenAnswer(call -> {
            call.<Consumer<TextDisplay>>getArgument(2).accept(text);
            return text;
        });
        AnimationAudience audience = new AnimationAudience(plugin, opener, visibility);
        audience.spawn(location, ItemDisplay.class, entity -> {});
        assertThrows(IllegalStateException.class, () -> audience.spawn(location, TextDisplay.class, entity -> {
            throw new IllegalStateException("Failed to initialize reward text");
        }));
        audience.cleanup();
        verify(item).remove();
        verify(text).remove();
        verify(opener, never()).showEntity(plugin, text);
    }
}
