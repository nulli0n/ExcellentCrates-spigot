package su.nightexpress.excellentcrates.opening.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.opening.spinner.AnimationSpinSettings;
import su.nightexpress.excellentcrates.opening.spinner.RewardSpinSettings;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryOpeningConfig {

    private final InventoryOpening.Mode mode;
    private final boolean               autoRun;
    private final int[]                 winSlots;
    private final long                  maxTicksForSkip;
    private final List<String>          scriptsOnOpen;
    private final List<String>          scriptsOnStart;

    private final int       selectionAmount;
    private final ItemStack selectedIcon;

    private final Map<String, RewardSpinSettings>    rewardSpinSettingsMap;
    private final Map<String, AnimationSpinSettings> animationSpinSettingsMap;

    public InventoryOpeningConfig(@NotNull InventoryOpening.Mode mode,
                                  boolean autoRun,
                                  int[] winSlots,
                                  long maxTicksForSkip,
                                  @NotNull List<String> scriptsOnOpen,
                                  @NotNull List<String> scriptsOnStart,
                                  int selectionAmount,
                                  @NotNull ItemStack selectedIcon,
                                  @NotNull Map<String, RewardSpinSettings> rewardSpinSettingsMap,
                                  @NotNull Map<String, AnimationSpinSettings> animationSpinSettingsMap) {
        this.mode = mode;
        this.autoRun = autoRun;
        this.winSlots = winSlots;
        this.maxTicksForSkip = maxTicksForSkip;
        this.scriptsOnOpen = new ArrayList<>(scriptsOnOpen);
        this.scriptsOnStart = new ArrayList<>(scriptsOnStart);
        this.selectionAmount = selectionAmount;
        this.selectedIcon = selectedIcon;
        this.rewardSpinSettingsMap = new HashMap<>(rewardSpinSettingsMap);
        this.animationSpinSettingsMap = new HashMap<>(animationSpinSettingsMap);
    }

    @NotNull
    public static InventoryOpeningConfig read(@NotNull FileConfig config) {
        InventoryOpening.Mode mode = ConfigValue.create("Settings.Mode",
            InventoryOpening.Mode.class,
            InventoryOpening.Mode.NORMAL
        ).read(config);

        int[] winSlots = ConfigValue.create("Settings.WinSlots", new int[0]).read(config);

        long maxTicksForSkip = ConfigValue.create("Settings.Max_Ticks_To_Skip",
            40L,
            "Sets max. amount of the opening ticks while players can skip the opening animation.",
            "Set to -1 to disable (no skip)."
        ).read(config);

        List<String> scriptsOnOpen = ConfigValue.create("Settings.ScriptRunner.OnOpen", List.of()).read(config);

        List<String> scriptsOnStart = ConfigValue.create("Settings.ScriptRunner.OnStart", List.of()).read(config);

        boolean autoRun = ConfigValue.create("Settings.Selection.AutoRun", false).read(config);

        int selectionAmount = ConfigValue.create("Settings.Selection.Amount", 1).read(config);

        ItemStack selectedIcon = ConfigValue.create("Settings.Selection.SelectedIcon",
            new ItemStack(Material.ENDER_CHEST)
        ).read(config);

        Map<String, RewardSpinSettings> rewardSpinSettingsMap = new HashMap<>();
        config.getSection("Rewards").forEach(sId -> {
            RewardSpinSettings settings = RewardSpinSettings.read(config, "Rewards." + sId, sId);
            rewardSpinSettingsMap.put(sId.toLowerCase(), settings);
        });

        Map<String, AnimationSpinSettings> animationSpinSettingsMap = new HashMap<>();
        config.getSection("Animations").forEach(sId -> {
            AnimationSpinSettings settings = AnimationSpinSettings.read(config, "Animations." + sId, sId);
            animationSpinSettingsMap.put(sId.toLowerCase(), settings);
        });

        return new InventoryOpeningConfig(
            mode, autoRun, winSlots, maxTicksForSkip,
            scriptsOnOpen, scriptsOnStart,
            selectionAmount, selectedIcon,
            rewardSpinSettingsMap,
            animationSpinSettingsMap
        );
    }

    @NotNull
    public InventoryOpening.Mode getMode() {
        return mode;
    }

    public int[] getWinSlots() {
        return winSlots;
    }

    public long getMaxTicksForSkip() {
        return maxTicksForSkip;
    }

    @NotNull
    public List<String> getScriptsOnOpen() {
        return scriptsOnOpen;
    }

    @NotNull
    public List<String> getScriptsOnStart() {
        return scriptsOnStart;
    }

    public boolean isAutoRun() {
        return autoRun;
    }

    public int getSelectionAmount() {
        return selectionAmount;
    }

    @NotNull
    public ItemStack getSelectedIcon() {
        return selectedIcon;
    }

    @NotNull
    public Map<String, RewardSpinSettings> getRewardSpinSettingsMap() {
        return this.rewardSpinSettingsMap;
    }

    @NotNull
    public Map<String, AnimationSpinSettings> getAnimationSpinSettingsMap() {
        return animationSpinSettingsMap;
    }
}
