package su.nightexpress.excellentcrates.opening;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.opening.inventory.InvOpeningProvider;
import su.nightexpress.excellentcrates.opening.inventory.InvOpeningType;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinMode;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerType;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.AnimationProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.RewardProvider;
import su.nightexpress.excellentcrates.opening.world.provider.SimpleRollProvider;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.random.WeightedItem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class OpeningUtils {

    private static final String      TITLE         = BLACK.wrap("Opening " + CRATE_NAME + "...");

    private static final int TICKS_TO_SKIP          = 40;
    private static final int COMPLETION_PAUSE_TICKS = 40;

    private static final String      SOUND         = BukkitThing.getAsString(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE);
    private static final Set<String> RARITIES      = Lists.newSet(WILDCARD);

    @Nullable
    public static Firework createFirework(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return null;

        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Type type = Rnd.get(FireworkEffect.Type.values());
        Color color = Color.fromBGR(Rnd.get(256), Rnd.get(256), Rnd.get(256));
        Color fade = Color.fromBGR(Rnd.get(256), Rnd.get(256), Rnd.get(256));
        FireworkEffect effect = FireworkEffect.builder()
            .flicker(Rnd.nextBoolean()).withColor(color).withFade(fade).with(type).trail(Rnd.nextBoolean()).build();

        meta.addEffect(effect);
        meta.setPower(Rnd.get(4));
        firework.setFireworkMeta(meta);
        return firework;
    }

    public static void setupSimpleRoll(@NotNull SimpleRollProvider provider) {
        provider.setStepsAmount(15);
        provider.setStepsTick(3);
        provider.setCompletePause(40);
    }

    public static void setupCSGO(@NotNull InvOpeningProvider config) {
        config.setInvType(MenuType.GENERIC_9X3);
        config.setInvTitle(TITLE);
        config.setMode(InvOpeningType.NORMAL);
        config.setMaxTicksForSkip(TICKS_TO_SKIP);
        config.setCompletionPauseTicks(COMPLETION_PAUSE_TICKS);
        config.setWinSlots(new int[]{13});

        var animationDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>());
        var rewardDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>());

        String rainbowId = "rainbow";

        animationDataMap.put("background", new SpinnerData(rainbowId, SpinMode.INDEPENDENT, "0,1,2,3,5,6,7,8,18,19,20,21,23,24,25,26",
            71, 1, 0, 10, 1, null));

        rewardDataMap.put("main", new SpinnerData(DEFAULT, SpinMode.SEQUENTAL, "17,16,15,14,13,12,11,10,9",
            51, 1, 0, 5, 1, SOUND));

        config.getDefaultItems().put("arrow_up", NightItem.asCustomHead("77334cddfab45d75ad28e1a47bf8cf5017d2f0982f6737da22d4972952510661")
            .setDisplayName(CYAN.wrap(BOLD.wrap("↑ Your Reward ↑")))
            .toMenuItem()
            .setSlots(22)
            .build());

        config.getDefaultItems().put("arrow_down", NightItem.asCustomHead("e7742034f59db890c8004156b727c77ca695c4399d8e0da5ce9227cf836bb8e2")
            .setDisplayName(CYAN.wrap(BOLD.wrap("↓ Your Reward ↓")))
            .toMenuItem()
            .setSlots(4)
            .build());

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>())
            .put(DEFAULT, new RewardProvider(RARITIES));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(rainbowId, new AnimationProvider(getRainbowPanes()));
    }

    public static void setupMystery(@NotNull InvOpeningProvider config) {
        config.setInvType(MenuType.GENERIC_9X3);
        config.setInvTitle(TITLE);
        config.setMode(InvOpeningType.NORMAL);
        config.setMaxTicksForSkip(TICKS_TO_SKIP);
        config.setCompletionPauseTicks(COMPLETION_PAUSE_TICKS);
        config.setWinSlots(new int[]{13});

        String rainbowId = "rainbow";

        var animationDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>());
        var rewardDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>());

        animationDataMap.put("background", new SpinnerData(rainbowId, SpinMode.INDEPENDENT, "0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,22,23,24,25,26",
            126, 1, 0, 30, 1, null));

        rewardDataMap.put("main", new SpinnerData(DEFAULT, SpinMode.SEQUENTAL, "13",
            51, 1, 0, 5, 1, SOUND));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>())
            .put(DEFAULT, new RewardProvider(RARITIES));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(rainbowId, new AnimationProvider(getRainbowPanes()));
    }

    public static void setupRoulette(@NotNull InvOpeningProvider config) {
        config.setInvType(MenuType.GENERIC_9X5);
        config.setInvTitle(TITLE);
        config.setMode(InvOpeningType.NORMAL);
        config.setMaxTicksForSkip(TICKS_TO_SKIP);
        config.setCompletionPauseTicks(COMPLETION_PAUSE_TICKS);
        config.setWinSlots(new int[]{20});

        config.getDefaultItems().put("background", NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .toMenuItem()
            .setSlots(0,1,2,3,4,5,6,7,8,9,10,11,15,16,17,18,19,20,24,25,26,27,28,29,33,34,35,36,37,38,39,40,41,42,43,44,12,13,14,22,23,30,31,32)
            .build());

        config.getDefaultItems().put("arrow_right", NightItem.asCustomHead("a6af217aeddf0f40064969ebb2042f7aeafbc7d0f175a27624133a3befd10281")
            .setDisplayName(LIGHT_RED.wrap(BOLD.wrap("YOUR PRIZE →")))
            .toMenuItem()
            .setSlots(19)
            .build());

        config.getDefaultItems().put("arrow_left", NightItem.asCustomHead("1c5a8aa8a4c03600a2b5a4eb6beb51d590260b095ee1cdaa976b09bdfe5661c6")
            .setDisplayName(LIGHT_RED.wrap(BOLD.wrap("← YOUR PRIZE")))
            .toMenuItem()
            .setSlots(21)
            .build());

        var rewardDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>());

        rewardDataMap.put("main", new SpinnerData(DEFAULT, SpinMode.SEQUENTAL, "39,40,41,33,24,15,5,4,3,11,20,29",
            75, 1, 0, 10, 1, BukkitThing.getAsString(Sound.BLOCK_NOTE_BLOCK_BANJO)));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>())
            .put(DEFAULT, new RewardProvider(RARITIES));
    }

    public static void setupChests(@NotNull InvOpeningProvider config) {
        config.setInvType(MenuType.GENERIC_9X3);
        config.setInvTitle(TITLE);
        config.setMode(InvOpeningType.SELECTION);
        config.setMaxTicksForSkip(TICKS_TO_SKIP);
        config.setCompletionPauseTicks(COMPLETION_PAUSE_TICKS);
        config.setWinSlots(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26});

        config.setSelectionAmount(3);
        config.setSelectionSlots(config.getWinSlots());
        config.setSelectionOriginIcon(NightItem.asCustomHead("f98bc63f05f6378bf29ef10e3d82acb3ceb73a720bf80f30bc576d0ad8c40cfb")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Click to select!")))
            .setLore(Lists.newList(LIGHT_GRAY.wrap("You can select and open " + LIGHT_YELLOW.wrap("3 chests") + ".")))
        );
        config.setSelectionClickedIcon(NightItem.asCustomHead("6ed2d4a43d5556d676a53a53851a63ea19ab597668b18d0800fa0fbeacec58f3")
            .setDisplayName(LIGHT_GREEN.wrap(BOLD.wrap("Selected Chest")))
            .setLore(Lists.newList(LIGHT_GRAY.wrap("Click to " + LIGHT_GREEN.wrap("deselect") + ".")))
        );

        String colorsId = "colors";
        Map<String, WeightedItem<NightItem>> backgroundItems = new HashMap<>();
        backgroundItems.put("white", getWeighted(Material.WHITE_STAINED_GLASS_PANE, 1D));
        backgroundItems.put("lime", getWeighted(Material.LIME_STAINED_GLASS_PANE, 1D));
        backgroundItems.put("green", getWeighted(Material.GREEN_STAINED_GLASS_PANE, 1D));

        var animationDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>());
        var rewardDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>());

        animationDataMap.put("background", new SpinnerData(colorsId, SpinMode.INDEPENDENT, UNSELECTED_SLOTS,
            67, 1, 0, 15, 1, null));

        rewardDataMap.put("main", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, SELECTED_SLOTS,
            60, 1, 0, 15, 1, SOUND));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>())
            .put(DEFAULT, new RewardProvider(RARITIES));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(colorsId, new AnimationProvider(backgroundItems));
    }

    public static void setupEnclosing(@NotNull InvOpeningProvider config) {
        config.setInvType(MenuType.GENERIC_9X5);
        config.setInvTitle(TITLE);
        config.setMode(InvOpeningType.NORMAL);
        config.setMaxTicksForSkip(TICKS_TO_SKIP);
        config.setCompletionPauseTicks(COMPLETION_PAUSE_TICKS);
        config.setWinSlots(new int[]{22});

        String fillBlackId = "fill_black";
        String fillGrayId = "fill_gray";
        String fillWhiteId = "fill_white";
        String pistonSound = BukkitThing.getAsString(Sound.BLOCK_PISTON_EXTEND);

        var animationDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>());
        var rewardDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>());

        animationDataMap.put("black_1", new SpinnerData(fillBlackId, SpinMode.SYNCRHONIZED, "0,9,18,27,36,8,17,26,35,44",
            1, 1, 13, 0, 0, pistonSound));

        animationDataMap.put("black_2", new SpinnerData(fillBlackId, SpinMode.SYNCRHONIZED, "1,10,19,28,37,7,16,25,34,43",
            1, 1, 19, 0, 0, pistonSound));

        animationDataMap.put("gray_1", new SpinnerData(fillGrayId, SpinMode.SYNCRHONIZED, "2,11,20,29,38,6,15,24,33,42",
            1, 1, 25, 0, 0, pistonSound));

        animationDataMap.put("gray_2", new SpinnerData(fillGrayId, SpinMode.SYNCRHONIZED, "3,5,39,41",
            1, 1, 31, 0, 0, pistonSound));

        animationDataMap.put("gray_3", new SpinnerData(fillGrayId, SpinMode.SYNCRHONIZED, "4,40",
            1, 1, 37, 0, 0, pistonSound));

        animationDataMap.put("white_1", new SpinnerData(fillWhiteId, SpinMode.SYNCRHONIZED, "12,21,30,14,23,32",
            1, 1, 43, 0, 0, pistonSound));

        animationDataMap.put("white_2", new SpinnerData(fillWhiteId, SpinMode.SYNCRHONIZED, "13,31",
            1, 1, 49, 0, 0, pistonSound));



        rewardDataMap.put("real", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "22",
            25, 2, 0, 0, 0,
            BukkitThing.getAsString(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE))
        );

        rewardDataMap.put("dummy_1", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "0,9,18,27,36,8,17,26,35,44",
            6, 2, 0, 0, 0, null
        ));

        rewardDataMap.put("dummy_2", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "1,10,19,28,37,7,16,25,34,43",
            9, 2, 0, 0, 0, null
        ));

        rewardDataMap.put("dummy_3", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "2,11,20,29,38,6,15,24,33,42",
            12, 2, 0, 0, 0, null
        ));

        rewardDataMap.put("dummy_4", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "3,5,39,41",
            15, 2, 0, 0, 0, null
        ));

        rewardDataMap.put("dummy_5", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "4,40",
            18, 2, 0, 0, 0, null
        ));

        rewardDataMap.put("dummy_6", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "12,21,30,14,23,32",
            21, 2, 0, 0, 0, null
        ));

        rewardDataMap.put("dummy_7", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "13,31",
            24, 2, 0, 0, 0, null
        ));

        Map<String, WeightedItem<NightItem>> blackItems = new HashMap<>();
        blackItems.put("pane", getWeighted(Material.BLACK_STAINED_GLASS_PANE, 100D));

        Map<String, WeightedItem<NightItem>> grayItems = new HashMap<>();
        grayItems.put("pane", getWeighted(Material.GRAY_STAINED_GLASS_PANE, 100D));

        Map<String, WeightedItem<NightItem>> whiteItems = new HashMap<>();
        whiteItems.put("pane", getWeighted(Material.WHITE_STAINED_GLASS_PANE, 100D));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>())
            .put(DEFAULT, new RewardProvider(RARITIES));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(fillBlackId, new AnimationProvider(blackItems));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(fillGrayId, new AnimationProvider(grayItems));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(fillWhiteId, new AnimationProvider(whiteItems));
    }

    public static void setupStorm(@NotNull InvOpeningProvider config) {
        config.setInvType(MenuType.GENERIC_9X6);
        config.setInvTitle(TITLE);
        config.setMode(InvOpeningType.NORMAL);
        config.setMaxTicksForSkip(TICKS_TO_SKIP);
        config.setCompletionPauseTicks(COMPLETION_PAUSE_TICKS);
        config.setWinSlots(new int[]{49});

        String animCloud = "cloud";
        String animThunder = "thunder";
        String animClear = "clear";

        var animationDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new LinkedHashMap<>());
        var rewardDataMap = config.getSpinnerDataMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>());

        animationDataMap.put("cloud_1_down", new SpinnerData(animCloud, SpinMode.SEQUENTAL, "9,10,11,12,13",
            5, 5, 9, 0, 0, null));

        animationDataMap.put("cloud_1_up", new SpinnerData(animCloud, SpinMode.SEQUENTAL, "1,2,3",
            3, 5, 18, 0, 0, null));

        animationDataMap.put("cloud_2_up", new SpinnerData(animCloud, SpinMode.SEQUENTAL, "8,7,6,5",
            4, 5, 9, 0, 0, null));

        animationDataMap.put("clouds_2_down", new SpinnerData(animCloud, SpinMode.SEQUENTAL, "17,16,15",
            3, 5, 15, 0, 0, null));

        animationDataMap.put("cloud_2_down_clear_corner", new SpinnerData(animClear, SpinMode.SEQUENTAL, "17",
            1, 1, 30, 0, 0, null));

        animationDataMap.put("rain_sound", new SpinnerData(animClear, SpinMode.INDEPENDENT, "-1",
            1, 5, 30, 0, 0, BukkitThing.getAsString(Sound.WEATHER_RAIN_ABOVE)));

        animationDataMap.put("thunder_1", new SpinnerData(animThunder, SpinMode.SEQUENTAL, "19,29,38,48",
            4, 4, 52, 0, 0, null));

        animationDataMap.put("thunder_1_sound", new SpinnerData(animClear, SpinMode.INDEPENDENT, "-1",
            1, 1, 66, 0, 0, BukkitThing.getAsString(Sound.ENTITY_LIGHTNING_BOLT_IMPACT)));

        animationDataMap.put("thunder_1_clear", new SpinnerData(animClear, SpinMode.INDEPENDENT, "19,29,38,48",
            1, 1, 71, 0, 0, null));

        animationDataMap.put("thunder_2", new SpinnerData(animThunder, SpinMode.SEQUENTAL, "24,33,41,50",
            4, 4, 68, 0, 0, null));

        animationDataMap.put("thunder_2_sound", new SpinnerData(animClear, SpinMode.INDEPENDENT, "-1",
            1, 1, 82, 0, 0, BukkitThing.getAsString(Sound.ENTITY_LIGHTNING_BOLT_IMPACT)));

        animationDataMap.put("thunder_2_clear", new SpinnerData(animClear, SpinMode.INDEPENDENT, "24,33,41,50",
            1, 1, 87, 0, 0, null));


        rewardDataMap.put("real", new SpinnerData(DEFAULT, SpinMode.INDEPENDENT, "49",
            1, 1, 83, 0, 0,
            BukkitThing.getAsString(Sound.BLOCK_AMETHYST_BLOCK_RESONATE))
        );


        Map<String, WeightedItem<NightItem>> cloudItems = new HashMap<>();
        cloudItems.put("cloud", getWeighted(Material.WHITE_STAINED_GLASS_PANE, 100D));

        Map<String, WeightedItem<NightItem>> thunderItems = new HashMap<>();
        thunderItems.put("thunder", getWeighted(Material.YELLOW_STAINED_GLASS_PANE, 100D));

        Map<String, WeightedItem<NightItem>> clearItems = new HashMap<>();
        clearItems.put("clear", getWeighted(Material.AIR, 100D));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.REWARD, k -> new HashMap<>())
            .put(DEFAULT, new RewardProvider(RARITIES));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(animCloud, new AnimationProvider(cloudItems));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(animThunder, new AnimationProvider(thunderItems));

        config.getSpinnerProviderMap().computeIfAbsent(SpinnerType.ANIMATION, k -> new HashMap<>())
            .put(animClear, new AnimationProvider(clearItems));
    }

    @NotNull
    private static Map<String, WeightedItem<NightItem>> getRainbowPanes() {
        Map<String, WeightedItem<NightItem>> rainbowItems = new HashMap<>();
        rainbowItems.put("s1", getWeighted(Material.WHITE_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s2", getWeighted(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s3", getWeighted(Material.GRAY_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s4", getWeighted(Material.BLACK_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s5", getWeighted(Material.BROWN_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s6", getWeighted(Material.RED_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s7", getWeighted(Material.ORANGE_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s8", getWeighted(Material.YELLOW_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s9", getWeighted(Material.LIME_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s10", getWeighted(Material.GREEN_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s11", getWeighted(Material.CYAN_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s12", getWeighted(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s13", getWeighted(Material.BLUE_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s14", getWeighted(Material.PURPLE_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s15", getWeighted(Material.MAGENTA_STAINED_GLASS_PANE, 1D));
        rainbowItems.put("s16", getWeighted(Material.PINK_STAINED_GLASS_PANE, 1D));
        return rainbowItems;
    }

    private static WeightedItem<NightItem> getWeighted(@NotNull Material material, double weight) {
        return new WeightedItem<>(NightItem.fromType(material), weight);
    }
}
