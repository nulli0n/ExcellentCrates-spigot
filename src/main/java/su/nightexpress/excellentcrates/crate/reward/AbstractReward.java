package su.nightexpress.excellentcrates.crate.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.limit.CooldownMode;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.excellentcrates.data.reward.RewardData;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.problem.ProblemCollector;
import su.nightexpress.nightcore.util.problem.ProblemReporter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

public abstract class AbstractReward implements Reward {

    protected final CratesPlugin plugin;
    protected final Crate        crate;
    protected final String       id;

    protected AdaptedItem preview;
    protected double      weight;
    protected Rarity      rarity;
    protected boolean     broadcast;
    protected LimitValues limits;
    protected Set<String> ignoredPermissions;
    protected Set<String> requiredPermissions;

    public AbstractReward(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull String id, @NotNull Rarity rarity) {
        this.plugin = plugin;
        this.crate = crate;
        this.id = id.toLowerCase();

        this.setWeight(10D);
        this.setRarity(rarity);
        this.setPreview(ItemHelper.vanilla(CrateUtils.getQuestionStack()));
        this.setBroadcast(false);
        this.setLimits(LimitValues.unlimited());
        this.setIgnoredPermissions(new HashSet<>());
        this.setRequiredPermissions(new HashSet<>());
    }

    @Override
    public void load(@NotNull FileConfig config, @NotNull String path) {
        if (config.contains(path + ".Win_Limit")) {
            boolean playerEnabled = config.getBoolean(path + ".Win_Limit.Player.Enabled", false);
            int playerAmount = config.getInt(path + ".Win_Limit.Player.Amount", -1);
            long playerCooldown = config.getLong(path + ".Win_Limit.Player.Cooldown");

            boolean globalEnabled = config.getBoolean(path + ".Win_Limit.Global.Enabled", false);
            int  globalAmount = config.getInt(path + ".Win_Limit.Global.Amount", -1);
            long  globalCooldown = config.getLong(path + ".Win_Limit.Global.Cooldown");

            CooldownMode cooldownType = playerCooldown == -2 || globalCooldown == -2 ? CooldownMode.DAILY : CooldownMode.CUSTOM;

            LimitValues values = new LimitValues(playerEnabled || globalEnabled, cooldownType, globalAmount, playerAmount, globalCooldown, playerCooldown);
            config.set(path + ".Limits", values);
            config.remove(path + ".Win_Limit");
        }

        this.setPreview(ItemHelper.readOrPlaceholder(config, path + ".PreviewData"));
        this.setWeight(config.getDouble(path + ".Weight", -1D));
        this.setBroadcast(config.getBoolean(path + ".Broadcast"));
        this.setLimits(LimitValues.read(config, path + ".Limits"));
        this.setIgnoredPermissions(config.getStringSet(path + ".Ignored_For_Permissions"));
        this.setRequiredPermissions(config.getStringSet(path + ".Required_Permissions"));

        this.loadAdditional(config, path);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Type", this.getType().name());
        config.set(path + ".PreviewData", this.preview);
        config.set(path + ".Weight", this.weight);
        config.set(path + ".Rarity", this.rarity.getId());
        config.set(path + ".Broadcast", this.broadcast);
        config.set(path + ".Limits", this.limits);
        config.set(path + ".Ignored_For_Permissions", this.ignoredPermissions);
        config.set(path + ".Required_Permissions", this.requiredPermissions);
        this.writeAdditional(config, path);
    }

    protected abstract void loadAdditional(@NotNull FileConfig config, @NotNull String path);

