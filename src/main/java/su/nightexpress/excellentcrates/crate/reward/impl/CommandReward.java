package su.nightexpress.excellentcrates.crate.reward.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.RewardType;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.reward.AbstractReward;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.problem.ProblemReporter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CommandReward extends AbstractReward {

    private String       name;
    private List<String> description;
    private List<String> commands;

    public CommandReward(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull String id, @NotNull Rarity rarity) {
        super(plugin, crate, id, rarity);
        this.setName(StringUtil.capitalizeUnderscored(id));
        this.setDescription(new ArrayList<>());
        this.setCommands(new ArrayList<>());
    }

    @Override
    protected void loadAdditional(@NotNull FileConfig config, @NotNull String path) {
        this.setName(config.getString(path + ".Name", StringUtil.capitalizeUnderscored(this.getId())));
        this.setDescription(config.getStringList(path + ".Description"));
        this.setCommands(config.getStringList(path + ".Commands"));
    }

    @Override
    protected void writeAdditional(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.name);
        config.set(path + ".Description", this.description);
        config.set(path + ".Commands", this.commands);
    }

    @Override
    protected void collectAdditionalProblems(@NotNull ProblemReporter reporter) {
        if (!this.preview.isValid()) {
            reporter.report(Lang.INSPECTIONS_REWARD_PREVIEW.get(false));
        }
        if (!this.hasContent()) {
            reporter.report(Lang.INSPECTIONS_REWARD_NO_COMMANDS.text());
        }
        else {
            this.commands.stream().filter(Predicate.not(this::isValidCommand)).forEach(command -> {
                reporter.report("Command '" + command + "' does no exist.");
            });
        }
    }

    @Override
    @NotNull
    public RewardType getType() {
        return RewardType.COMMAND;
    }

    @Override
    public boolean hasContent() {
        return !this.commands.isEmpty();
    }

    public int countCommands() {
        return this.commands.size();
    }

    public boolean hasInvalidCommands() {
        return this.commands.stream().anyMatch(Predicate.not(this::isValidCommand));
    }

    private boolean isValidCommand(@NotNull String command) {
        return CommandUtil.getCommand(command.split(" ")[0]).isPresent();
    }

    @Override
    public void giveContent(@NotNull Player player) {
        Replacer replacer = this.createContentReplacer(player).replace(Placeholders.forPlayerWithPAPI(player));

        this.getCommands().forEach(command -> {
            Players.dispatchCommand(player, replacer.apply(command));
        });
    }

    @Override
    @NotNull
    public ItemStack getPreviewItem() {
        ItemStack itemStack = ItemHelper.toItemStack(this.preview);
        ItemUtil.editMeta(itemStack, meta -> {
            ItemUtil.setCustomName(meta, this.name);
            ItemUtil.setLore(meta, this.description);
        });
        return itemStack;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public List<String> getDescription() {
        return this.description;
    }

    public void setDescription(@NotNull List<String> description) {
        this.description = description;
    }

    @NotNull
    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(@NotNull List<String> commands) {
        this.commands = Lists.modify(commands, str -> str
            // Legacy placeholder validation
            .replace("[CONSOLE]", "")
            .replace("%player%", Placeholders.PLAYER_NAME)
            .trim()
        );
        this.commands.removeIf(String::isBlank);
    }
}
