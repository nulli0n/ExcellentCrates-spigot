package su.nightexpress.excellentcrates.editor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.editor.crate.*;
import su.nightexpress.excellentcrates.editor.generic.ItemTypeMenu;
import su.nightexpress.excellentcrates.editor.key.KeyListMenu;
import su.nightexpress.excellentcrates.editor.key.KeyOptionsMenu;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.function.Consumer;

public class EditorManager extends AbstractManager<CratesPlugin> {

    private EditorMenu editorMenu;
    private ItemTypeMenu itemTypeMenu;

    private CrateListMenu       crateListMenu;
    private CrateOptionsMenu    crateOptionsMenu;
    private CrateCostsMenu      crateCostsMenu;
    private CrateParticleMenu   crateParticleMenu;
    private CrateMilestonesMenu crateMilestonesMenu;
    private CratePlacementMenu  cratePlacementMenu;
    private RewardListMenu      rewardListMenu;
    private RewardCreationMenu  rewardCreationMenu;
    private RewardOptionsMenu   rewardOptionsMenu;
    private RewardContentMenu rewardContentMenu;
    private RewardLimitsMenu    rewardLimitsMenu;
    private RewardSortMenu      rewardSortMenu;

    private KeyListMenu    keyListMenu;
    private KeyOptionsMenu keyOptionsMenu;

    public EditorManager(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.editorMenu = new EditorMenu(this.plugin);
        this.itemTypeMenu = new ItemTypeMenu(this.plugin);

        this.crateListMenu = new CrateListMenu(this.plugin);
        this.crateOptionsMenu = new CrateOptionsMenu(this.plugin);
        this.crateCostsMenu = new CrateCostsMenu(this.plugin);
        this.crateParticleMenu = new CrateParticleMenu(this.plugin);
        this.crateMilestonesMenu = new CrateMilestonesMenu(this.plugin);
        this.cratePlacementMenu = new CratePlacementMenu(this.plugin);
        this.rewardListMenu = new RewardListMenu(this.plugin);
        this.rewardCreationMenu = new RewardCreationMenu(this.plugin);
        this.rewardOptionsMenu = new RewardOptionsMenu(this.plugin);
        this.rewardContentMenu = new RewardContentMenu(this.plugin);
        this.rewardLimitsMenu = new RewardLimitsMenu(this.plugin);
        this.rewardSortMenu = new RewardSortMenu(this.plugin);

        this.keyListMenu = new KeyListMenu(this.plugin);
        this.keyOptionsMenu = new KeyOptionsMenu(this.plugin);

        this.addListener(new EditorListener(this.plugin));
    }

    @Override
    protected void onShutdown() {
        if (this.itemTypeMenu != null) this.itemTypeMenu.clear();

        if (this.crateListMenu != null) this.crateListMenu.clear();
        if (this.crateOptionsMenu != null) this.crateOptionsMenu.clear();
        if (this.crateParticleMenu != null) this.crateParticleMenu.clear();
        if (this.crateCostsMenu != null) this.crateCostsMenu.clear();
        if (this.crateMilestonesMenu != null) this.crateMilestonesMenu.clear();
        if (this.cratePlacementMenu != null) this.cratePlacementMenu.clear();
        if (this.rewardCreationMenu != null) this.rewardCreationMenu.clear();
        if (this.rewardListMenu != null) this.rewardListMenu.clear();
        if (this.rewardOptionsMenu != null) this.rewardOptionsMenu.clear();
        if (this.rewardContentMenu != null) this.rewardContentMenu.clear();
        if (this.rewardLimitsMenu != null) this.rewardLimitsMenu.clear();
        if (this.rewardSortMenu != null) this.rewardSortMenu.clear();

        if (this.keyListMenu != null) this.keyListMenu.clear();
        if (this.keyOptionsMenu != null) this.keyOptionsMenu.clear();

        if (this.editorMenu != null) this.editorMenu.clear();
    }

    public void openEditor(@NotNull Player player) {
        this.editorMenu.open(player);
    }

    public void openItemTypeMenu(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull Consumer<ItemProvider> result) {
        this.itemTypeMenu.open(player, itemStack, result);
    }



    public void openCrateList(@NotNull Player player) {
        this.crateListMenu.open(player, this.plugin.getCrateManager());
    }

    public void openOptionsMenu(@NotNull Player player, @NotNull Crate crate) {
        this.crateOptionsMenu.open(player, crate);
    }

    public void openCostsMenu(@NotNull Player player, @NotNull Crate crate) {
        this.crateCostsMenu.open(player, crate);
    }

    public void openParticleMenu(@NotNull Player player, @NotNull Crate crate) {
        this.crateParticleMenu.open(player, crate);
    }

    public void openMilestones(@NotNull Player player, @NotNull Crate crate) {
        this.crateMilestonesMenu.open(player, crate);
    }

    public void openPlacementMenu(@NotNull Player player, @NotNull Crate crate) {
        this.cratePlacementMenu.open(player, crate);
    }

    public void openRewardList(@NotNull Player player, @NotNull Crate crate) {
        this.rewardListMenu.open(player, crate);
    }

    public void openRewardCreation(@NotNull Player player, @NotNull Crate crate) {
        this.rewardCreationMenu.open(player, crate, null);
    }

    public void openRewardContent(@NotNull Player player, @NotNull ItemReward reward) {
        this.rewardContentMenu.open(player, reward);
    }

    public void openRewardSort(@NotNull Player player, @NotNull Crate crate) {
        this.rewardSortMenu.open(player, crate);
    }

    public void openRewardOptions(@NotNull Player player, @NotNull Reward reward) {
        this.rewardOptionsMenu.open(player, reward);
    }

    public void openRewardLimits(@NotNull Player player, @NotNull Reward reward, @NotNull LimitValues values) {
        this.rewardLimitsMenu.open(player, reward, values);
    }



    public void openKeyList(@NotNull Player player) {
        this.keyListMenu.open(player, this.plugin.getKeyManager());
    }

    public void openKeyOptions(@NotNull Player player, @NotNull CrateKey key) {
        this.keyOptionsMenu.open(player, key);
    }
}
