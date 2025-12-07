package su.nightexpress.excellentcrates.opening;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.opening.inventory.InventoryProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinMode;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinStep;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerBuilder;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.AnimationProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.provider.RewardProvider;
import su.nightexpress.excellentcrates.opening.selectable.SelectableProvider;
import su.nightexpress.excellentcrates.opening.world.provider.SimpleRollProvider;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.random.WeightedItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static su.nightexpress.excellentcrates.Placeholders.CRATE_NAME;
import static su.nightexpress.excellentcrates.Placeholders.DEFAULT;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class OpeningUtils {

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

    @NotNull
    public static SimpleRollProvider createSimpleRoll(@NotNull CratesPlugin plugin, @NotNull String id) {
        SimpleRollProvider provider = new SimpleRollProvider(plugin, id);
        provider.setSpinsRequired(15);
        provider.setSpinInterval(3);
        provider.setFinishDelay(40);
        return provider;
    }

    @NotNull
    public static SelectableProvider createSelectableSingle(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupSelectableProvider(plugin, id, provider -> provider.setSelectionAmount(1));
    }

    @NotNull
    public static SelectableProvider createSelectableTriple(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupSelectableProvider(plugin, id, provider -> provider.setSelectionAmount(3));
    }

    @NotNull
    private static SelectableProvider setupSelectableProvider(@NotNull CratesPlugin plugin, @NotNull String id, @NotNull Consumer<SelectableProvider> consumer) {
        SelectableProvider provider = new SelectableProvider(plugin, id);
        consumer.accept(provider);
        return provider;
    }

    @NotNull
    private static InventoryProvider setupInventoryProvider(@NotNull CratesPlugin plugin, @NotNull String id, @NotNull Consumer<InventoryProvider> consumer) {
        InventoryProvider provider = new InventoryProvider(plugin, id);
        provider.setInvTitle(BLACK.wrap("Opening " + CRATE_NAME + "..."));
        provider.setMaxTicksForSkip(40);
        provider.setCompletionPauseTicks(40);
        consumer.accept(provider);
        return provider;
    }

    @NotNull
    public static InventoryProvider setupCSGO(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupInventoryProvider(plugin, id, provider -> {
            provider.setInvType(MenuType.GENERIC_9X3);
            provider.setWinSlots(new int[]{13});

            provider.getDefaultItems().put("arrow_up", NightItem.asCustomHead("77334cddfab45d75ad28e1a47bf8cf5017d2f0982f6737da22d4972952510661")
                .setDisplayName(CYAN.wrap(BOLD.wrap("↑ Your Reward ↑")))
                .toMenuItem()
                .setSlots(22)
                .build());

            provider.getDefaultItems().put("arrow_down", NightItem.asCustomHead("e7742034f59db890c8004156b727c77ca695c4399d8e0da5ce9227cf836bb8e2")
                .setDisplayName(CYAN.wrap(BOLD.wrap("↓ Your Reward ↓")))
                .toMenuItem()
                .setSlots(4)
                .build());

            provider.addSpinner(SpinnerBuilder.rewardBuilder().name("main").mode(SpinMode.SEQUENTAL).spinnerId("normal")
                .slots(17, 16, 15, 14, 13, 12, 11, 10, 9)
                .delay(0)
                .steps(
                    SpinStep.of(12, 1),
                    SpinStep.of(12, 2),
                    SpinStep.of(12, 3),
                    SpinStep.of(12, 4),
                    SpinStep.of(5, 6),
                    SpinStep.of(3, 8),
                    SpinStep.of(2, 10),
                    SpinStep.of(1, 12)
                )
                .provider(RewardProvider.everything())
                .sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                .build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("background").mode(SpinMode.INDEPENDENT).spinnerId("rainbow")
                .slots(0, 1, 2, 3, 5, 6, 7, 8, 18, 19, 20, 21, 23, 24, 25, 26)
                .delay(0)
                .steps(
                    SpinStep.of(12, 1),
                    SpinStep.of(12, 2),
                    SpinStep.of(12, 3),
                    SpinStep.of(12, 4),
                    SpinStep.of(20, 5)
                )
                .provider(new AnimationProvider(getRainbowPanes()))
                .build()
            );
        });
    }

    @NotNull
    public static InventoryProvider setupMystery(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupInventoryProvider(plugin, id, provider -> {
            provider.setInvType(MenuType.GENERIC_9X3);
            provider.setWinSlots(new int[]{13});

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("background").mode(SpinMode.INDEPENDENT).spinnerId("rainbow")
                .slots(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)
                .delay(0)
                .steps(
                    SpinStep.of(12, 1),
                    SpinStep.of(12, 2),
                    SpinStep.of(12, 3),
                    SpinStep.of(12, 4),
                    SpinStep.of(20, 5)
                )
                .provider(new AnimationProvider(getRainbowPanes()))
                .build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder().name("main").mode(SpinMode.SEQUENTAL).spinnerId(DEFAULT)
                .slots(13)
                .delay(0)
                .steps(
                    SpinStep.of(12, 1),
                    SpinStep.of(12, 2),
                    SpinStep.of(12, 3),
                    SpinStep.of(12, 4),
                    SpinStep.of(5, 6),
                    SpinStep.of(3, 8),
                    SpinStep.of(2, 10),
                    SpinStep.of(1, 12)
                )
                .provider(RewardProvider.everything())
                .sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                .build()
            );
        });
    }

    @NotNull
    public static InventoryProvider setupRoulette(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupInventoryProvider(plugin, id, provider -> {
            provider.setInvType(MenuType.GENERIC_9X5);
            provider.setWinSlots(new int[]{20});

            provider.getDefaultItems().put("background", NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
                .toMenuItem()
                .setSlots(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 24, 25, 26, 27, 28, 29, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 12, 13, 14, 22, 23, 30, 31, 32)
                .build());

            provider.getDefaultItems().put("arrow_right", NightItem.asCustomHead("a6af217aeddf0f40064969ebb2042f7aeafbc7d0f175a27624133a3befd10281")
                .setDisplayName(LIGHT_RED.wrap(BOLD.wrap("YOUR PRIZE →")))
                .toMenuItem()
                .setSlots(19)
                .build());

            provider.getDefaultItems().put("arrow_left", NightItem.asCustomHead("1c5a8aa8a4c03600a2b5a4eb6beb51d590260b095ee1cdaa976b09bdfe5661c6")
                .setDisplayName(LIGHT_RED.wrap(BOLD.wrap("← YOUR PRIZE")))
                .toMenuItem()
                .setSlots(21)
                .build());

            provider.addSpinner(SpinnerBuilder.rewardBuilder().name("main").mode(SpinMode.SEQUENTAL).spinnerId(DEFAULT)
                .slots(39, 40, 41, 33, 24, 15, 5, 4, 3, 11, 20, 29)
                .delay(0)
                .steps(
                    SpinStep.of(12, 1),
                    SpinStep.of(12, 2),
                    SpinStep.of(12, 3),
                    SpinStep.of(12, 4),
                    SpinStep.of(5, 6),
                    SpinStep.of(3, 8),
                    SpinStep.of(2, 10),
                    SpinStep.of(1, 12)
                )
                .provider(RewardProvider.everything())
                .sound(Sound.BLOCK_NOTE_BLOCK_BANJO)
                .build()
            );
        });
    }

    @NotNull
    public static InventoryProvider setupEnclosing(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupInventoryProvider(plugin, id, provider -> {
            provider.setInvType(MenuType.GENERIC_9X5);
            provider.setWinSlots(new int[]{22});

            String fillBlackId = "fill_black";
            String fillGrayId = "fill_gray";
            String fillWhiteId = "fill_white";
            Sound pistonSound = Sound.BLOCK_PISTON_EXTEND;

            Map<String, WeightedItem<NightItem>> blackItems = new HashMap<>();
            blackItems.put("pane", getWeighted(Material.BLACK_STAINED_GLASS_PANE, 100D));

            Map<String, WeightedItem<NightItem>> grayItems = new HashMap<>();
            grayItems.put("pane", getWeighted(Material.GRAY_STAINED_GLASS_PANE, 100D));

            Map<String, WeightedItem<NightItem>> whiteItems = new HashMap<>();
            whiteItems.put("pane", getWeighted(Material.WHITE_STAINED_GLASS_PANE, 100D));

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("black_1").mode(SpinMode.SYNCRHONIZED).spinnerId(fillBlackId)
                .slots(0, 9, 18, 27, 36, 8, 17, 26, 35, 44)
                .delay(13).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(blackItems)).sound(pistonSound).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("black_2").mode(SpinMode.SYNCRHONIZED).spinnerId(fillBlackId)
                .slots(1, 10, 19, 28, 37, 7, 16, 25, 34, 43)
                .delay(19).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(blackItems)).sound(pistonSound).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("gray_1").mode(SpinMode.SYNCRHONIZED).spinnerId(fillGrayId)
                .slots(2, 11, 20, 29, 38, 6, 15, 24, 33, 42)
                .delay(25).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(grayItems)).sound(pistonSound).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("gray_2").mode(SpinMode.SYNCRHONIZED).spinnerId(fillGrayId)
                .slots(3, 5, 39, 41)
                .delay(31).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(grayItems)).sound(pistonSound).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("gray_3").mode(SpinMode.SYNCRHONIZED).spinnerId(fillGrayId)
                .slots(4, 40)
                .delay(37).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(grayItems)).sound(pistonSound).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("white_1").mode(SpinMode.SYNCRHONIZED).spinnerId(fillWhiteId)
                .slots(12, 21, 30, 14, 23, 32)
                .delay(43).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(whiteItems)).sound(pistonSound).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder().name("white_2").mode(SpinMode.SYNCRHONIZED).spinnerId(fillWhiteId)
                .slots(13, 31)
                .delay(49).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(whiteItems)).sound(pistonSound).build()
            );


            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("real").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(22)
                .delay(0).steps(SpinStep.of(25, 2)).provider(RewardProvider.everything())
                .sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_1").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(0, 9, 18, 27, 36, 8, 17, 26, 35, 44)
                .delay(0).steps(SpinStep.of(6, 2)).provider(RewardProvider.everything()).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_2").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(1, 10, 19, 28, 37, 7, 16, 25, 34, 43)
                .delay(0).steps(SpinStep.of(9, 2)).provider(RewardProvider.everything()).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_3").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(2, 11, 20, 29, 38, 6, 15, 24, 33, 42)
                .delay(0).steps(SpinStep.of(12, 2)).provider(RewardProvider.everything()).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_4").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(3, 5, 39, 41)
                .delay(0).steps(SpinStep.of(15, 2)).provider(RewardProvider.everything()).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_5").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(4, 40)
                .delay(0).steps(SpinStep.of(18, 2)).provider(RewardProvider.everything()).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_6").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(12, 21, 30, 14, 23, 32)
                .delay(0).steps(SpinStep.of(21, 2)).provider(RewardProvider.everything()).build()
            );

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("dummy_7").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(13, 31)
                .delay(0).steps(SpinStep.of(24, 2)).provider(RewardProvider.everything()).build()
            );
        });
    }

    @NotNull
    public static InventoryProvider setupStorm(@NotNull CratesPlugin plugin, @NotNull String id) {
        return setupInventoryProvider(plugin, id, provider -> {
            provider.setInvType(MenuType.GENERIC_9X6);
            provider.setWinSlots(new int[]{49});

            String animCloud = "cloud";
            String animThunder = "thunder";
            String animClear = "clear";

            Map<String, WeightedItem<NightItem>> cloudItems = new HashMap<>();
            cloudItems.put("cloud", getWeighted(Material.WHITE_STAINED_GLASS_PANE, 100D));

            Map<String, WeightedItem<NightItem>> thunderItems = new HashMap<>();
            thunderItems.put("thunder", getWeighted(Material.YELLOW_STAINED_GLASS_PANE, 100D));

            Map<String, WeightedItem<NightItem>> clearItems = new HashMap<>();
            clearItems.put("clear", getWeighted(Material.AIR, 100D));

            // Clouds

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("cloud_1_down").mode(SpinMode.SEQUENTAL).spinnerId(animCloud).slots(9, 10, 11, 12, 13)
                .delay(9).steps(SpinStep.of(5, 5)).provider(new AnimationProvider(cloudItems)).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("cloud_1_up").mode(SpinMode.SEQUENTAL).spinnerId(animCloud).slots(1, 2, 3)
                .delay(18).steps(SpinStep.of(3, 5)).provider(new AnimationProvider(cloudItems)).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("cloud_2_up").mode(SpinMode.SEQUENTAL).spinnerId(animCloud).slots(8, 7, 6, 5)
                .delay(9).steps(SpinStep.of(4, 5)).provider(new AnimationProvider(cloudItems)).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("clouds_2_down").mode(SpinMode.SEQUENTAL).spinnerId(animCloud).slots(17, 16, 15)
                .delay(15).steps(SpinStep.of(3, 5)).provider(new AnimationProvider(cloudItems)).build()
            );

            // Clearing

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("cloud_2_down_clear_corner").mode(SpinMode.INDEPENDENT).spinnerId(animClear).slots(17)
                .delay(30).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(clearItems)).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("thunder_1_clear").mode(SpinMode.INDEPENDENT).spinnerId(animClear).slots(19, 29, 38, 48)
                .delay(71).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(clearItems)).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("thunder_2_clear").mode(SpinMode.INDEPENDENT).spinnerId(animClear).slots(24, 33, 41, 50)
                .delay(87).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(clearItems)).build()
            );

            // Thunder

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("thunder_1").mode(SpinMode.SEQUENTAL).spinnerId(animThunder).slots(19, 29, 38, 48)
                .delay(52).steps(SpinStep.of(4, 4)).provider(new AnimationProvider(thunderItems)).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("thunder_2").mode(SpinMode.SEQUENTAL).spinnerId(animThunder).slots(24, 33, 41, 50)
                .delay(68).steps(SpinStep.of(4, 4)).provider(new AnimationProvider(thunderItems)).build()
            );

            // Sounds

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("rain_sound").mode(SpinMode.INDEPENDENT).spinnerId(animClear).slots(-1)
                .delay(30).steps(SpinStep.of(1, 5)).provider(new AnimationProvider(clearItems))
                .sound(Sound.WEATHER_RAIN_ABOVE).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("thunder_1_sound").mode(SpinMode.INDEPENDENT).spinnerId(animClear).slots(-1)
                .delay(66).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(clearItems))
                .sound(Sound.ENTITY_LIGHTNING_BOLT_IMPACT).build()
            );

            provider.addSpinner(SpinnerBuilder.animationBuilder()
                .name("thunder_2_sound").mode(SpinMode.INDEPENDENT).spinnerId(animClear).slots(-1)
                .delay(82).steps(SpinStep.of(1, 1)).provider(new AnimationProvider(clearItems))
                .sound(Sound.ENTITY_LIGHTNING_BOLT_IMPACT).build()
            );

            // Rewards

            provider.addSpinner(SpinnerBuilder.rewardBuilder()
                .name("main").mode(SpinMode.INDEPENDENT).spinnerId(DEFAULT)
                .slots(49)
                .delay(83).steps(SpinStep.of(1, 1))
                .provider(RewardProvider.everything())
                .sound(Sound.BLOCK_AMETHYST_BLOCK_RESONATE)
                .build()
            );
        });
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

    @NotNull
    private static WeightedItem<NightItem> getWeighted(@NotNull Material material, double weight) {
        return new WeightedItem<>(NightItem.fromType(material), weight);
    }
}
