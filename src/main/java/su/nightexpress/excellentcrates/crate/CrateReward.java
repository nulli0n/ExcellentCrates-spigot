package su.nightexpress.excellentcrates.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.manager.IEditable;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.editor.EditorCrateReward;
import su.nightexpress.excellentcrates.data.CrateUser;
import su.nightexpress.excellentcrates.data.UserRewardWinLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CrateReward implements IEditable, ICleanable, IPlaceholder {

    private final Crate crate;
    private final String id;

    private String          name;
    private double          chance;
    private boolean         broadcast;
    private int             winLimitAmount;
    private long            winLimitCooldown;
    private ItemStack       preview;
    private List<ItemStack> items;
    private List<String>    commands;

    private EditorCrateReward editor;

    public CrateReward(@NotNull Crate crate, @NotNull String id) {
        this(
            crate,
            id,

            "&a" + StringUtil.capitalizeFully(id) + " Reward",
            25D,
            false,

            -1,
            0L,

            new ItemStack(Material.EMERALD),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public CrateReward(
        @NotNull Crate crate,
        @NotNull String id,

        @NotNull String name,
        double chance,
        boolean broadcast,

        int winLimitAmount,
        long winLimitCooldown,

        @NotNull ItemStack preview,
        @NotNull List<ItemStack> items,
        @NotNull List<String> commands
    ) {
        this.crate = crate;
        this.id = id.toLowerCase();

        this.setChance(chance);
        this.setName(name);
        this.setBroadcast(broadcast);

        this.setWinLimitAmount(winLimitAmount);
        this.setWinLimitCooldown(winLimitCooldown);

        this.setItems(items);
        this.setCommands(commands);
        this.setPreview(preview);
    }

    @NotNull
    public ExcellentCrates plugin() {
        return this.getCrate().plugin();
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        String winAmount = this.isWinLimitedAmount() ? String.valueOf(this.getWinLimitAmount()) : plugin().getMessage(Lang.OTHER_INFINITY).getLocalized();
        String winCooldown = this.isWinLimitedCooldown() ? (this.getWinLimitCooldown() > 0 ? TimeUtil.formatTime(this.getWinLimitCooldown() * 1000L) : plugin().getMessage(Lang.OTHER_ONE_TIMED).getLocalized()) : plugin().getMessage(Lang.OTHER_NO).getLocalized();

        return str -> str
            .replace(Placeholders.REWARD_ID, this.getId())
            .replace(Placeholders.REWARD_NAME, this.getName())
            .replace(Placeholders.REWARD_CHANCE, NumberUtil.format(this.getChance()))
            .replace(Placeholders.REWARD_BROADCAST, LangManager.getBoolean(this.isBroadcast()))
            .replace(Placeholders.REWARD_PREVIEW_NAME, ItemUtil.getItemName(this.getPreview()))
            .replace(Placeholders.REWARD_PREVIEW_LORE, String.join("\n", ItemUtil.getLore(this.getPreview())))
            .replace(Placeholders.REWARD_COMMANDS, String.join(DELIMITER_DEFAULT, this.getCommands()))
            .replace(Placeholders.REWARD_WIN_LIMIT_AMOUNT, winAmount)
            .replace(Placeholders.REWARD_WIN_LIMIT_COOLDOWN, winCooldown)
            ;
    }

    @Override
    @NotNull
    public EditorCrateReward getEditor() {
        if (this.editor == null) {
            this.editor = new EditorCrateReward(this);
        }
        return this.editor;
    }

    @Override
    public void clear() {
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public Crate getCrate() {
        return this.crate;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = StringUtil.color(name);
    }

    public double getChance() {
        return this.chance;
    }

    public void setChance(double chance) {
        this.chance = Math.max(0, chance);
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public boolean isWinLimitedAmount() {
        return this.getWinLimitAmount() >= 0;
    }

    public boolean isWinLimitedCooldown() {
        return this.getWinLimitCooldown() != 0;
    }

    public boolean isWinLimitedOnce() {
        return this.getWinLimitAmount() == 1 || this.getWinLimitCooldown() < 0;
    }

    public int getWinLimitAmount() {
        return winLimitAmount;
    }

    public void setWinLimitAmount(int winLimitAmount) {
        this.winLimitAmount = winLimitAmount;
    }

    public long getWinLimitCooldown() {
        return winLimitCooldown;
    }

    public void setWinLimitCooldown(long winLimitCooldown) {
        this.winLimitCooldown = winLimitCooldown;
    }

    public boolean canWin(@NotNull Player player) {
        if (this.isWinLimitedAmount() || this.isWinLimitedCooldown()) {
            CrateUser user = plugin().getUserManager().getUserData(player);
            UserRewardWinLimit winLimit = user.getRewardWinLimit(this);
            if (winLimit == null) return true;
            if (!winLimit.isExpired() || winLimit.isDrained(this)) return false;
        }
        return true;
    }

    @NotNull
    public ItemStack getPreview() {
        return new ItemStack(this.preview);
    }

    public void setPreview(@NotNull ItemStack item) {
        this.preview = new ItemStack(item);
    }

    @NotNull
    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(@NotNull List<String> commands) {
        this.commands = new ArrayList<>(commands);
        this.commands.removeIf(String::isEmpty);
    }

    @NotNull
    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(@NotNull List<ItemStack> items) {
        this.items = new ArrayList<>(items);
        this.items.removeIf(item -> item == null || item.getType().isAir());
    }

    public void give(@NotNull Player player) {
        this.getItems().forEach(item -> {
            ItemStack give = new ItemStack(item);
            ItemUtil.setPlaceholderAPI(player, give);
            PlayerUtil.addItem(player, give);
        });
        this.getCommands().forEach(cmd -> PlayerUtil.dispatchCommand(player, cmd));

        this.plugin().getMessage(Lang.CRATE_OPEN_REWARD_INFO)
            .replace(Placeholders.CRATE_NAME, crate.getName())
            .replace(Placeholders.REWARD_NAME, this.getName())
            .send(player);

        if (this.isBroadcast()) {
            this.plugin().getMessage(Lang.CRATE_OPEN_REWARD_BROADCAST)
                .replace(Placeholders.Player.replacer(player))
                .replace(Placeholders.CRATE_NAME, crate.getName())
                .replace(Placeholders.REWARD_NAME, this.getName())
                .broadcast();
        }

        if (this.isWinLimitedAmount() || this.isWinLimitedCooldown()) {
            CrateUser user = plugin().getUserManager().getUserData(player);
            UserRewardWinLimit winLimit = user.getRewardWinLimit(this);
            if (winLimit == null) winLimit = new UserRewardWinLimit(0, 0);

            if (!player.hasPermission(Perms.BYPASS_REWARD_LIMIT_AMOUNT)) {
                if (this.isWinLimitedAmount()) winLimit.setAmount(winLimit.getAmount() + 1);
            }
            if (!player.hasPermission(Perms.BYPASS_REWARD_LIMIT_COOLDOWN)) {
                if (this.isWinLimitedCooldown()) {
                    winLimit.setExpireDate(this.getWinLimitCooldown() < 0 ? -1L : System.currentTimeMillis() + this.getWinLimitCooldown() * 1000L);
                }
            }
            user.setRewardWinLimit(this, winLimit);
        }
    }
}
