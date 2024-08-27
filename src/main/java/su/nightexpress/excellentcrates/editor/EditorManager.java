package su.nightexpress.excellentcrates.editor;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.crate.menu.CratesEditorMenu;
import su.nightexpress.excellentcrates.editor.crate.*;
import su.nightexpress.excellentcrates.editor.key.KeyListEditor;
import su.nightexpress.excellentcrates.editor.key.KeyMainEditor;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.manager.AbstractManager;

public class EditorManager extends AbstractManager<CratesPlugin> {

    private CratesEditorMenu editorMenu;

    private CrateListEditor cratesEditor;
    private CrateMainEditor crateSettingsEditor;
    private CrateParticleEditor crateParticleEditor;
    private CrateMilestonesEditor milestonesEditor;
    private CratePlacementEditor placementEditor;
    private RewardListEditor rewardsEditor;
    private RewardMainEditor rewardSettingsEditor;
    private RewardSortEditor rewardSortEditor;

    private KeyListEditor keysEditor;
    private KeyMainEditor keySettingsEditor;

    public EditorManager(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.editorMenu = new CratesEditorMenu(this.plugin);

        this.cratesEditor = new CrateListEditor(this.plugin);
        this.crateSettingsEditor = new CrateMainEditor(this.plugin);
        this.crateParticleEditor = new CrateParticleEditor(this.plugin);
        this.milestonesEditor = new CrateMilestonesEditor(this.plugin);
        this.placementEditor = new CratePlacementEditor(this.plugin);
        this.rewardsEditor = new RewardListEditor(this.plugin);
        this.rewardSettingsEditor = new RewardMainEditor(this.plugin);
        this.rewardSortEditor = new RewardSortEditor(this.plugin);

        this.keysEditor = new KeyListEditor(this.plugin);
        this.keySettingsEditor = new KeyMainEditor(this.plugin);

        this.addListener(new EditorListener(this.plugin));
    }

    @Override
    protected void onShutdown() {
        if (this.cratesEditor != null) this.cratesEditor.clear();
        if (this.crateSettingsEditor != null) this.crateSettingsEditor.clear();
        if (this.crateParticleEditor != null) this.crateParticleEditor.clear();
        if (this.milestonesEditor != null) this.milestonesEditor.clear();
        if (this.placementEditor != null) this.placementEditor.clear();
        if (this.rewardsEditor != null) this.rewardsEditor.clear();
        if (this.rewardSettingsEditor != null) this.rewardSettingsEditor.clear();
        if (this.rewardSortEditor != null) this.rewardSortEditor.clear();

        if (this.keysEditor != null) this.keysEditor.clear();
        if (this.keySettingsEditor != null) this.keySettingsEditor.clear();

        if (this.editorMenu != null) this.editorMenu.clear();
    }

    public void openEditor(@NotNull Player player) {
        this.editorMenu.open(player, this.plugin);
    }


    public void openCrateList(@NotNull Player player) {
        this.cratesEditor.open(player, this.plugin.getCrateManager());
    }

    public void openCrate(@NotNull Player player, @NotNull Crate crate) {
        this.crateSettingsEditor.open(player, crate);
    }

    public void openParticle(@NotNull Player player, @NotNull Crate crate) {
        this.crateParticleEditor.open(player, crate);
    }

    public void openMilestones(@NotNull Player player, @NotNull Crate crate) {
        this.milestonesEditor.open(player, crate);
    }

    public void openPlacement(@NotNull Player player, @NotNull Crate crate) {
        this.placementEditor.open(player, crate);
    }

    public void openRewards(@NotNull Player player, @NotNull Crate crate) {
        this.rewardsEditor.open(player, crate);
    }

    public void openRewardsSort(@NotNull Player player, @NotNull Crate crate) {
        this.rewardSortEditor.open(player, crate);
    }

    public void openReward(@NotNull Player player, @NotNull Reward reward) {
        this.rewardSettingsEditor.open(player, reward);
    }


    public void openKeyList(@NotNull Player player) {
        this.keysEditor.open(player, this.plugin.getKeyManager());
    }

    public void openKey(@NotNull Player player, @NotNull CrateKey key) {
        this.keySettingsEditor.open(player, key);
    }
}
