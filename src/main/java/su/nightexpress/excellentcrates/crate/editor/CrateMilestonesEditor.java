package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateReward;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.editor.EditorLocales;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class CrateMilestonesEditor extends EditorMenu<ExcellentCrates, Crate> implements AutoPaged<Milestone> {

    private static final String TEXTURE_INVALID = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0=";

    public CrateMilestonesEditor(@NotNull ExcellentCrates plugin, @NotNull Crate crate) {
        super(plugin, crate, Config.EDITOR_TITLE_CRATE.get(), 45);

        this.addReturn(39).setClick((viewer, event) -> {
            crate.getEditor().openNextTick(viewer, 1);
        });
        this.addNextPage(44);
        this.addPreviousPage(36);

        this.addCreation(EditorLocales.MILESTONE_CREATE, 41).setClick((viewer, event) -> {
            Milestone milestone = new Milestone("null", 0, 0);
            crate.getMilestones().add(milestone);
            this.save(viewer);
        });
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.save();
        this.openNextTick(viewer, viewer.getPage());
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(0, 36).toArray();
    }

    @Override
    @NotNull
    public List<Milestone> getObjects(@NotNull Player player) {
        return this.object.getMilestones().stream().sorted(Comparator.comparing(Milestone::getOpenings)).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Milestone milestone) {
        CrateReward reward = this.object.getMilestoneReward(milestone);
        ItemStack item = new ItemStack(reward == null ? ItemUtil.createCustomHead(TEXTURE_INVALID) : reward.getPreview());
        ItemUtil.mapMeta(item, meta -> {
            meta.setDisplayName(EditorLocales.MILESTONE_OBJECT.getLocalizedName());
            meta.setLore(EditorLocales.MILESTONE_OBJECT.getLocalizedLore());
            meta.addItemFlags(ItemFlag.values());
            ItemUtil.replace(meta, milestone.replacePlaceholders());
        });

        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Milestone milestone) {
        return (viewer, event) -> {
            if (event.isShiftClick()) {
                if (event.isRightClick()) {
                    this.object.getMilestones().remove(milestone);
                    this.save(viewer);
                    return;
                }
            }

            if (event.isLeftClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_AMOUNT, wrapper -> {
                    milestone.setOpenings(wrapper.asInt());
                    this.object.save();
                    return true;
                });
            }
            else if (event.isRightClick()) {
                EditorManager.suggestValues(viewer.getPlayer(), this.object.getRewardsMap().keySet(), true);
                this.handleInput(viewer, Lang.EDITOR_MILESTONE_ENTER_REWARD, wrapper -> {
                    milestone.setRewardId(wrapper.getTextRaw());
                    this.object.save();
                    return true;
                });
            }
        };
    }
}
