package su.nightexpress.excellentcrates.util;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.api.event.CrateObtainRewardEvent;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.*;

public class CrateUtils {

    public static final int REWARD_ITEMS_LIMIT = 27;

    private static final Map<Player, Crate> ASSIGN_BLOCK_MAP = new WeakHashMap<>();

    public static void setAssignBlockCrate(@NotNull Player player, @NotNull Crate crate) {
        ASSIGN_BLOCK_MAP.put(player, crate);
    }

    @Nullable
    public static Crate getAssignBlockCrate(@NotNull Player player) {
        return ASSIGN_BLOCK_MAP.remove(player);
    }

    public static boolean hasEconomyBridge() {
        return Plugins.isInstalled(HookId.ECONOMY_BRIDGE);
    }

    public static void callRewardObtainEvent(@NotNull Player player, @NotNull Reward reward) {
        CrateObtainRewardEvent event = new CrateObtainRewardEvent(reward, player);
        Bukkit.getPluginManager().callEvent(event);
    }

    @NotNull
    public static Set<Player> getPlayersForEffects(@NotNull Location location) {
        Set<Player> players = new HashSet<>(Bukkit.getServer().getOnlinePlayers());
        players.removeIf(player -> !isInEffectRange(player, location));

        return players;
    }

    public static boolean isInEffectRange(@NotNull Player player, @NotNull Location location) {
        World world = location.getWorld();
        int distance = Config.CRATE_EFFECTS_VISIBILITY_DISTANCE.get();

        return player.getWorld() == world && player.getLocation().distance(location) <= distance;
    }

    @NotNull
    public static ItemStack removeCrateTags(@NotNull ItemStack itemStack) {
        ItemUtil.editMeta(itemStack, meta -> {
            PDCUtil.remove(meta, Keys.crateId);
            PDCUtil.remove(meta, Keys.keyId);
        });
        return itemStack;
    }

    @NotNull
    public static String createID(@NotNull String name) {
        String id = StringUtil.transformForID(name);
        if (id.isBlank()) id = UUID.randomUUID().toString().substring(0, 8);

        return id;
    }

    @NotNull
    public static String generateRewardID(@NotNull Crate crate, @NotNull ItemProvider provider) {
        String itemName = createID(provider.getItemType());

        int count = 0;
        while (crate.getReward(addCount(itemName, count)) != null) {
            count++;
        }

        return addCount(itemName, count);
    }

    private static String addCount(@NotNull String str, int count) {
        return count <= 0 ? str : str + "_" + count;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static boolean isSupportedParticle(@NotNull Particle particle) {
        return particle != Particle.VIBRATION && particle != Particle.DUST_COLOR_TRANSITION && particle != Particle.TRAIL;
    }

    public static boolean isSupportedParticleData(@NotNull UniParticle particle) {
        return particle.getParticle() != null && isSupportedParticleData(particle.getParticle().getDataType());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static boolean isSupportedParticleData(@NotNull Class<?> clazz) {
        return clazz != Void.class && clazz != Vibration.class && clazz != Particle.DustTransition.class && clazz != Particle.Trail.class;
    }
}
