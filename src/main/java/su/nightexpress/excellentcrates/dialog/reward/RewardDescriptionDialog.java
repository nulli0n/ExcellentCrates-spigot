package su.nightexpress.excellentcrates.dialog.reward;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.dialog.generic.GenericDescriptionDialog;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

import java.util.List;

public class RewardDescriptionDialog extends GenericDescriptionDialog<CommandReward> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Reward.Description.Title").text(title("Reward", "Description"));

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
    protected List<String> getDescription(@NotNull CommandReward source) {
        return source.getDescription();
    }

    @Override
    protected void setDescription(@NotNull CommandReward source, @NotNull List<String> description) {
        source.setDescription(description);
    }

    @Override
    protected void save(@NotNull CommandReward source) {
        source.getCrate().markDirty();
    }
}