    protected abstract void writeAdditional(@NotNull FileConfig config, @NotNull String path);

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.REWARD.replacer(this);
    }

    @NotNull
    protected Replacer createContentReplacer(@NotNull Player player) {
        return Replacer.create().replace(this.crate.replacePlaceholders()).replace(this.replacePlaceholders());
    }

    @Override
    @NotNull
    public ProblemReporter collectProblems() {
        ProblemReporter reporter = new ProblemCollector(this.getId(), this.crate.getPath() + " -> " + this.id);

        this.collectAdditionalProblems(reporter);

        return reporter;
    }

    protected abstract void collectAdditionalProblems(@NotNull ProblemReporter reporter);

    @Override
    public boolean hasProblems() {
        return !this.collectProblems().isEmpty();
    }

    @Override
    public boolean isOnCooldown(@NotNull Player player) {
        if (!this.limits.isEnabled()) return false;

        if (this.limits.hasGlobalCooldown()) {
            RewardData globalLimit = this.plugin.getDataManager().getRewardLimit(this, null);
            if (globalLimit != null && globalLimit.isOnCooldown()) return true;
        }

        if (this.limits.hasPlayerCooldown()) {
            RewardData playerLimit = this.plugin.getDataManager().getRewardLimit(this, player);
            return playerLimit != null && playerLimit.isOnCooldown();
        }

        return false;
    }

    @Override
    public int getAvailableRolls(@NotNull Player player) {
        RewardData globalLimit = this.plugin.getDataManager().getRewardLimit(this, null);
        RewardData playerLimit = this.plugin.getDataManager().getRewardLimit(this, player);

        int globalLeft = -1;
        int playerLeft = -1;

        if (this.limits.isEnabled()) {
            if (this.limits.isGlobalAmountLimited()) {
                globalLeft = globalLimit == null ? this.limits.getGlobalAmount() : Math.max(0, this.limits.getGlobalAmount() - globalLimit.getRolls());
            }
            if (this.limits.isPlayerAmountLimited()) {
                playerLeft = playerLimit == null ? this.limits.getPlayerAmount() : Math.max(0, this.limits.getPlayerAmount() - playerLimit.getRolls());
            }
        }

        if (globalLeft < 0 || playerLeft < 0) {
            return Math.max(playerLeft, globalLeft);
        }

        return Math.min(playerLeft, globalLeft);
    }

    @Override
    public boolean isRollable() {
        return this.weight > 0D;
    }

    @Override
    public boolean hasBadPermissions(@NotNull Player player) {
        return !this.ignoredPermissions.isEmpty() && this.ignoredPermissions.stream().anyMatch(player::hasPermission);
    }

    @Override
    public boolean hasRequiredPermissions(@NotNull Player player) {
        return this.requiredPermissions.isEmpty() || this.requiredPermissions.stream().anyMatch(player::hasPermission);
    }

    @Override
    public boolean fitRequirements(@NotNull Player player) {
        return !this.hasBadPermissions(player) && this.hasRequiredPermissions(player);
    }

    @Override
    public boolean canWin(@NotNull Player player) {
        if (!this.isRollable()) return false;
        if (!this.fitRequirements(player)) return false;

        return !this.isOnCooldown(player) && this.getAvailableRolls(player) != 0;
    }

    @Override
    public void give(@NotNull Player player) {
        this.plugin.getCrateManager().giveReward(player, this);
    }

    @Override
    public double getRollChance() {
        double sum = this.crate.getRewards(this.rarity).stream().mapToDouble(Reward::getWeight).sum();
        double rarityChance = this.rarity.getRollChance(this.crate);
        double chance = (this.weight / sum) * (rarityChance / 100D);

        return chance * 100D;
    }

    @Override
    @NotNull
    public String getId() {
        return this.id;
    }

    @Override
    @NotNull
    public Crate getCrate() {
        return this.crate;
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
    public AdaptedItem getPreview() {
        return this.preview;
    }

    public void setPreview(@NotNull AdaptedItem provider) {
        this.preview = provider;
    }

    @Override
    @NotNull
    public Rarity getRarity() {
        return this.rarity;
    }

    @Override
    public void setRarity(@NotNull Rarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean isBroadcast() {
        return this.broadcast;
    }

    @Override
    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    @NotNull
    public LimitValues getLimits() {
        return this.limits;
    }

    public void setLimits(@NotNull LimitValues limitValues) {
        this.limits = limitValues;
    }

    @Override
    @NotNull
    public Set<String> getIgnoredPermissions() {
        return this.ignoredPermissions;
    }

    @Override
    public void setIgnoredPermissions(@NotNull Set<String> ignoredPermissions) {
        this.ignoredPermissions = ignoredPermissions;
    }

    @NotNull
    @Override
    public Set<String> getRequiredPermissions() {
        return this.requiredPermissions;
    }

    @Override
    public void setRequiredPermissions(@NotNull Set<String> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }
}
