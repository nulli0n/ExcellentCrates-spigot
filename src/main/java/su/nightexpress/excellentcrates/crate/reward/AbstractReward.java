package su.nightexpress.excellentcrates.crate.reward;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.excellentcrates.util.inspect.Inspectors;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public abstract class AbstractReward implements Reward {

    protected final CratesPlugin plugin;
    protected final Crate        crate;
    protected final String       id;

    protected ItemProvider preview;
    protected double       weight;
    protected Rarity      rarity;
    protected boolean     broadcast;
    protected boolean     placeholderApply;
    protected LimitValues playerLimits;
    protected LimitValues globalLimits;
    protected Set<String> ignoredPermissions;
    protected Set<String> requiredPermissions;

    public AbstractReward(@NotNull CratesPlugin plugin, @NotNull Crate crate, @NotNull String id, @NotNull Rarity rarity) {
        this.plugin = plugin;
        this.crate = crate;
        this.id = id.toLowerCase();

        this.setWeight(10D);
        this.setRarity(rarity);
        this.setBroadcast(false);
        this.setPlaceholderApply(false);

        this.setPlayerLimits(LimitValues.unlimited());
        this.setGlobalLimits(LimitValues.unlimited());
        this.setIgnoredPermissions(new HashSet<>());
        this.setRequiredPermissions(new HashSet<>());
    }

    @Override
    public void load(@NotNull FileConfig config, @NotNull String path) {
        this.setPreview(ItemTypes.read(config, path + ".PreviewData"));
        this.setWeight(config.getDouble(path + ".Weight", -1D));
        this.setBroadcast(config.getBoolean(path + ".Broadcast"));
        this.setPlaceholderApply(config.getBoolean(path + ".Placeholder_Apply"));

        this.setPlayerLimits(LimitValues.read(config, path + ".Win_Limit.Player"));
        this.setGlobalLimits(LimitValues.read(config, path + ".Win_Limit.Global"));
        this.setIgnoredPermissions(config.getStringSet(path + ".Ignored_For_Permissions"));
        this.setRequiredPermissions(config.getStringSet(path + ".Required_Permissions"));

        this.loadAdditional(config, path);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Type", this.getType().name());
        if (!this.preview.isDummy()) {
            config.set(path + ".PreviewData", this.preview);
        }
        config.set(path + ".Weight", this.weight);
        config.set(path + ".Rarity", this.rarity.getId());
        config.set(path + ".Broadcast", this.broadcast);
        config.set(path + ".Placeholder_Apply", this.placeholderApply);
        this.playerLimits.write(config, path + ".Win_Limit.Player");
        this.globalLimits.write(config, path + ".Win_Limit.Global");
        config.set(path + ".Ignored_For_Permissions", this.ignoredPermissions);
        config.set(path + ".Required_Permissions", this.requiredPermissions);
        this.writeAdditional(config, path);
    }

    @Override
    public void save() {
        this.crate.saveReward(this);
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
        Replacer replacer = Replacer.create();
        if (this.placeholderApply) {
            replacer.replace(this.crate.replacePlaceholders());
            replacer.replace(this.replacePlaceholders());
            replacer.replace(Placeholders.forPlayerWithPAPI(player));
        }
        return replacer;
    }

    @Override
    public boolean hasProblems() {
        return Inspectors.REWARD.hasProblems(this);
    }

    @Override
    public boolean hasGlobalLimit() {
        return this.globalLimits.isEnabled() && !this.globalLimits.isUnlimitedAmount();
    }

    @Override
    public boolean hasPersonalLimit() {
        return this.playerLimits.isEnabled() && !this.playerLimits.isUnlimitedAmount();
    }

    @Override
    public int getAvailableRolls(@NotNull Player player) {
        return plugin.getCrateManager().getAvailableRolls(player, this);
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

        return this.getAvailableRolls(player) != 0;
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
    @NotNull
    public String getNameTranslated() {
        return NightMessage.asLegacy(this.getName());
    }

    @Override
    @NotNull
    public List<String> getDescriptionTranslated() {
        return NightMessage.asLegacy(this.getDescription());
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
    public ItemProvider getPreview() {
        return this.preview;
    }

    public void setPreview(@NotNull ItemProvider provider) {
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

    @Override
    public void setPlaceholderApply(boolean placeholderApply) {
        this.placeholderApply = placeholderApply;
    }

    @Override
    public boolean isPlaceholderApply() {
        return this.placeholderApply;
    }

    @Override
    public boolean isOneTimed() {
        return this.playerLimits.isOneTimed() || this.globalLimits.isOneTimed();
    }

//    @Override
//    @NotNull
//    public LimitValues getLimitValues(@NotNull LimitType limitType) {
//        return limitType == LimitType.PLAYER ? this.playerLimits : this.globalLimits;
//    }

    @Override
    @NotNull
    public LimitValues getPlayerLimits() {
        return this.playerLimits;
    }

    @Override
    public void setPlayerLimits(@NotNull LimitValues playerLimits) {
        this.playerLimits = playerLimits;
    }

    @Override
    @NotNull
    public LimitValues getGlobalLimits() {
        return this.globalLimits;
    }

    @Override
    public void setGlobalLimits(@NotNull LimitValues globalLimits) {
        this.globalLimits = globalLimits;
    }

    @Override
    @NotNull
    public ItemStack getPreviewItem() {
        return this.getPreview().getItemStack();
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
