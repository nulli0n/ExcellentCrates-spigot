package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.Comparator;

public class RewardSortEditor extends EditorMenu<CratesPlugin, Crate> implements CrateEditor {

    private static final String TEXTURE_1 = "a8432a5756a04ebf062d72a6f31bd62e8f4d82a92120336ae1972fe18d3870ba";
    private static final String TEXTURE_2 = "7e50c7097994313d9432142da7651dc6dd633587e2e1dd9a562abbc7878efb65";
    private static final String TEXTURE_3 = "5dd22db8c6e238fb8cc0819d02a65403297d63b67c6c7ce6b43bc829189837f4";
    private static final String TEXTURE_4 = "854c1ded92319bd83573f0f0041e730338eb7bb7997eb71ff583c2908323888e";
    private static final String TEXTURE_5 = "54dac7cf2017a2aefcdf29dc3832d407cbd9c8b6ba0e51a0a3169f6ffb62c015";

    public RewardSortEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_REWARD_SORT.getString(), MenuSize.CHEST_27);

        this.addReturn(22, (viewer, event, crate) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewards(viewer.getPlayer(), crate));
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_1), EditorLang.REWARD_SORT_BY_WEIGHT, 2, (viewer, event, crate) -> {
            this.sortRewards(viewer, crate, Comparator.comparingDouble(Reward::getWeight).reversed());
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_2), EditorLang.REWARD_SORT_BY_RARITY, 3, (viewer, event, crate) -> {
            this.sortRewards(viewer, crate, Comparator.comparingDouble((Reward reward) -> reward.getRarity().getWeight()).reversed());
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_3), EditorLang.REWARD_SORT_BY_CHANCE, 4, (viewer, event, crate) -> {
            this.sortRewards(viewer, crate, Comparator.comparingDouble(Reward::getRollChance).reversed());
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_4), EditorLang.REWARD_SORT_BY_NAME, 5, (viewer, event, crate) -> {
            this.sortRewards(viewer, crate, Comparator.comparing(reward -> Colorizer.restrip(reward.getNameTranslated())));
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_5), EditorLang.REWARD_SORT_BY_ITEM, 6, (viewer, event, crate) -> {
            this.sortRewards(viewer, crate, Comparator.comparing(reward -> reward.getPreview().getType().name()));
        });
    }

    private void sortRewards(@NotNull MenuViewer viewer, @NotNull Crate crate, @NotNull Comparator<Reward> comparator) {
        crate.setRewards(crate.getRewards().stream().sorted(comparator).toList());
        this.saveRewards(viewer, crate, false);
        this.runNextTick(() -> this.plugin.getEditorManager().openRewards(viewer.getPlayer(), crate));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
