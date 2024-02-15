package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractConfigHolder;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.PDCUtil;
import su.nexmedia.engine.utils.random.Rnd;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.editor.CrateMainEditor;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.hologram.HologramHandler;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Crate extends AbstractConfigHolder<ExcellentCratesPlugin> implements Placeholder {

    private final Map<Currency, Double>         openCostMap;
    private final Set<Location>                 blockLocations;
    private final Set<Milestone>                milestones;
    private final CrateInspector                inspector;
    private final CrateMainEditor               editor;
    private final LinkedHashMap<String, Reward> rewardMap;
    private final PlaceholderMap                placeholderMap;

    private String           name;
    private String           openingConfig;
    private String           previewConfig;
    private boolean          isPermissionRequired;
    private int              openCooldown;
    private Set<String>      keyIds;
    private ItemStack        item;
    private boolean          milestonesRepeatable;
    private boolean          pushbackEnabled;
    private boolean          hologramEnabled;
    private String           hologramTemplate;
    private CrateEffectModel effectModel;
    private UniParticle      effectParticle;
    private String lastOpener;
    private String lastReward;

    public Crate(@NotNull ExcellentCratesPlugin plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.setKeyIds(new HashSet<>());
        this.openCostMap = new HashMap<>();
        this.rewardMap = new LinkedHashMap<>();
        this.blockLocations = new HashSet<>();
        this.milestones = new HashSet<>();
        this.placeholderMap = Placeholders.forCrate(this);
        this.inspector = new CrateInspector(this);
        this.editor = new CrateMainEditor(this);
    }

    @Override
    public boolean load() {
        // Setting migration - start
        if (cfg.contains("Block.Hologram.Text")) {
            List<String> holoText = cfg.getStringList("Block.Hologram.Text");
            var map = Config.CRATE_HOLOGRAM_TEMPLATES.get();
            map.putIfAbsent(this.getId(), holoText);
            Config.CRATE_HOLOGRAM_TEMPLATES.set(map);
            Config.CRATE_HOLOGRAM_TEMPLATES.write(this.plugin.getConfig());

            plugin.getConfig().saveChanges();
            cfg.remove("Block.Hologram.Text");
            cfg.remove("Block.Hologram.Offset");
            cfg.set("Block.Hologram.Template", this.getId());
        }
        // Setting migration - end

        this.setName(cfg.getString("Name", this.getId()));
        this.setOpeningConfig(cfg.getString("Animation_Config"));
        this.setPreviewConfig(cfg.getString("Preview_Config"));
        this.setPermissionRequired(cfg.getBoolean("Permission_Required"));

        this.setOpenCooldown(cfg.getInt("Opening.Cooldown"));

        for (String curId : cfg.getSection("Opening.Cost")) {
            Currency currency = this.plugin.getCurrencyManager().getCurrency(curId);
            if (currency == null) continue;

            double amount = cfg.getDouble("Opening.Cost." + curId);
            this.setOpenCost(currency, amount);
        }

        this.setKeyIds(cfg.getStringSet("Key.Ids"));
        this.setItem(cfg.getItem("Item"));

        this.setPushbackEnabled(cfg.getBoolean("Block.Pushback.Enabled"));
        this.setHologramEnabled(cfg.getBoolean("Block.Hologram.Enabled"));
        this.setHologramTemplate(cfg.getString("Block.Hologram.Template", Placeholders.DEFAULT));

        CrateEffectModel model = cfg.getEnum("Block.Effect.Model", CrateEffectModel.class, CrateEffectModel.SIMPLE);
        UniParticle particle = UniParticle.read(cfg, "Block.Effect.Particle");
        this.setEffectModel(model);
        this.setEffectParticle(particle);

        this.setLastOpener(cfg.getString("Last_Opener"));
        this.setLastReward(cfg.getString("Last_Reward"));

        for (String rewId : cfg.getSection("Rewards.List")) {
            String path = "Rewards.List." + rewId + ".";

            String rewName = cfg.getString(path + "Name", rewId);
            double rewChance = cfg.getDouble(path + "Chance");
            String rewRarityId = cfg.getString(path + "Rarity", "");
            Rarity rarity = plugin.getCrateManager().getRarity(rewRarityId);
            if (rarity == null) rarity = plugin.getCrateManager().getMostCommonRarity();

            boolean rBroadcast = cfg.getBoolean(path + "Broadcast");
            ItemStack rewPreview = cfg.getItemEncoded(path + "Preview");
            if (rewPreview == null) rewPreview = new ItemStack(Material.BARRIER);

            int winLimitAmount = cfg.getInt(path + "Win_Limits.Amount", -1);
            long winLimitCooldown = cfg.getLong(path + "Win_Limits.Cooldown", 0L);

            List<String> rewCmds = cfg.getStringList(path + "Commands");
            List<ItemStack> rewItem = new ArrayList<>(Stream.of(cfg.getItemsEncoded(path + "Items")).toList());
            Set<String> ignoredForPerms = cfg.getStringSet(path + "Ignored_For_Permissions");

            Reward reward = new Reward(this, rewId, rewName, rewChance, rarity, rBroadcast,
                winLimitAmount, winLimitCooldown,
                rewPreview, rewItem, rewCmds, ignoredForPerms);
            this.rewardMap.put(rewId, reward);
        }

        this.setMilestonesRepeatable(cfg.getBoolean("Milestones.Repeatable"));
        for (String sId : cfg.getSection("Milestones.List")) {
            this.getMilestones().add(Milestone.read(cfg, "Milestones.List." + sId));
        }

        this.cfg.saveChanges();
        return true;
    }

    public void loadLocations() {
        this.getBlockLocations().addAll(LocationUtil.deserialize(cfg.getStringList("Block.Locations")));
        this.getBlockLocations().removeIf(location -> location.getBlock().isEmpty());
        this.updateHologram();
    }

    @Override
    public void onSave() {
        cfg.set("Name", this.getName());
        cfg.set("Animation_Config", this.getOpeningConfig());
        cfg.set("Preview_Config", this.getPreviewConfig());
        cfg.set("Permission_Required", this.isPermissionRequired());

        cfg.set("Opening.Cooldown", this.getOpenCooldown());
        this.openCostMap.forEach((currency, amount) -> {
            cfg.set("Opening.Cost." + currency.getId(), amount);
        });

        cfg.set("Key.Ids", this.getKeyIds());
        cfg.setItem("Item", this.getRawItem());

        cfg.set("Block.Locations", LocationUtil.serialize(new ArrayList<>(this.getBlockLocations())));
        cfg.set("Block.Pushback.Enabled", this.isPushbackEnabled());
        cfg.set("Block.Hologram.Enabled", this.isHologramEnabled());
        cfg.set("Block.Hologram.Template", this.getHologramTemplate());
        cfg.set("Block.Effect.Model", this.getEffectModel().name());
        cfg.remove("Block.Effect.Particle");
        this.getEffectParticle().write(cfg, "Block.Effect.Particle");
        this.writeLastOpenData();
        this.writeRewards();

        cfg.set("Milestones.Repeatable", this.isMilestonesRepeatable());
        cfg.remove("Milestones.List");
        int i = 0;
        for (Milestone milestone : this.getMilestones()) {
            milestone.write(cfg, "Milestones.List." + (i++));
        }
    }

    private void writeLastOpenData() {
        cfg.set("Last_Opener", this.getLastOpener());
        cfg.set("Last_Reward", this.getLastReward());
    }

    private void writeRewards() {
        cfg.remove("Rewards.List");
        for (Entry<String, Reward> e : this.getRewardsMap().entrySet()) {
            Reward reward = e.getValue();
            String path = "Rewards.List." + e.getKey() + ".";

            cfg.set(path + "Name", reward.getName());
            cfg.set(path + "Chance", reward.getWeight());
            cfg.set(path + "Rarity", reward.getRarity().getId());
            cfg.set(path + "Broadcast", reward.isBroadcast());
            cfg.set(path + "Win_Limits.Amount", reward.getWinLimitAmount());
            cfg.set(path + "Win_Limits.Cooldown", reward.getWinLimitCooldown());
            cfg.setItemEncoded(path + "Preview", reward.getPreview());
            cfg.set(path + "Commands", reward.getCommands());
            cfg.setItemsEncoded(path + "Items", reward.getItems());
            cfg.set(path + "Ignored_For_Permissions", reward.getIgnoredForPermissions());
        }
    }

    public void saveRewards() {
        this.writeRewards();
        this.getConfig().saveChanges();
    }

    public void saveLastOpenData() {
        this.writeLastOpenData();
        this.getConfig().saveChanges();
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    public void clear() {
        this.removeHologram();
        if (this.editor != null) this.editor.clear();
        this.rewardMap.values().forEach(Reward::clear);
        this.rewardMap.clear();
    }

    @NotNull
    public CrateInspector getInspector() {
        return this.inspector;
    }

    @NotNull
    public CrateMainEditor getEditor() {
        return this.editor;
    }

    public void createHologram() {
        if (!this.isHologramEnabled()) return;

        HologramHandler hologramHandler = plugin.getHologramHandler();
        if (hologramHandler == null) return;

        hologramHandler.create(this);
    }

    public void removeHologram() {
        HologramHandler hologramHandler = plugin.getHologramHandler();
        if (hologramHandler == null) return;

        hologramHandler.remove(this);
    }

    public void updateHologram() {
        this.removeHologram();
        this.createHologram();
    }

    public boolean isKeyRequired() {
        return !this.getKeyIds().isEmpty();
    }

    @NotNull
    public String getPermission() {
        return Perms.PREFIX_CRATE + this.getId();
    }

    @NotNull
    public List<String> getHologramText() {
        List<String> text = new ArrayList<>(Config.CRATE_HOLOGRAM_TEMPLATES.get().getOrDefault(this.getHologramTemplate(), Collections.emptyList()));
        text.replaceAll(this.replacePlaceholders());
        return text;
    }

    public boolean hasPermission(@NotNull Player player) {
        return !this.isPermissionRequired() || (player.hasPermission(this.getPermission()));
    }

    @NotNull
    public Reward rollReward() {
        return this.rollReward(null);
    }

    @NotNull
    public Reward rollReward(@Nullable Player player) {
        Collection<Reward> allRewards = player == null ? this.getRewards() : this.getRewards(player);

        Map<Rarity, Double> rarities = new HashMap<>();
        allRewards.stream().map(Reward::getRarity).forEach(rarity -> {
            rarities.putIfAbsent(rarity, rarity.getChance());
        });

        Rarity rarity = Rnd.getByWeight(rarities);

        Map<Reward, Double> rewards = new HashMap<>();
        allRewards.stream().filter(reward -> reward.getRarity() == rarity).forEach(reward -> {
            rewards.put(reward, reward.getWeight());
        });
        return Rnd.getByWeight(rewards);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = Colorizer.apply(name);
    }

    @Nullable
    public String getOpeningConfig() {
        return this.openingConfig;
    }

    public void setOpeningConfig(@Nullable String openingConfig) {
        this.openingConfig = openingConfig == null ? null : openingConfig.toLowerCase();
    }

    @Nullable
    public String getPreviewConfig() {
        return this.previewConfig;
    }

    public void setPreviewConfig(@Nullable String previewConfig) {
        this.previewConfig = previewConfig == null ? null : previewConfig.toLowerCase();
    }

    public boolean isPermissionRequired() {
        return isPermissionRequired;
    }

    public void setPermissionRequired(boolean isPermissionRequired) {
        this.isPermissionRequired = isPermissionRequired;
    }

    public int getOpenCooldown() {
        return this.openCooldown;
    }

    public void setOpenCooldown(int openCooldown) {
        this.openCooldown = openCooldown;
    }

    @NotNull
    public Map<Currency, Double> getOpenCostMap() {
        return openCostMap;
    }

    public double getOpenCost(@NotNull Currency currency) {
        return this.getOpenCostMap().getOrDefault(currency, 0D);
    }

    public void setOpenCost(@NotNull Currency currency, double amount) {
        this.getOpenCostMap().put(currency, amount);
    }

    @NotNull
    public Set<String> getKeyIds() {
        return keyIds;
    }

    public void setKeyIds(@NotNull Set<String> keyIds) {
        this.keyIds = new HashSet<>(keyIds.stream().filter(Predicate.not(String::isEmpty)).map(String::toLowerCase).toList());
    }

    @NotNull
    public ItemStack getRawItem() {
        return new ItemStack(this.item);
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack item = this.getRawItem();
        PDCUtil.set(item, Keys.CRATE_ID, this.getId());
        return item;
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = new ItemStack(item);
    }

    public boolean isPushbackEnabled() {
        return this.pushbackEnabled;
    }

    public void setPushbackEnabled(boolean blockPushback) {
        this.pushbackEnabled = blockPushback;
    }

    @NotNull
    public Set<Location> getBlockLocations() {
        return blockLocations;
    }

    public void addBlockLocation(@NotNull Location location) {
        this.getBlockLocations().add(location);
    }

    public void removeBlockLocation(@NotNull Location location) {
        this.getBlockLocations().remove(location);
    }

    public boolean isHologramEnabled() {
        return this.hologramEnabled;
    }

    public void setHologramEnabled(boolean hologramEnabled) {
        this.hologramEnabled = hologramEnabled;
    }

    @NotNull
    public String getHologramTemplate() {
        return hologramTemplate;
    }

    public void setHologramTemplate(@NotNull String hologramTemplate) {
        this.hologramTemplate = hologramTemplate.toLowerCase();
    }

    @NotNull
    public CrateEffectModel getEffectModel() {
        return effectModel;
    }

    public void setEffectModel(@NotNull CrateEffectModel effectModel) {
        this.effectModel = effectModel;
    }

    @NotNull
    public UniParticle getEffectParticle() {
        return effectParticle;
    }

    public void setEffectParticle(@NotNull UniParticle effectParticle) {
        this.effectParticle = effectParticle;
    }

    @Nullable
    public String getLastOpener() {
        return lastOpener;
    }

    public void setLastOpener(@Nullable String lastOpener) {
        this.lastOpener = lastOpener;
    }

    @Nullable
    public String getLastReward() {
        return lastReward;
    }

    public void setLastReward(@Nullable String lastReward) {
        this.lastReward = lastReward;
    }

    @NotNull
    public LinkedHashMap<String, Reward> getRewardsMap() {
        return this.rewardMap;
    }

    @NotNull
    public Collection<Reward> getRewards() {
        return this.getRewardsMap().values();
    }

    @NotNull
    public List<Reward> getRewards(@NotNull Rarity rarity) {
        return this.getRewards().stream().filter(reward -> reward.getRarity() == rarity).toList();
    }

    @NotNull
    public List<Reward> getRewards(@NotNull Player player) {
        return this.getRewards().stream().filter(reward -> reward.canWin(player)).toList();
    }

    @NotNull
    public List<Reward> getRewards(@NotNull Player player, @NotNull Rarity rarity) {
        return this.getRewards().stream().filter(reward -> reward.getRarity() == rarity && reward.canWin(player)).toList();
    }

    public void setRewards(@NotNull List<Reward> rewards) {
        this.getRewardsMap().clear();
        this.getRewardsMap().putAll(rewards.stream().collect(
            Collectors.toMap(Reward::getId, Function.identity(), (has, add) -> add, LinkedHashMap::new)));
    }

    @Nullable
    public Reward getReward(@NotNull String id) {
        return this.getRewardsMap().get(id.toLowerCase());
    }

    @Nullable
    public Reward getMilestoneReward(int openings) {
        Milestone milestone = this.getMilestone(openings);
        return milestone == null ? null : this.getMilestoneReward(milestone);
    }

    @Nullable
    public Reward getMilestoneReward(@NotNull Milestone milestone) {
        return this.getReward(milestone.getRewardId());
    }

    public void addReward(@NotNull Reward reward) {
        this.getRewardsMap().put(reward.getId(), reward);
    }

    public void removeReward(@NotNull Reward reward) {
        this.removeReward(reward.getId());
    }

    public void removeReward(@NotNull String id) {
        this.getRewardsMap().remove(id);
    }

    @NotNull
    public Set<Milestone> getMilestones() {
        return milestones;
    }

    @Nullable
    public Milestone getMilestone(int openings) {
        return this.getMilestones().stream().filter(milestone -> milestone.getOpenings() == openings).findFirst().orElse(null);
    }

    public boolean isMilestonesRepeatable() {
        return milestonesRepeatable;
    }

    public void setMilestonesRepeatable(boolean milestonesRepeatable) {
        this.milestonesRepeatable = milestonesRepeatable;
    }

    public int getMaxMilestone() {
        return this.getMilestones().stream().mapToInt(Milestone::getOpenings).max().orElse(0);
    }
}
