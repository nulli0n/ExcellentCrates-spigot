package su.nightexpress.excellentcrates.crate.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.editor.RewardMainEditor;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.UserRewardData;
import su.nightexpress.excellentcrates.util.CrateLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public class Reward implements Placeholder {

    private final Crate crate;
    private final String id;
    private final RewardInspector inspector;

    private String name;
    private double weight;
    private Rarity rarity;
    private boolean         broadcast;
    private int             winLimitAmount;
    private long            winLimitCooldown;
    private ItemStack       preview;
    private List<ItemStack> items;
    private List<String> commands;
    private Set<String>  ignoredForPermissions;

    private RewardMainEditor editor;

    private final PlaceholderMap placeholderMap;

    public Reward(@NotNull Crate crate, @NotNull String id) {
        this(
            crate,
            id,

            Colors2.WHITE + StringUtil.capitalizeUnderscored(id),
            25D,
            crate.plugin().getCrateManager().getMostCommonRarity(),
            false,

            -1,
            0L,

            new ItemStack(Material.EMERALD),
            new ArrayList<>(),
            new ArrayList<>(),
            new HashSet<>()
        );
    }

    public Reward(
        @NotNull Crate crate,
        @NotNull String id,

        @NotNull String name,
        double weight,
        @NotNull Rarity rarity,
        boolean broadcast,

        int winLimitAmount,
        long winLimitCooldown,

        @NotNull ItemStack preview,
        @NotNull List<ItemStack> items,
        @NotNull List<String> commands,
        @NotNull Set<String> ignoredForPermissions
    ) {
        this.crate = crate;
        this.id = id.toLowerCase();

        this.setName(name);
        this.setWeight(weight);
        this.setRarity(rarity);
        this.setBroadcast(broadcast);

        this.setWinLimitAmount(winLimitAmount);
        this.setWinLimitCooldown(winLimitCooldown);

        this.setItems(items);
        this.setCommands(commands);
        this.setPreview(preview);
        this.setIgnoredForPermissions(ignoredForPermissions);

        this.inspector = new RewardInspector(this);
        this.placeholderMap = Placeholders.forReward(this);
    }

    @NotNull
    public ExcellentCratesPlugin plugin() {
        return this.getCrate().plugin();
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public RewardInspector getInspector() {
        return inspector;
    }

    @NotNull
    public RewardMainEditor getEditor() {
        if (this.editor == null) {
            this.editor = new RewardMainEditor(this);
        }
        return this.editor;
    }

    public void clear() {
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
    }

    public boolean canWin(@NotNull Player player) {
        if (this.getIgnoredForPermissions().stream().anyMatch(player::hasPermission)) {
            return false;
        }
        if (this.isWinLimitedAmount() || this.isWinLimitedCooldown()) {
            CrateUser user = plugin().getUserManager().getUserData(player);
            UserRewardData winLimit = user.getRewardWinLimit(this);
            if (winLimit == null) return true;
            return winLimit.isExpired() && !winLimit.isDrained(this);
        }
        return true;
    }

    public void giveContent(@NotNull Player player) {
        UnaryOperator<String> papi = str -> EngineUtils.hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(player, str) : str;

        this.getItems().forEach(item -> {
            ItemStack give = new ItemStack(item);

            if (Config.CRATE_PLACEHOLDER_API_FOR_REWARDS.get()) {
                ItemUtil.mapMeta(give, meta -> {
                    if (meta.hasDisplayName()) {
                        meta.setDisplayName(papi.apply(this.replacePlaceholders().apply(meta.getDisplayName())));
                    }

                    List<String> loreHas = meta.getLore();
                    if (loreHas != null) {
                        loreHas.replaceAll(this.replacePlaceholders());
                        loreHas.replaceAll(papi);
                        meta.setLore(loreHas);
                    }
                });
            }

            PlayerUtil.addItem(player, give);
        });

        this.getCommands().forEach(command -> {
            PlayerUtil.dispatchCommand(player, this.replacePlaceholders().apply(command));
        });
    }

    public void give(@NotNull Player player) {
        this.giveContent(player);

        this.plugin().getMessage(Lang.CRATE_OPEN_REWARD_INFO)
            .replace(this.getCrate().replacePlaceholders())
            .replace(this.replacePlaceholders())
            .send(player);

        if (this.isBroadcast()) {
            this.plugin().getMessage(Lang.CRATE_OPEN_REWARD_BROADCAST)
                .replace(Placeholders.forPlayer(player))
                .replace(this.getCrate().replacePlaceholders())
                .replace(this.replacePlaceholders())
                .broadcast();
        }

        if (this.isWinLimitedAmount() || this.isWinLimitedCooldown()) {
            CrateUser user = plugin().getUserManager().getUserData(player);
            UserRewardData winLimit = user.getRewardWinLimit(this);
            if (winLimit == null) winLimit = new UserRewardData(0, 0);

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

        this.getCrate().setLastReward(this.getName());

        CrateLogger.logReward(player, this);
    }

    public double getRealChance() {
        double sum = this.getCrate().getRewards().stream().mapToDouble(Reward::getWeight).sum();
        return (this.getWeight() / sum) * 100D;
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
        this.name = Colorizer.apply(name);
    }

    @Deprecated
    public double getChance() {
        return this.getWeight();
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = Math.max(0, weight);
    }

    @NotNull
    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(@NotNull Rarity rarity) {
        this.rarity = rarity;
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
    public Set<String> getIgnoredForPermissions() {
        return ignoredForPermissions;
    }

    public void setIgnoredForPermissions(@NotNull Set<String> ignoredForPermissions) {
        this.ignoredForPermissions = ignoredForPermissions;
    }

    @NotNull
    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(@NotNull List<ItemStack> items) {
        this.items = new ArrayList<>(items.stream().limit(27).toList());
        this.items.removeIf(item -> item == null || item.getType().isAir());
    }
}
