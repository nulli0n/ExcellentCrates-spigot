package su.nightexpress.excellentcrates.crate.reward.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.RewardType;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.reward.AbstractReward;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CommandReward extends AbstractReward {

    //private ItemProvider preview;
    private String       name;
    private List<String> description;
    private List<String> commands;

    public CommandReward(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull String id, @NotNull Rarity rarity) {
        super(plugin, crate, id, rarity);
        this.setName(StringUtil.capitalizeUnderscored(id));
        this.setDescription(new ArrayList<>());
        this.setCommands(new ArrayList<>());
        this.setPreview(ItemTypes.vanilla(new ItemStack(Material.COMMAND_BLOCK)));
    }

    @Override
    protected void loadAdditional(@NotNull FileConfig config, @NotNull String path) {
        this.setName(config.getString(path + ".Name", StringUtil.capitalizeUnderscored(this.getId())));
        this.setDescription(config.getStringList(path + ".Description"));
//        this.setPreview(ItemTypes.read(config, path + ".PreviewData"));
        this.setCommands(config.getStringList(path + ".Commands"));
    }

    @Override
    protected void writeAdditional(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.name);
        config.set(path + ".Description", this.description);
//        config.set(path + ".PreviewData", this.preview);
        config.set(path + ".Commands", this.commands);
    }

    @Override
    @NotNull
    public UnaryOperator<String> replaceAllPlaceholders() {
        return Placeholders.COMMAND_REWARD_EDITOR.replacer(this);
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

    @Override
    public void giveContent(@NotNull Player player) {
        Replacer replacer = this.createContentReplacer(player);

        this.getCommands().forEach(command -> {
            if (this.placeholderApply) {
                command = replacer.apply(command);
            }

            Players.dispatchCommand(player, command);
        });
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

//    @NotNull
//    public ItemProvider getPreview() {
//        return this.preview;
//    }
//
//    public void setPreview(@NotNull ItemProvider provider) {
//        this.preview = provider;
//    }

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
