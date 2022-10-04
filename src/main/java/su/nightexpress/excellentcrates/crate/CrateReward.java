package su.nightexpress.excellentcrates.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.editor.CrateEditorReward;
import su.nightexpress.excellentcrates.data.CrateUser;
import su.nightexpress.excellentcrates.data.UserRewardWinLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CrateReward implements ICrateReward {

    private final ICrate crate;
    private final String id;

    private String          name;
    private double          chance;
    private boolean         broadcast;
    private int             winLimitAmount;
    private long            winLimitCooldown;
    private ItemStack       preview;
    private List<ItemStack> items;
    private List<String>    commands;

    private CrateEditorReward editor;

    public CrateReward(@NotNull ICrate crate, @NotNull String id) {
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
        @NotNull ICrate crate,
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
        ItemStack preview = this.getPreview();
        ItemMeta meta = preview.getItemMeta();
        List<String> lore = meta != null ? meta.getLore() : null;

        String winAmount = this.isWinLimitedAmount() ? String.valueOf(this.getWinLimitAmount()) : plugin().getMessage(Lang.OTHER_INFINITY).getLocalized();
        String winCooldown = this.isWinLimitedCooldown() ? (this.getWinLimitCooldown() > 0 ? TimeUtil.formatTime(this.getWinLimitCooldown() * 1000L) : plugin().getMessage(Lang.OTHER_ONE_TIMED).getLocalized()) : plugin().getMessage(Lang.OTHER_NO).getLocalized();

        return str -> str
            .replace(Placeholders.REWARD_ID, this.getId())
            .replace(Placeholders.REWARD_NAME, this.getName())
            .replace(Placeholders.REWARD_CHANCE, NumberUtil.format(this.getChance()))
            .replace(Placeholders.REWARD_BROADCAST, LangManager.getBoolean(this.isBroadcast()))
            .replace(Placeholders.REWARD_PREVIEW_NAME, ItemUtil.getItemName(preview))
            .replace(Placeholders.REWARD_PREVIEW_LORE, String.join("\n", lore == null ? new ArrayList<>() : lore))
            .replace(Placeholders.REWARD_COMMANDS, String.join(DELIMITER_DEFAULT, this.getCommands()))
            .replace(Placeholders.REWARD_WIN_LIMIT_AMOUNT, winAmount)
            .replace(Placeholders.REWARD_WIN_LIMIT_COOLDOWN, winCooldown)
            ;
    }

    @Override
    @NotNull
    public CrateEditorReward getEditor() {
        if (this.editor == null) {
            this.editor = new CrateEditorReward(this.plugin(), this);
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

    @Override
    @NotNull
    public String getId() {
        return this.id;
    }

    @Override
    @NotNull
    public ICrate getCrate() {
        return this.crate;
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = StringUtil.color(name);
    }

    @Override
    public double getChance() {
        return this.chance;
    }

    @Override
    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public boolean isBroadcast() {
        return broadcast;
    }

    @Override
    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public int getWinLimitAmount() {
        return winLimitAmount;
    }

    @Override
    public void setWinLimitAmount(int winLimitAmount) {
        this.winLimitAmount = winLimitAmount;
    }

    @Override
    public long getWinLimitCooldown() {
        return winLimitCooldown;
    }

    @Override
    public void setWinLimitCooldown(long winLimitCooldown) {
        this.winLimitCooldown = winLimitCooldown;
    }

    @Override
    public boolean canWin(@NotNull Player player) {
        if (this.isWinLimitedAmount() || this.isWinLimitedCooldown()) {
            CrateUser user = plugin().getUserManager().getUserData(player);
            UserRewardWinLimit winLimit = user.getRewardWinLimit(this);
            if (winLimit == null) return true;
            if (!winLimit.isExpired() || winLimit.isDrained(this)) return false;
        }
        return true;
    }

    @Override
    @NotNull
    public ItemStack getPreview() {
        return new ItemStack(this.preview);
    }

    @Override
    public void setPreview(@NotNull ItemStack item) {
        this.preview = new ItemStack(item);
    }

    @Override
    @NotNull
    public List<String> getCommands() {
        return this.commands;
    }

    @Override
    public void setCommands(@NotNull List<String> commands) {
        this.commands = new ArrayList<>(commands);
        this.commands.removeIf(String::isEmpty);
    }

    @Override
    @NotNull
    public List<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void setItems(@NotNull List<ItemStack> items) {
        this.items = new ArrayList<>(items);
        this.items.removeIf(item -> item == null || item.getType().isAir());
    }

    @Override
    public void give(@NotNull Player player) {
        this.getItems().forEach(item -> {
            ItemUtil.setPlaceholderAPI(player, item);
            PlayerUtil.addItem(player, item);
        });
        this.getCommands().forEach(cmd -> PlayerUtil.dispatchCommand(player, cmd));

        this.plugin().getMessage(Lang.CRATE_OPEN_REWARD_INFO)
            .replace(Placeholders.CRATE_NAME, crate.getName())
            .replace(Placeholders.REWARD_NAME, this.getName())
            .send(player);

        if (this.isBroadcast()) {
            this.plugin().getMessage(Lang.CRATE_OPEN_REWARD_BROADCAST)
                .replace("%player%", player.getName())
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
