package su.nightexpress.excellentcrates.dialog.reward;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.dialog.generic.GenericNameDialog;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class RewardNameDialog extends GenericNameDialog<CommandReward> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Reward.Name.Title").text(title("Reward", "Name"));

    @Override
    @NotNull
    protected TextLocale title() {
        return TITLE;
    }

    @Override
    @NotNull
    protected AdaptedItem getItem(@NotNull CommandReward source) {
        return source.getPreview();
    }

    @Override
    protected void setItem(@NotNull CommandReward source, @NotNull AdaptedItem item) {
        source.setPreview(item);
    }

    @Override
    @NotNull
    protected String getName(@NotNull CommandReward source) {
        return source.getName();
    }

    @Override
    protected void setName(@NotNull CommandReward source, @NotNull String name) {
        source.setName(name);
    }

    @Override
    protected void save(@NotNull CommandReward source) {
        source.getCrate().markDirty();
    }
}
