package su.nightexpress.excellentcrates.crate.impl;

import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.effect.EffectModel;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.hologram.HologramType;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.util.pos.BlockPos;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Crate extends AbstractFileData<CratesPlugin> implements Placeholder {

    private final Set<CrateKey> keys;
    private final Set<WorldPos> blockPositions;
    private final Set<Milestone> milestones;
    private final Map<Currency, Double> openCostMap;
    private final LinkedHashMap<String, Reward> rewardMap;
    private final PlaceholderMap placeholderMap;
    private final PlaceholderMap placeholderFullMap;

    private String name;
    private String openingConfig;
    private String previewConfig;
    private boolean permissionRequired;
    private int openCooldown;
    private boolean keyRequired;
    private ItemStack item;
    private boolean milestonesRepeatable;
    private boolean pushbackEnabled;
    private boolean hologramEnabled;
    private String hologramTemplate;
    private double hologramYOffset;
    private EffectModel effectModel;
    private UniParticle effectParticle;
    private String lastOpener;
    private String lastReward;

    public Crate(@NotNull CratesPlugin plugin, @NotNull File file) {
        super(plugin, file);
        this.keys = new HashSet<>();
        this.openCostMap = new HashMap<>();
        this.rewardMap = new LinkedHashMap<>();
        this.blockPositions = new HashSet<>();
        this.milestones = new HashSet<>();
        this.placeholderMap = Placeholders.forCrate(this);
        this.placeholderFullMap = Placeholders.forCrateAll(this);
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        // Setting migration - start
        if (config.contains("Block.Hologram.Text")) {
            List<String> holoText = config.getStringList("Block.Hologram.Text");
            var map = Config.CRATE_HOLOGRAM_TEMPLATES.get();
            map.putIfAbsent(this.getId(), holoText);
            Config.CRATE_HOLOGRAM_TEMPLATES.set(map);
            Config.CRATE_HOLOGRAM_TEMPLATES.write(this.plugin.getConfig());

            plugin.getConfig().saveChanges();
            config.remove("Block.Hologram.Text");
            config.remove("Block.Hologram.Offset");
            config.set("Block.Hologram.Template", this.getId());
        }

        if (config.contains("Block.Locations")) {
            List<WorldPos> positions = new ArrayList<>();

            config.getStringList("Block.Locations").forEach(raw -> {
                String[] split = raw.split(",");
                if (split.length != 6) return;

                String worldName = split[5];
                BlockPos blockPos = BlockPos.deserialize(raw);

                positions.add(new WorldPos(worldName, blockPos));
            });
            config.remove("Block.Locations");
            config.set("Block.Positions", positions.stream().map(WorldPos::serialize).toList());
            positions.clear();
        }
        // Setting migration - end

        this.setName(config.getString("Name", this.getId()));
        this.setOpeningConfig(config.getString("Animation_Config"));
        this.setPreviewConfig(config.getString("Preview_Config"));
        this.setPermissionRequired(config.getBoolean("Permission_Required"));

        this.setOpenCooldown(config.getInt("Opening.Cooldown"));

        for (String curId : config.getSection("Opening.Cost")) {
            Currency currency = this.plugin.getCurrencyManager().getCurrency(curId);
            if (currency == null) continue;

            double amount = config.getDouble("Opening.Cost." + curId);
            if (amount <= 0D) continue;

            this.setOpenCost(currency, amount);
        }

        this.setKeyRequired(ConfigValue.create("Key.Required",
                true,
                "Sets whether or not keys are required to open this crate."
        ).read(config));

        config.getStringList("Key.Ids").forEach(keyId -> {
            CrateKey key = this.plugin.getKeyManager().getKeyById(keyId);
            if (key == null) {
                this.plugin.warn("Invalid key '" + keyId + "' in '" + this.getId() + "' crate key requirements. Ignoring...");
                return;
            }
            this.addKey(key);
        });

        this.setItem(config.getItem("Item"));

        this.blockPositions.addAll(config.getStringList("Block.Positions").stream().map(WorldPos::deserialize).toList());
        List<WorldPos> blockPositionsTemp = new ArrayList<>(blockPositions);
        for (WorldPos pos : blockPositionsTemp) {
            Block block = pos.toBlock();
            if (block != null) {
                new FoliaScheduler(plugin).runTask(Objects.requireNonNull(pos.toBlock()).getLocation(), () -> {
                    if (block.isEmpty()) {
                        this.blockPositions.remove(pos);
                    }
                });
            } else {
                this.blockPositions.remove(pos);
            }
        }

        this.setPushbackEnabled(config.getBoolean("Block.Pushback.Enabled"));
        this.setHologramEnabled(config.getBoolean("Block.Hologram.Enabled"));
        this.setHologramTemplate(config.getString("Block.Hologram.Template", Placeholders.DEFAULT));
        this.setHologramYOffset(config.getDouble("Block.Hologram.Y_Offset", Config.getHologramType() == HologramType.INTERNAL ? 0 : 0.5));

        EffectModel model = config.getEnum("Block.Effect.Model", EffectModel.class, EffectModel.SIMPLE);
        UniParticle particle = UniParticle.read(config, "Block.Effect.Particle");
        this.setEffectModel(model);
        this.setEffectParticle(particle);

        this.setLastOpener(config.getString("Last_Opener"));
        this.setLastReward(config.getString("Last_Reward"));

        for (String sId : config.getSection("Rewards.List")) {
            Reward reward = Reward.read(this.plugin, this, config, "Rewards.List." + sId, sId);
            this.rewardMap.put(sId, reward);
        }

        this.setMilestonesRepeatable(config.getBoolean("Milestones.Repeatable"));
        for (String sId : config.getSection("Milestones.List")) {
            this.getMilestones().add(Milestone.read(this, config, "Milestones.List." + sId));
        }

        return true;
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        this.writeSettings(config);
        this.writeLastOpenData(config);
        this.writeRewards(config);
        this.writeMilestones(config);
    }

    private void writeSettings(@NotNull FileConfig config) {
        config.set("Name", this.getName());
        config.set("Animation_Config", this.getOpeningConfig());
        config.set("Preview_Config", this.getPreviewConfig());
        config.set("Permission_Required", this.isPermissionRequired());

        config.set("Opening.Cooldown", this.getOpenCooldown());
        config.remove("Opening.Cost");
        this.openCostMap.forEach((currency, amount) -> {
            config.set("Opening.Cost." + currency.getId(), amount);
        });

        config.set("Key.Required", this.isKeyRequired());
        config.set("Key.Ids", this.getKeyNames());
        config.setItem("Item", this.getRawItem());

        //config.set("Block.Locations", LocationUtil.serialize(new ArrayList<>(this.getBlockLocations())));
        config.set("Block.Positions", this.blockPositions.stream().map(WorldPos::serialize).toList());
        config.set("Block.Pushback.Enabled", this.isPushbackEnabled());
        config.set("Block.Hologram.Enabled", this.isHologramEnabled());
        config.set("Block.Hologram.Template", this.getHologramTemplate());
        config.set("Block.Hologram.Y_Offset", this.getHologramYOffset());
        config.set("Block.Effect.Model", this.getEffectModel().name());
        config.remove("Block.Effect.Particle");
        this.getEffectParticle().write(config, "Block.Effect.Particle");
    }

    private void writeLastOpenData(@NotNull FileConfig config) {
        config.set("Last_Opener", this.getLastOpener());
        config.set("Last_Reward", this.getLastReward());
    }

    private void writeRewards(@NotNull FileConfig config) {
        config.remove("Rewards.List");
        this.getRewards().forEach(reward -> this.writeReward(config, reward));
//        this.rewardMap.forEach((id, reward) -> {
//            reward.write(config, "Rewards.List." + id);
//        });
    }

    private void writeReward(@NotNull FileConfig config, @NotNull Reward reward) {
        reward.write(config, "Rewards.List." + reward.getId());
    }

    private void writeMilestones(@NotNull FileConfig config) {
        config.set("Milestones.Repeatable", this.isMilestonesRepeatable());
        config.remove("Milestones.List");
        int i = 0;
        for (Milestone milestone : this.getMilestones()) {
            milestone.write(config, "Milestones.List." + (i++));
        }
    }

    public void saveSettings() {
        this.writeConfig(this::writeSettings);
    }

    public void saveRewards() {
        this.writeConfig(this::writeRewards);
    }

    public void saveReward(@NotNull Reward reward) {
        this.writeConfig(config -> this.writeReward(config, reward));
    }

    public void saveMilestones() {
        this.writeConfig(this::writeMilestones);
    }

    public void saveLastOpenData() {
        this.writeConfig(this::writeLastOpenData);
    }

    private void writeConfig(@NotNull Consumer<FileConfig> consumer) {
        FileConfig config = this.getConfig();

        consumer.accept(config);
        config.saveChanges();
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

//    @Deprecated
//    public void loadLocations() {
//        this.getBlockLocations().addAll(LocationUtil.deserialize(this.getConfig().getStringList("Block.Locations")));
//        this.getBlockLocations().removeIf(location -> location.getBlock().isEmpty());
//        this.updateHologram();
//    }

    public void loadRewardWinDatas() {
        this.getRewards().forEach(Reward::loadGlobalWinData);
    }

    public void saveRewardWinDatas() {
        this.getRewards().forEach(Reward::saveGlobalWinData);
    }

    public void deleteRewardWinDatas() {
        this.plugin.getData().deleteRewardWinData(this);
    }

    public void clear() {
        this.removeHologram();
        this.rewardMap.clear();
    }

    public void createHologram() {
        this.manageHologram(handler -> handler.create(this));
    }

    public void removeHologram() {
        this.manageHologram(handler -> handler.remove(this));
    }

    public void updateHologram() {
        this.manageHologram(handler -> handler.refresh(this));
    }

    private void manageHologram(@NotNull Consumer<HologramHandler> consumer) {
        if (!this.isHologramEnabled()) return;

        HologramHandler handler = plugin.getHologramHandler();
        if (handler == null) return;

        consumer.accept(handler);
    }

    public boolean hasRewards() {
        return !this.getRewards().isEmpty();
    }

    public boolean hasMilestones() {
        return Config.isMilestonesEnabled() && !this.getMilestones().isEmpty();
    }

    public boolean isGoodKey(@NotNull CrateKey key) {
        return this.getKeys().contains(key);
    }

    public boolean isAllPhysicalKeys() {
        return this.getKeys().stream().noneMatch(CrateKey::isVirtual);
    }

    public boolean isAllVirtualKeys() {
        return this.getKeys().stream().allMatch(CrateKey::isVirtual);
    }

    public boolean hasValidPreview() {
        String name = this.getPreviewConfig();
        if (name == null) return true;

        return this.plugin.getCrateManager().getPreview(name) != null;
    }

    public boolean hasValidOpening() {
        String name = this.getOpeningConfig();
        if (name == null) return true;

        return this.plugin.getOpeningManager().getInventoryOpening(name) != null;
    }

    public boolean hasValidHologram() {
        String id = this.getHologramTemplate();

        return Config.CRATE_HOLOGRAM_TEMPLATES.get().containsKey(id);
    }

    public boolean hasOpenCost() {
        return !this.openCostMap.isEmpty();
    }

    public boolean hasPermission(@NotNull Player player) {
        if (!this.isPermissionRequired()) return true;

        return player.hasPermission(this.getPermission());
    }

    public boolean hasCostBypassPermisssion(@NotNull Player player) {
        return player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST) || player.hasPermission(this.getCostBypassPermission());
    }

    @NotNull
    public String getPermission() {
        return Perms.PREFIX_CRATE + this.getId();
    }

    @NotNull
    public String getCostBypassPermission() {
        return Perms.PREFIX_BYPASS_OPEN_COST + this.getId();
    }

    @NotNull
    public List<String> getHologramText() {
        List<String> text = new ArrayList<>(Config.CRATE_HOLOGRAM_TEMPLATES.get().getOrDefault(this.getHologramTemplate(), Collections.emptyList()));
        text.replaceAll(this.replacePlaceholders());
        return text;
    }

    public boolean hasRewards(@NotNull Player player) {
        return this.hasRewards(player, null);
    }

    public boolean hasRewards(@NotNull Rarity rarity) {
        return this.hasRewards(null, rarity);
    }

    public boolean hasRewards(@Nullable Player player, @Nullable Rarity rarity) {
        return !this.getRewards(player, rarity).isEmpty();
    }

    @NotNull
    public Reward rollReward() {
        return this.rollReward(null, null);
    }

    @NotNull
    public Reward rollReward(@NotNull Rarity rarity) {
        return this.rollReward(null, rarity);
    }

    @NotNull
    public Reward rollReward(@NotNull Player player) {
        return this.rollReward(player, null);
    }

    @NotNull
    public Reward rollReward(@Nullable Player player, @Nullable Rarity rarity) {
        List<Reward> rewards = this.getRewards(player, rarity);

        // If no rarity is specified, we have to select a random one and filter rewards by selected rarity.
        // Otherwise reward list is already obtained with specified rarity.
        if (rarity == null) {
            Map<Rarity, Double> rarities = new HashMap<>();
            rewards.stream().map(Reward::getRarity).forEach(rarity1 -> {
                rarities.putIfAbsent(rarity1, rarity1.getWeight());
            });

            Rarity rarityRoll = Rnd.getByWeight(rarities);
            rewards.removeIf(reward -> reward.getRarity() != rarityRoll);
        }

        return this.rollReward(rewards);
    }

    @NotNull
    private Reward rollReward(@NotNull Collection<Reward> allRewards) {
        Map<Reward, Double> rewards = new HashMap<>();
        allRewards.forEach(reward -> {
            rewards.put(reward, reward.getWeight());
        });
        return Rnd.getByWeight(rewards);
    }

    public void addBlockPosition(@NotNull Location location) {
        WorldPos pos = WorldPos.from(location);

        this.blockPositions.add(pos);
    }

    public void clearBlockPositions() {
        this.blockPositions.clear();
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getNameTranslated() {
        return NightMessage.asLegacy(this.getName());
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
        return permissionRequired;
    }

    public void setPermissionRequired(boolean isPermissionRequired) {
        this.permissionRequired = isPermissionRequired;
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
        return this.openCostMap.getOrDefault(currency, 0D);
    }

    public void setOpenCost(@NotNull Currency currency, double amount) {
        this.openCostMap.put(currency, amount);
    }

    public boolean isKeyRequired() {
        return keyRequired;
    }

    public void setKeyRequired(boolean keyRequired) {
        this.keyRequired = keyRequired;
    }

    /**
     * @return A copy of the original set with CrateKey objects.
     */
    @NotNull
    public Set<CrateKey> getKeys() {
        return new HashSet<>(this.keys);
    }

    public void setKeys(@NotNull Set<CrateKey> keys) {
        this.keys.clear();
        this.keys.addAll(keys);
    }

    public boolean addKey(@NotNull CrateKey key) {
        return this.keys.add(key);
    }

    @NotNull
    public Set<String> getKeyNames() {
        return this.keys.stream().map(CrateKey::getId).collect(Collectors.toSet());
    }

    @NotNull
    public ItemStack getRawItem() {
        return new ItemStack(this.item);
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack item = this.getRawItem();
        PDCUtil.set(item, Keys.crateId, this.getId());
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
    public Set<WorldPos> getBlockPositions() {
        return new HashSet<>(this.blockPositions);
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

    public double getHologramYOffset() {
        return hologramYOffset;
    }

    public void setHologramYOffset(double hologramYOffset) {
        this.hologramYOffset = hologramYOffset;
    }

    @NotNull
    public EffectModel getEffectModel() {
        return effectModel;
    }

    public void setEffectModel(@NotNull EffectModel effectModel) {
        this.effectModel = effectModel;
    }

    @NotNull
    public UniParticle getEffectParticle() {
        return effectParticle;
    }

    public void setEffectParticle(@NotNull UniParticle effectParticle) {
        this.effectParticle = effectParticle;
        this.effectParticle.validateData();
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
        return new LinkedHashMap<>(this.rewardMap);
    }

    @NotNull
    public Set<Rarity> getRarities() {
        return this.getRewards().stream().map(Reward::getRarity).collect(Collectors.toSet());
    }

    @NotNull
    public Collection<Reward> getRewards() {
        return this.getRewardsMap().values();
    }

    public void setRewards(@NotNull List<Reward> rewards) {
        this.rewardMap.clear();
        this.rewardMap.putAll(rewards.stream().collect(
                Collectors.toMap(Reward::getId, Function.identity(), (has, add) -> add, LinkedHashMap::new)));
    }

    @NotNull
    public List<Reward> getRewards(@NotNull Rarity rarity) {
        return this.getRewards(null, rarity);
    }

    @NotNull
    public List<Reward> getRewards(@NotNull Player player) {
        return this.getRewards(player, null);
    }

    @NotNull
    public List<Reward> getRewards(@Nullable Player player, @Nullable Rarity rarity) {
        Predicate<Reward> predicate = reward -> {
            if (rarity != null && reward.getRarity() != rarity) return false;

            return player == null || reward.canWin(player);
        };

        return new ArrayList<>(this.getRewards().stream().filter(predicate).toList());
    }

    @Nullable
    public Reward getReward(@NotNull String id) {
        return this.rewardMap.get(id.toLowerCase());
    }

    @Nullable
    public Reward getMilestoneReward(int openings) {
        Milestone milestone = this.getMilestone(openings);
        return milestone == null ? null : milestone.getReward();
    }

    public void addReward(@NotNull Reward reward) {
        this.rewardMap.put(reward.getId(), reward);
    }

    public void removeReward(@NotNull Reward reward) {
        this.removeReward(reward.getId());
    }

    public void removeReward(@NotNull String id) {
        this.rewardMap.remove(id);
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
