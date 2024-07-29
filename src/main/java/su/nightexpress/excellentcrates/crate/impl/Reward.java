package su.nightexpress.excellentcrates.crate.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.opening.Weighted;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.RewardWinData;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Reward implements Weighted, Placeholder {

    private final CratesPlugin   plugin;
    private final Crate          crate;
    private final String         id;
    private final PlaceholderMap placeholderMap;
    private final PlaceholderMap placeholderFullMap;

    private String          name;
    private double          weight;
    private Rarity          rarity;
    private boolean        broadcast;
    private boolean        placeholderApply;
    private RewardWinLimit playerWinLimit;
    private RewardWinLimit  globalWinLimit;
    private ItemStack       preview;
    private List<ItemStack> items;
    private List<String>    commands;
    private Set<String>     ignoredForPermissions;

    private RewardWinData globalWinData;

    @NotNull
    public static Reward createEmpty(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull String id) {
        String name = StringUtil.capitalizeUnderscored(id);
        double weight = 25D;
        Rarity rarity = plugin.getCrateManager().getDefaultRarity();
        boolean broadcast = false;
        boolean setPlaceholders = false;
        RewardWinLimit playerWinLimit = new RewardWinLimit(false, -1, 0, 1);
        RewardWinLimit globalWinLimit = new RewardWinLimit(false, -1, 0, 1);
        ItemStack preview = ItemUtil.getSkinHead(Placeholders.SKIN_NEW_REWARD);
        List<ItemStack> items = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        Set<String> ignoredPermissions = new HashSet<>();

        return new Reward(plugin, crate, id, name, weight, rarity, broadcast, setPlaceholders, playerWinLimit, globalWinLimit, preview, items, commands, ignoredPermissions);
    }

    public Reward(
        @NotNull CratesPlugin plugin,
        @NotNull Crate crate,
        @NotNull String id,

        @NotNull String name,
        double weight,
        @NotNull Rarity rarity,
        boolean broadcast,
        boolean placeholderApply,

        @NotNull RewardWinLimit playerWinLimit,
        @NotNull RewardWinLimit globalWinLimit,

        @NotNull ItemStack preview,
        @NotNull List<ItemStack> items,
        @NotNull List<String> commands,
        @NotNull Set<String> ignoredForPermissions
    ) {
        this.plugin = plugin;
        this.crate = crate;
        this.id = id.toLowerCase();

        this.setName(name);
        this.setWeight(weight);
        this.setRarity(rarity);
        this.setBroadcast(broadcast);
        this.setPlaceholderApply(placeholderApply);

        this.setPlayerWinLimit(playerWinLimit);
        this.setGlobalWinLimit(globalWinLimit);

        this.setItems(items);
        this.setCommands(commands);
        this.setPreview(preview);
        this.setIgnoredForPermissions(ignoredForPermissions);

        this.placeholderMap = Placeholders.forReward(this);
        this.placeholderFullMap = Placeholders.forRewardAll(this);
    }

    @NotNull
    public static Reward read(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        String name = config.getString(path + ".Name", id);
        double weight = config.getDouble(path + ".Weight", config.getDouble(path + ".Chance"));
        String rarityId = config.getString(path + ".Rarity", "");
        Rarity rarity = plugin.getCrateManager().getRarity(rarityId);
        if (rarity == null) rarity = plugin.getCrateManager().getDefaultRarity();

        boolean broadcast = config.getBoolean(path + ".Broadcast");
        boolean placeholderApply = config.getBoolean(path + ".Placeholder_Apply");
        ItemStack preview = config.getItemEncoded(path + ".Preview");
        if (preview == null) preview = new ItemStack(Material.BARRIER);

        if (config.contains(path + ".Win_Limits")) {
            int winLimitAmount = config.getInt(path + ".Win_Limits.Amount", -1);
            long winLimitCooldown = config.getLong(path + ".Win_Limits.Cooldown", 0L);

            RewardWinLimit winLimit = new RewardWinLimit(winLimitAmount > 0, winLimitAmount, winLimitCooldown, 1);
            winLimit.write(config, path + ".Win_Limit.Player");

            config.remove(path + ".Win_Limits");
        }

        RewardWinLimit playerLimit = RewardWinLimit.read(config, path + ".Win_Limit.Player");
        RewardWinLimit globalLimit = RewardWinLimit.read(config, path + ".Win_Limit.Global");

        List<String> commands = new ArrayList<>();
        for (String command : config.getStringList(path + ".Commands")) {
             commands.add(command
                 // Legacy placeholder validation
                .replace("[CONSOLE]", "")
                .replace("%player%", Placeholders.PLAYER_NAME)
                .trim()
            );
        }

        List<ItemStack> items = new ArrayList<>(Stream.of(config.getItemsEncoded(path + ".Items")).toList());
        Set<String> ignoredPermissions = config.getStringSet(path + ".Ignored_For_Permissions");

        return new Reward(plugin, crate, id, name, weight, rarity, broadcast, placeholderApply, playerLimit, globalLimit, preview, items, commands, ignoredPermissions);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.getName());
        config.set(path + ".Weight", this.getWeight());
        config.set(path + ".Rarity", this.getRarity().getId());
        config.set(path + ".Broadcast", this.isBroadcast());
        config.set(path + ".Placeholder_Apply", this.isPlaceholderApply());
        this.getPlayerWinLimit().write(config, path + ".Win_Limit.Player");
        this.getGlobalWinLimit().write(config, path + ".Win_Limit.Global");
        config.setItemEncoded(path + ".Preview", this.getPreview());
        config.set(path + ".Commands", this.getCommands());
        config.setItemsEncoded(path + ".Items", this.getItems());
        config.set(path + ".Ignored_For_Permissions", this.getIgnoredForPermissions());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public PlaceholderMap getAllPlaceholders() {
        return placeholderFullMap;
    }

    public void loadGlobalWinData() {
        RewardWinLimit winLimit = this.getGlobalWinLimit();
        if (!winLimit.isEnabled()) return;

        RewardWinData winData = this.plugin.getData().getRewardWinData(this);
        if (winData == null/* || winData.isExpired()*/) {
            winData = RewardWinData.create();
            //this.plugin.getData().deleteRewardWinData(this);
            this.plugin.getData().addRewardWinData(this, winData);
        }
        this.globalWinData = winData;
    }

    public void saveGlobalWinData() {
        RewardWinLimit winLimit = this.getGlobalWinLimit();
        if (!winLimit.isEnabled() || this.globalWinData == null) return;

        this.plugin.getData().saveRewardWinData(this, this.globalWinData);
    }

    public void resetGlobalWinData() {
        this.plugin.getData().deleteRewardWinData(this);

        this.loadGlobalWinData();
    }

    public boolean hasContent() {
        return !this.getItems().isEmpty() || !this.getCommands().isEmpty();
    }

    public int getWinsAmountLeft(@NotNull Player player) {
        RewardWinLimit globalLimit = this.getGlobalWinLimit();
        RewardWinLimit playerLimit = this.getPlayerWinLimit();

        int globalLeft = globalLimit.getAmount();
        int playerLeft = playerLimit.getAmount();

        if (globalLimit.isEnabled() && !globalLimit.isUnlimitedAmount()) {
            if (this.globalWinData != null) {
                if (this.globalWinData.isOut(globalLimit)) return 0;

                globalLeft = Math.max(0, globalLimit.getAmount() - this.globalWinData.getAmount());
            }
        }
        else globalLeft = -1;

        if (playerLimit.isEnabled() && !playerLimit.isUnlimitedAmount()) {
            CrateUser user = this.plugin.getUserManager().getUserData(player);
            RewardWinData winData = user.getWinData(this);
            if (winData != null) {
                if (winData.isOut(playerLimit)) return 0;

                playerLeft = Math.max(0, playerLimit.getAmount() - winData.getAmount());
            }
        }
        else playerLeft = -1;

        if (globalLeft < 0 || playerLeft < 0) {
            return Math.max(playerLeft, globalLeft);
        }

        return Math.min(playerLeft, globalLeft);
    }

    public long getWinCooldown(@NotNull Player player) {
        RewardWinLimit globalLimit = this.getGlobalWinLimit();
        RewardWinLimit playerLimit = this.getPlayerWinLimit();

        long globalLeft = 0L;
        long playerLeft = 0L;

        if (globalLimit.isEnabled()) {
            if (this.globalWinData != null && this.globalWinData.isOnCooldown()) {
                globalLeft = this.globalWinData.getExpireDate();
            }
        }

        if (playerLimit.isEnabled()) {
            CrateUser user = this.plugin.getUserManager().getUserData(player);
            RewardWinData winData = user.getWinData(this);
            if (winData != null && winData.isOnCooldown()) {
                playerLeft = winData.getExpireDate();
            }
        }

        return Math.max(playerLeft, globalLeft);
    }

    public boolean isRollable() {
        return this.getWeight() > 0D;
    }

    public boolean hasBadPermissions(@NotNull Player player) {
        return this.getIgnoredForPermissions().stream().anyMatch(player::hasPermission);
    }

    public boolean canWin(@NotNull Player player) {
        if (this.hasBadPermissions(player)) return false;

        RewardWinLimit globalLimit = this.getGlobalWinLimit();
        if (globalLimit.isEnabled()) {
            if (this.globalWinData != null && (this.globalWinData.isOut(globalLimit) || this.globalWinData.isOnCooldown())) {
                return false;
            }
        }

        RewardWinLimit playerLimit = this.getPlayerWinLimit();
        if (playerLimit.isEnabled()) {
            CrateUser user = this.plugin.getUserManager().getUserData(player);
            RewardWinData winData = user.getWinData(this);

            return winData == null || (!winData.isOut(playerLimit) && !winData.isOnCooldown());
        }
        return true;
    }

    public void giveContent(@NotNull Player player) {
        boolean doPlaceholders = this.isPlaceholderApply();

        Function<String, String> replacer;
        if (doPlaceholders || !this.getCommands().isEmpty()) {
            UnaryOperator<String> papi = str -> Plugins.hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(player, str) : str;
            UnaryOperator<String> inter = PlaceholderMap.fusion(this.getCrate().getPlaceholders(), this.getPlaceholders()).replacer();
            UnaryOperator<String> forPlayer = Placeholders.forPlayer(player);
            Function<String, String> combo = inter.andThen(forPlayer);

            if (Config.CRATE_PLACEHOLDER_API_FOR_REWARDS.get()) {
                combo = combo.andThen(papi);
            }

            replacer = combo;
        }
        else replacer = null;

        this.getItems().forEach(item -> {
            ItemStack give = new ItemStack(item);

            if (doPlaceholders) {
                ItemUtil.editMeta(give, meta -> {
                    if (meta.hasDisplayName()) {
                        meta.setDisplayName(replacer.apply(meta.getDisplayName()));
                    }

                    List<String> loreHas = meta.getLore();
                    if (loreHas != null) {
                        loreHas.replaceAll(replacer::apply);
                        meta.setLore(loreHas);
                    }
                });
            }

            Players.addItem(player, give);
        });

        this.getCommands().forEach(command -> {
            Players.dispatchCommand(player, replacer == null ? command : replacer.apply(command));
        });
    }

    public void give(@NotNull Player player) {
        this.giveContent(player);

        Lang.CRATE_OPEN_REWARD_INFO.getMessage()
            .replace(this.getCrate().replacePlaceholders())
            .replace(this.replacePlaceholders())
            .send(player);

        if (this.isBroadcast()) {
            Lang.CRATE_OPEN_REWARD_BROADCAST.getMessage()
                .replace(Placeholders.forPlayer(player))
                .replace(this.getCrate().replacePlaceholders())
                .replace(this.replacePlaceholders())
                .broadcast();
        }

        RewardWinLimit globalLimit = this.getGlobalWinLimit();
        if (globalLimit.isEnabled() && this.globalWinData != null) {
            if (!globalLimit.isUnlimitedAmount()) {
                this.globalWinData.setAmount(globalWinData.getAmount() + 1);
            }
            if (globalLimit.hasCooldown() && globalLimit.isCooldownStep(globalWinData.getAmount())) {
                this.globalWinData.setExpireDate(globalLimit.generateCooldownTimestamp());
            }
        }

        RewardWinLimit playerLimit = this.getPlayerWinLimit();
        if (playerLimit.isEnabled()) {
            CrateUser user = this.plugin.getUserManager().getUserData(player);
            RewardWinData rewardData = user.getRewardDataOrCreate(this);

            if (!playerLimit.isUnlimitedAmount() && !player.hasPermission(Perms.BYPASS_REWARD_LIMIT_AMOUNT)) {
                rewardData.setAmount(rewardData.getAmount() + 1);
            }
            if (playerLimit.hasCooldown() && playerLimit.isCooldownStep(rewardData.getAmount()) && !player.hasPermission(Perms.BYPASS_REWARD_LIMIT_COOLDOWN)) {
                rewardData.setExpireDate(playerLimit.generateCooldownTimestamp());
            }
        }

        this.getCrate().setLastReward(this.getName());

        this.plugin.getCrateLogger().logReward(player, this);
    }

    @Override
    public double getRollChance() {
        Rarity rarity = this.getRarity();

        double sum = this.crate.getRewards(rarity).stream().mapToDouble(Reward::getWeight).sum();
        double rarityChance = rarity.getRollChance(this.crate);
        double chance = (this.getWeight() / sum) * (rarityChance / 100D);

        return chance * 100D;
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

    @NotNull
    public String getNameTranslated() {
        return NightMessage.asLegacy(this.getName());
    }

    public void setName(@NotNull String name) {
        this.name = (name);
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
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

    public void setPlaceholderApply(boolean placeholderApply) {
        this.placeholderApply = placeholderApply;
    }

    public boolean isPlaceholderApply() {
        return placeholderApply;
    }

    public boolean isOneTimed() {
        return this.getPlayerWinLimit().isOneTimed() || this.getGlobalWinLimit().isOneTimed();
    }

    @NotNull
    public RewardWinLimit getWinLimit(@NotNull LimitType limitType) {
        return limitType == LimitType.PLAYER ? this.playerWinLimit : this.globalWinLimit;
    }

    @NotNull
    public RewardWinLimit getPlayerWinLimit() {
        return playerWinLimit;
    }

    public void setPlayerWinLimit(@NotNull RewardWinLimit playerWinLimit) {
        this.playerWinLimit = playerWinLimit;
    }

    @NotNull
    public RewardWinLimit getGlobalWinLimit() {
        return globalWinLimit;
    }

    public void setGlobalWinLimit(@NotNull RewardWinLimit globalWinLimit) {
        this.globalWinLimit = globalWinLimit;
    }

    @Nullable
    public RewardWinData getGlobalWinData() {
        return globalWinData;
    }

    public void setGlobalWinData(@Nullable RewardWinData globalWinData) {
        this.globalWinData = globalWinData;
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
