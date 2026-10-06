package su.nightexpress.engine.bukkit.particle;

import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Particle.Spell;
import org.bukkit.Particle.Trail;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ParticleTypes {

    public static final ParticleType<Void> POOF              = ParticleType.simple("poof");
    public static final ParticleType<Void> EXPLOSION         = ParticleType.simple("explosion");
    public static final ParticleType<Void> EXPLOSION_EMITTER = ParticleType.simple("explosion_emitter");
    public static final ParticleType<Void> FIREWORK          = ParticleType.simple("firework");

    public static final ParticleType<Void> BUBBLE        = ParticleType.simple("bubble");
    public static final ParticleType<Void> SPLASH        = ParticleType.simple("splash");
    public static final ParticleType<Void> FISHING       = ParticleType.simple("fishing");
    public static final ParticleType<Void> UNDERWATER    = ParticleType.simple("underwater");
    public static final ParticleType<Void> CRIT          = ParticleType.simple("crit");
    public static final ParticleType<Void> ENCHANTED_HIT = ParticleType.simple("enchanted_hit");
    public static final ParticleType<Void> SMOKE         = ParticleType.simple("smoke");
    public static final ParticleType<Void> LARGE_SMOKE   = ParticleType.simple("large_smoke");

    /**
     * Uses {@link Spell} as DataType
     */
    public static final ParticleType<Spell> EFFECT = ParticleType.withData("effect", Spell.class);

    /**
     * Uses {@link Spell} as DataType
     */
    public static final ParticleType<Spell> INSTANT_EFFECT = ParticleType.withData("instant_effect", Spell.class);

    /**
     * Uses {@link Color} as DataType (with alpha support)
     */
    public static final ParticleType<Color> ENTITY_EFFECT = ParticleType.withData("entity_effect", Color.class);

    public static final ParticleType<Void> WITCH          = ParticleType.simple("witch");
    public static final ParticleType<Void> DRIPPING_WATER = ParticleType.simple("dripping_water");
    public static final ParticleType<Void> DRIPPING_LAVA  = ParticleType.simple("dripping_lava");
    public static final ParticleType<Void> ANGRY_VILLAGER = ParticleType.simple("angry_villager");
    public static final ParticleType<Void> HAPPY_VILLAGER = ParticleType.simple("happy_villager");
    public static final ParticleType<Void> MYCELIUM       = ParticleType.simple("mycelium");
    public static final ParticleType<Void> NOTE           = ParticleType.simple("note");
    public static final ParticleType<Void> PORTAL         = ParticleType.simple("portal");
    public static final ParticleType<Void> ENCHANT        = ParticleType.simple("enchant");
    public static final ParticleType<Void> FLAME          = ParticleType.simple("flame");
    public static final ParticleType<Void> LAVA           = ParticleType.simple("lava");
    public static final ParticleType<Void> CLOUD          = ParticleType.simple("cloud");

    /**
     * Uses {@link DustOptions} as DataType
     */
    public static final ParticleType<DustOptions> DUST = ParticleType.withData("dust", DustOptions.class);

    public static final ParticleType<Void> ITEM_SNOWBALL = ParticleType.simple("item_snowball");
    public static final ParticleType<Void> ITEM_SLIME    = ParticleType.simple("item_slime");
    public static final ParticleType<Void> HEART         = ParticleType.simple("heart");

    /**
     * Uses {@link ItemStack} as DataType
     */
    public static final ParticleType<ItemStack> ITEM = ParticleType.withData("item", ItemStack.class);

    /**
     * Uses {@link BlockData} as DataType
     */
    public static final ParticleType<BlockData> BLOCK = ParticleType.withData("block", BlockData.class);

    public static final ParticleType<Void> RAIN           = ParticleType.simple("rain");
    public static final ParticleType<Void> ELDER_GUARDIAN = ParticleType.simple("elder_guardian");

    /**
     * Uses {@link Float} as DataType, for the power of the breath
     */
    public static final ParticleType<Float> DRAGON_BREATH = ParticleType.withData("dragon_breath", Float.class);

    public static final ParticleType<Void> END_ROD          = ParticleType.simple("end_rod");
    public static final ParticleType<Void> DAMAGE_INDICATOR = ParticleType.simple("damage_indicator");
    public static final ParticleType<Void> SWEEP_ATTACK     = ParticleType.simple("sweep_attack");

    /**
     * Uses {@link BlockData} as DataType
     */
    public static final ParticleType<BlockData> FALLING_DUST = ParticleType.withData("falling_dust", BlockData.class);

    public static final ParticleType<Void> TOTEM_OF_UNDYING      = ParticleType.simple("totem_of_undying");
    public static final ParticleType<Void> SPIT                  = ParticleType.simple("spit");
    public static final ParticleType<Void> SQUID_INK             = ParticleType.simple("squid_ink");
    public static final ParticleType<Void> BUBBLE_POP            = ParticleType.simple("bubble_pop");
    public static final ParticleType<Void> CURRENT_DOWN          = ParticleType.simple("current_down");
    public static final ParticleType<Void> BUBBLE_COLUMN_UP      = ParticleType.simple("bubble_column_up");
    public static final ParticleType<Void> NAUTILUS              = ParticleType.simple("nautilus");
    public static final ParticleType<Void> DOLPHIN               = ParticleType.simple("dolphin");
    public static final ParticleType<Void> SNEEZE                = ParticleType.simple("sneeze");
    public static final ParticleType<Void> CAMPFIRE_COSY_SMOKE   = ParticleType.simple("campfire_cosy_smoke");
    public static final ParticleType<Void> CAMPFIRE_SIGNAL_SMOKE = ParticleType.simple("campfire_signal_smoke");
    public static final ParticleType<Void> COMPOSTER             = ParticleType.simple("composter");

    /**
     * Uses {@link Color} as DataType
     */
    public static final ParticleType<Color> FLASH = ParticleType.withData("flash", Color.class);

    public static final ParticleType<Void> FALLING_LAVA           = ParticleType.simple("falling_lava");
    public static final ParticleType<Void> LANDING_LAVA           = ParticleType.simple("landing_lava");
    public static final ParticleType<Void> FALLING_WATER          = ParticleType.simple("falling_water");
    public static final ParticleType<Void> DRIPPING_HONEY         = ParticleType.simple("dripping_honey");
    public static final ParticleType<Void> FALLING_HONEY          = ParticleType.simple("falling_honey");
    public static final ParticleType<Void> LANDING_HONEY          = ParticleType.simple("landing_honey");
    public static final ParticleType<Void> FALLING_NECTAR         = ParticleType.simple("falling_nectar");
    public static final ParticleType<Void> SOUL_FIRE_FLAME        = ParticleType.simple("soul_fire_flame");
    public static final ParticleType<Void> ASH                    = ParticleType.simple("ash");
    public static final ParticleType<Void> CRIMSON_SPORE          = ParticleType.simple("crimson_spore");
    public static final ParticleType<Void> WARPED_SPORE           = ParticleType.simple("warped_spore");
    public static final ParticleType<Void> SOUL                   = ParticleType.simple("soul");
    public static final ParticleType<Void> DRIPPING_OBSIDIAN_TEAR = ParticleType.simple("dripping_obsidian_tear");
    public static final ParticleType<Void> FALLING_OBSIDIAN_TEAR  = ParticleType.simple("falling_obsidian_tear");
    public static final ParticleType<Void> LANDING_OBSIDIAN_TEAR  = ParticleType.simple("landing_obsidian_tear");
    public static final ParticleType<Void> REVERSE_PORTAL         = ParticleType.simple("reverse_portal");
    public static final ParticleType<Void> WHITE_ASH              = ParticleType.simple("white_ash");

    /**
     * Uses {@link DustTransition} as DataType
     */
    public static final ParticleType<DustTransition> DUST_COLOR_TRANSITION = ParticleType.withData(
        "dust_color_transition", DustTransition.class
    );

    /**
     * Uses {@link Vibration} as DataType
     */
    public static final ParticleType<Vibration> VIBRATION = ParticleType.withData("vibration", Vibration.class);

    public static final ParticleType<Void> FALLING_SPORE_BLOSSOM    = ParticleType.simple("falling_spore_blossom");
    public static final ParticleType<Void> SPORE_BLOSSOM_AIR        = ParticleType.simple("spore_blossom_air");
    public static final ParticleType<Void> SMALL_FLAME              = ParticleType.simple("small_flame");
    public static final ParticleType<Void> SNOWFLAKE                = ParticleType.simple("snowflake");
    public static final ParticleType<Void> DRIPPING_DRIPSTONE_LAVA  = ParticleType.simple("dripping_dripstone_lava");
    public static final ParticleType<Void> FALLING_DRIPSTONE_LAVA   = ParticleType.simple("falling_dripstone_lava");
    public static final ParticleType<Void> DRIPPING_DRIPSTONE_WATER = ParticleType.simple("dripping_dripstone_water");
    public static final ParticleType<Void> FALLING_DRIPSTONE_WATER  = ParticleType.simple("falling_dripstone_water");
    public static final ParticleType<Void> GLOW_SQUID_INK           = ParticleType.simple("glow_squid_ink");
    public static final ParticleType<Void> GLOW                     = ParticleType.simple("glow");
    public static final ParticleType<Void> WAX_ON                   = ParticleType.simple("wax_on");
    public static final ParticleType<Void> WAX_OFF                  = ParticleType.simple("wax_off");
    public static final ParticleType<Void> ELECTRIC_SPARK           = ParticleType.simple("electric_spark");
    public static final ParticleType<Void> SCRAPE                   = ParticleType.simple("scrape");
    public static final ParticleType<Void> SONIC_BOOM               = ParticleType.simple("sonic_boom");
    public static final ParticleType<Void> SCULK_SOUL               = ParticleType.simple("sculk_soul");

    /**
     * Uses {@link Float} as DataType, the angle in radians
     */
    public static final ParticleType<Float> SCULK_CHARGE     = ParticleType.withData("sculk_charge", Float.class);
    public static final ParticleType<Void>  SCULK_CHARGE_POP = ParticleType.simple("sculk_charge_pop");

    /**
     * Uses {@link Integer} as DataType
     */
    public static final ParticleType<Integer> SHRIEK = ParticleType.withData("shriek", Integer.class);

    public static final ParticleType<Void> CHERRY_LEAVES   = ParticleType.simple("cherry_leaves");
    public static final ParticleType<Void> PALE_OAK_LEAVES = ParticleType.simple("pale_oak_leaves");

    /**
     * Uses {@link Color} as DataType
     */
    public static final ParticleType<Color> TINTED_LEAVES = ParticleType.withData("tinted_leaves", Color.class);

    public static final ParticleType<Void> EGG_CRACK                       = ParticleType.simple("egg_crack");
    public static final ParticleType<Void> DUST_PLUME                      = ParticleType.simple("dust_plume");
    public static final ParticleType<Void> WHITE_SMOKE                     = ParticleType.simple("white_smoke");
    public static final ParticleType<Void> GUST                            = ParticleType.simple("gust");
    public static final ParticleType<Void> SMALL_GUST                      = ParticleType.simple("small_gust");
    public static final ParticleType<Void> GUST_EMITTER_LARGE              = ParticleType.simple("gust_emitter_large");
    public static final ParticleType<Void> GUST_EMITTER_SMALL              = ParticleType.simple("gust_emitter_small");
    public static final ParticleType<Void> TRIAL_SPAWNER_DETECTION         = ParticleType.simple(
        "trial_spawner_detection");
    public static final ParticleType<Void> TRIAL_SPAWNER_DETECTION_OMINOUS = ParticleType.simple(
        "trial_spawner_detection_ominous");
    public static final ParticleType<Void> VAULT_CONNECTION                = ParticleType.simple("vault_connection");
    public static final ParticleType<Void> INFESTED                        = ParticleType.simple("infested");
    public static final ParticleType<Void> ITEM_COBWEB                     = ParticleType.simple("item_cobweb");

    /**
     * Uses {@link BlockData} as DataType
     */
    public static final ParticleType<BlockData> DUST_PILLAR = ParticleType.withData("dust_pillar", BlockData.class);

    /**
     * Uses {@link BlockData} as DataType
     */
    public static final ParticleType<BlockData> BLOCK_CRUMBLE = ParticleType.withData("block_crumble", BlockData.class);

    public static final ParticleType<Void> FIREFLY = ParticleType.simple("firefly");

    /**
     * Uses {@link Trail} as DataType
     */
    public static final ParticleType<Trail> TRAIL = ParticleType.withData("trail", Trail.class);

    public static final ParticleType<Void> OMINOUS_SPAWNING = ParticleType.simple("ominous_spawning");
    public static final ParticleType<Void> RAID_OMEN        = ParticleType.simple("raid_omen");
    public static final ParticleType<Void> TRIAL_OMEN       = ParticleType.simple("trial_omen");

    /**
     * Uses {@link BlockData} as DataType
     */
    public static final ParticleType<BlockData> BLOCK_MARKER = ParticleType.withData("block_marker", BlockData.class);

    public static final ParticleType<Void> COPPER_FIRE_FLAME = ParticleType.simple("copper_fire_flame");
    public static final ParticleType<Void> PAUSE_MOB_GROWTH  = ParticleType.simple("pause_mob_growth");
    public static final ParticleType<Void> RESET_MOB_GROWTH  = ParticleType.simple("reset_mob_growth");

    private ParticleTypes() {
    }
}
