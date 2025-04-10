package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.Comparator;

@SuppressWarnings("UnstableApiUsage")
public class RewardSortMenu extends LinkedMenu<CratesPlugin, Crate> {

    private static final String[] TEXTURES = {
        "a8432a5756a04ebf062d72a6f31bd62e8f4d82a92120336ae1972fe18d3870ba",
        "7e50c7097994313d9432142da7651dc6dd633587e2e1dd9a562abbc7878efb65",
        "5dd22db8c6e238fb8cc0819d02a65403297d63b67c6c7ce6b43bc829189837f4",
        "854c1ded92319bd83573f0f0041e730338eb7bb7997eb71ff583c2908323888e",
        "54dac7cf2017a2aefcdf29dc3832d407cbd9c8b6ba0e51a0a3169f6ffb62c015"
    };

    public RewardSortMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X3, Lang.EDITOR_TITLE_REWARD_SORT.getString());

        this.addItem(MenuItem.buildReturn(this, 22, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardList(viewer.getPlayer(), this.getLink(viewer)));
        }));

        SortMode[] modes = SortMode.values();
        for (int index = 0; index < modes.length; index++) {
            SortMode mode = modes[index];
            int slot = 2 + index;
            String texture = TEXTURES[index];
            NightItem item = NightItem.asCustomHead(texture)
                .setHideComponents(true)
                .localized(Lang.EDITOR_BUTTON_SORT_REWARDS)
                .replacement(replacer -> replacer
                    .replace(Placeholders.GENERIC_MODE, Lang.REWARD_SORT_MODE.getLocalized(mode))
                );

            this.addItem(item, slot, (viewer, event, crate) -> {
                var comparator = event.isLeftClick() ? mode.getComparator() : mode.reversed();
                this.sortRewards(viewer, crate, comparator);
            });
        }
    }

    private void sortRewards(@NotNull MenuViewer viewer, @NotNull Crate crate, @NotNull Comparator<Reward> comparator) {
        crate.setRewards(crate.getRewards().stream().sorted(comparator).toList());
        crate.saveRewards();
        this.runNextTick(() -> this.plugin.getEditorManager().openRewardList(viewer.getPlayer(), crate));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    public enum SortMode {
        WEIGHT(Comparator.comparingDouble(Reward::getWeight)),
        RARITY(Comparator.comparingDouble((Reward reward) -> reward.getRarity().getWeight())),
        CHANCE(Comparator.comparingDouble(Reward::getRollChance)),
        NAME(Comparator.comparing(reward -> NightMessage.stripTags(reward.getName()))),
        ITEM(Comparator.comparing(reward -> BukkitThing.toString(reward.getPreviewItem().getType())));

        private final Comparator<Reward> comparator;

        SortMode(@NotNull Comparator<Reward> comparator) {
            this.comparator = comparator;
        }

        @NotNull
        public Comparator<Reward> getComparator() {
            return this.comparator;
        }

        @NotNull
        public Comparator<Reward> reversed() {
            return this.comparator.reversed();
        }
    }
}
