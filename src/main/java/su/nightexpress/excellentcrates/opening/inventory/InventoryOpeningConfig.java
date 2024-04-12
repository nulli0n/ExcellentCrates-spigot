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
    private final List<String>          scriptsOnOpen;
    private final List<String>          scriptsOnStart;

    private final int       selectionAmount;
    private final ItemStack selectedIcon;

    private final Map<String, RewardSpinSettings>    rewardSpinSettingsMap;
    private final Map<String, AnimationSpinSettings> animationSpinSettingsMap;

    public InventoryOpeningConfig(@NotNull InventoryOpening.Mode mode,
                                  boolean autoRun,
                                  int[] winSlots,
                                  @NotNull List<String> scriptsOnOpen,
                                  @NotNull List<String> scriptsOnStart,
                                  int selectionAmount,
                                  @NotNull ItemStack selectedIcon,
                                  @NotNull Map<String, RewardSpinSettings> rewardSpinSettingsMap,
                                  @NotNull Map<String, AnimationSpinSettings> animationSpinSettingsMap) {
        this.mode = mode;
        this.autoRun = autoRun;
        this.winSlots = winSlots;
        this.scriptsOnOpen = new ArrayList<>(scriptsOnOpen);
        this.scriptsOnStart = new ArrayList<>(scriptsOnStart);
        this.selectionAmount = selectionAmount;
        this.selectedIcon = selectedIcon;
        this.rewardSpinSettingsMap = new HashMap<>(rewardSpinSettingsMap);
        this.animationSpinSettingsMap = new HashMap<>(animationSpinSettingsMap);
    }

    public static InventoryOpeningConfig read(@NotNull FileConfig cfg) {
        InventoryOpening.Mode mode = ConfigValue.create("Settings.Mode",
            InventoryOpening.Mode.class,
            InventoryOpening.Mode.NORMAL
        ).read(cfg);

        int[] winSlots = ConfigValue.create("Settings.WinSlots", new int[0]).read(cfg);

        List<String> scriptsOnOpen = ConfigValue.create("Settings.ScriptRunner.OnOpen", List.of()).read(cfg);

        List<String> scriptsOnStart = ConfigValue.create("Settings.ScriptRunner.OnStart", List.of()).read(cfg);

        boolean autoRun = ConfigValue.create("Settings.Selection.AutoRun", false).read(cfg);

        int selectionAmount = ConfigValue.create("Settings.Selection.Amount", 1).read(cfg);

        ItemStack selectedIcon = ConfigValue.create("Settings.Selection.SelectedIcon",
            new ItemStack(Material.ENDER_CHEST)
        ).read(cfg);

        Map<String, RewardSpinSettings> rewardSpinSettingsMap = new HashMap<>();
        cfg.getSection("Rewards").forEach(sId -> {
            RewardSpinSettings settings = RewardSpinSettings.read(cfg, "Rewards." + sId, sId);
            rewardSpinSettingsMap.put(sId.toLowerCase(), settings);
        });

        Map<String, AnimationSpinSettings> animationSpinSettingsMap = new HashMap<>();
        cfg.getSection("Animations").forEach(sId -> {
            AnimationSpinSettings settings = AnimationSpinSettings.read(cfg, "Animations." + sId, sId);
            animationSpinSettingsMap.put(sId.toLowerCase(), settings);
        });

        return new InventoryOpeningConfig(
            mode, autoRun, winSlots,
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
