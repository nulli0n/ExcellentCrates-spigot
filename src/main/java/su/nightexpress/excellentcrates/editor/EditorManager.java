package su.nightexpress.excellentcrates.editor;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.editor.crate.*;
import su.nightexpress.excellentcrates.editor.key.KeyListMenu;
import su.nightexpress.excellentcrates.editor.key.KeyOptionsMenu;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.manager.AbstractManager;

public class EditorManager extends AbstractManager<CratesPlugin> {

    private EditorMenu editorMenu;

    private CrateListMenu       crateListMenu;
    private CrateOptionsMenu    crateOptionsMenu;
    private CostsListMenu costsListMenu;
    private CostOptionsMenu costOptionsMenu;
    private CrateMilestonesMenu crateMilestonesMenu;
    private RewardListMenu      rewardListMenu;
    private RewardOptionsMenu   rewardOptionsMenu;
    private RewardContentMenu rewardContentMenu;

    private KeyListMenu    keyListMenu;
    private KeyOptionsMenu keyOptionsMenu;

    public EditorManager(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.editorMenu = new EditorMenu(this.plugin);

        this.crateListMenu = new CrateListMenu(this.plugin);
        this.crateOptionsMenu = new CrateOptionsMenu(this.plugin);
        this.costsListMenu = new CostsListMenu(this.plugin);
        this.costOptionsMenu = new CostOptionsMenu(this.plugin);
        this.crateMilestonesMenu = new CrateMilestonesMenu(this.plugin);
        this.rewardListMenu = new RewardListMenu(this.plugin);
        this.rewardOptionsMenu = new RewardOptionsMenu(this.plugin);
        this.rewardContentMenu = new RewardContentMenu(this.plugin);

        this.keyListMenu = new KeyListMenu(this.plugin);
        this.keyOptionsMenu = new KeyOptionsMenu(this.plugin);
    }

    @Override
    protected void onShutdown() {
        if (this.crateListMenu != null) this.crateListMenu.clear();
        if (this.crateOptionsMenu != null) this.crateOptionsMenu.clear();
        if (this.costsListMenu != null) this.costsListMenu.clear();
        if (this.costOptionsMenu != null) this.costOptionsMenu.clear();
        if (this.crateMilestonesMenu != null) this.crateMilestonesMenu.clear();
        if (this.rewardListMenu != null) this.rewardListMenu.clear();
        if (this.rewardOptionsMenu != null) this.rewardOptionsMenu.clear();
        if (this.rewardContentMenu != null) this.rewardContentMenu.clear();

        if (this.keyListMenu != null) this.keyListMenu.clear();
        if (this.keyOptionsMenu != null) this.keyOptionsMenu.clear();

        if (this.editorMenu != null) this.editorMenu.clear();
    }

    public void openEditor(@NotNull Player player) {
        this.editorMenu.open(player);
    }



    public void openCrateList(@NotNull Player player) {
        this.crateListMenu.open(player, this.plugin.getCrateManager());
    }

    public void openOptionsMenu(@NotNull Player player, @NotNull Crate crate) {
        this.crateOptionsMenu.open(player, crate);
    }

    public void openCosts(@NotNull Player player, @NotNull Crate crate) {
        this.costsListMenu.open(player, crate);
    }

    public void openCostOptions(@NotNull Player player, @NotNull Crate crate, @NotNull Cost cost) {
        this.costOptionsMenu.open(player, crate, cost);
    }

    public void openMilestones(@NotNull Player player, @NotNull Crate crate) {
        this.crateMilestonesMenu.open(player, crate);
    }

    public void openRewardList(@NotNull Player player, @NotNull Crate crate) {
        this.rewardListMenu.open(player, crate);
    }

    public void openRewardContent(@NotNull Player player, @NotNull ItemReward reward) {
        this.rewardContentMenu.open(player, reward);
    }

    public void openRewardOptions(@NotNull Player player, @NotNull Reward reward) {
        this.rewardOptionsMenu.open(player, reward);
    }



    public void openKeyList(@NotNull Player player) {
        this.keyListMenu.open(player, this.plugin.getKeyManager());
    }

    public void openKeyOptions(@NotNull Player player, @NotNull CrateKey key) {
        this.keyOptionsMenu.open(player, key);
    }
}
