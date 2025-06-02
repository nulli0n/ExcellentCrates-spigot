package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.excellentcrates.crate.effect.EffectRegistry;
import su.nightexpress.excellentcrates.crate.reward.RewardFactory;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.hologram.HologramManager;
import su.nightexpress.excellentcrates.hologram.HologramTemplate;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.universalscheduler.foliaScheduler.FoliaScheduler;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Crate extends AbstractFileData<CratesPlugin> {

    private final Set<String>                   keyIds;
    private final Set<WorldPos>                 blockPositions;
    private final Set<Milestone>                milestones;
    private final Map<String, Cost>             openCostMap;
    private final LinkedHashMap<String, Reward> rewardMap;

    private String      name;
    private List<String> description;
    private ItemProvider itemProvider;
    private boolean itemStackable;

    private boolean previewEnabled;
    private String  previewId;
    private boolean animationEnabled;
    private String  animationId;

    private boolean     permissionRequired;
    private int         openCooldown;
    private boolean     keyRequired;
    private boolean     milestonesRepeatable;
    private boolean     pushbackEnabled;

    private boolean hologramEnabled;
    private String  hologramTemplateId;
    private double  hologramYOffset;

    private String      effectType;
    private UniParticle effectParticle;

    public Crate(@NotNull CratesPlugin plugin, @NotNull File file) {
        super(plugin, file);
        this.keyIds = new HashSet<>();
        this.openCostMap = new HashMap<>();
        this.rewardMap = new LinkedHashMap<>();
        this.blockPositions = new HashSet<>();
        this.milestones = new HashSet<>();
        this.description = new ArrayList<>();
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        if (!config.contains("_dataver")) {
            config.set("_dataver", 600);

            File source = config.getFile();
            File target = new File(source.getParentFile().getAbsolutePath() + "/backups", source.getName() + ".backup535");
            FileUtil.create(target);

            try {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        if (config.contains("Item")) {
            ItemStack itemStack = config.getCosmeticItem("Item").getItemStack();
            ItemProvider provider = ItemTypes.vanilla(itemStack);
            config.set("ItemProvider", provider);
            config.remove("Item");
        }
        if (!config.contains("Preview")) {
            String oldId = config.getString("Preview_Config");
            config.set("Preview.Enabled", oldId != null);
            config.set("Preview.Id", oldId == null ? Placeholders.DEFAULT : oldId);
            config.remove("Preview_Config");
        }
        if (!config.contains("Animation")) {
            String oldId = config.getString("Animation_Config");
            config.set("Animation.Enabled", oldId != null);
            config.set("Animation.Id", oldId == null ? Placeholders.DEFAULT : oldId);
            config.remove("Animation_Config");
        }

        this.setName(config.getString("Name", this.getId()));
        this.setDescription(config.getStringList("Description"));
        this.setItemProvider(ItemTypes.read(config, "ItemProvider"));
        this.setItemStackable(config.getBoolean("ItemStackable", true));

        this.setPreviewEnabled(config.getBoolean("Preview.Enabled"));
        this.setPreviewId(config.getString("Preview.Id", Placeholders.DEFAULT));
        this.setAnimationEnabled(config.getBoolean("Animation.Enabled"));
        this.setAnimationId(config.getString("Animation.Id", Placeholders.DEFAULT));

        this.setPermissionRequired(config.getBoolean("Permission_Required"));
        this.setOpenCooldown(config.getInt("Opening.Cooldown"));

        // Load costs only if EconomyBridge is installed.
        if (Plugins.hasEconomyBridge()) {
            for (String curId : config.getSection("Opening.Cost")) {
                double amount = config.getDouble("Opening.Cost." + curId);
                Cost cost = new Cost(curId, amount);
                this.addOpenCost(cost);
            }
        }

        this.setKeyRequired(config.getBoolean("Key.Required"));
        this.setKeyIds(config.getStringSet("Key.Ids"));

        this.blockPositions.addAll(config.getStringList("Block.Positions").stream().map(WorldPos::deserialize).toList());
        if (!Config.isCrateInAirBlocksAllowed()) {
            new FoliaScheduler(plugin).runTask(() -> {
                List<WorldPos> blockPositionsTemp = new ArrayList<>(blockPositions);
                for (WorldPos pos : blockPositionsTemp) {
                    new FoliaScheduler(plugin).runTask(pos.toLocation(), () -> {
                        Block block = pos.toBlock();

                        if (block == null || block.isEmpty())
                            this.blockPositions.remove(pos);
                    });
                }
            });
        }

        this.setPushbackEnabled(config.getBoolean("Block.Pushback.Enabled"));
        this.setHologramEnabled(config.getBoolean("Block.Hologram.Enabled"));
        this.setHologramTemplateId(config.getString("Block.Hologram.Template", Placeholders.DEFAULT));
        this.setHologramYOffset(config.getDouble("Block.Hologram.Y_Offset", 0D));

        this.setEffectType(config.getString("Block.Effect.Model", EffectId.NONE));
        this.setEffectParticle(UniParticle.read(config, "Block.Effect.Particle"));

        for (String sId : config.getSection("Rewards.List")) {
            Reward reward = RewardFactory.read(this.plugin, this, sId, config, "Rewards.List." + sId);
            this.rewardMap.put(sId, reward);
        }

        // Load milestones only if the feature is enabled.
        if (Config.isMilestonesEnabled()) {
            this.setMilestonesRepeatable(config.getBoolean("Milestones.Repeatable"));
            for (String sId : config.getSection("Milestones.List")) {
                this.milestones.add(Milestone.read(this, config, "Milestones.List." + sId));
            }
        }

        return true;
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        this.writeSettings(config);
        this.writeRewards(config);
        this.writeMilestones(config);
    }

    private void writeSettings(@NotNull FileConfig config) {
        config.set("Name", this.name);
        config.set("Description", this.description);
        config.set("ItemProvider", this.itemProvider);
        config.set("ItemStackable", this.itemStackable);
        config.set("Permission_Required", this.permissionRequired);

        config.set("Preview.Enabled", this.previewEnabled);
        config.set("Preview.Id", this.previewId);
        config.set("Animation.Enabled", this.animationEnabled);
        config.set("Animation.Id", this.animationId);

        config.set("Opening.Cooldown", this.openCooldown);

        // Write costs only if EconomyBridge is installed.
        if (Plugins.hasEconomyBridge()) {
            config.remove("Opening.Cost");
            this.getOpenCosts().forEach(cost -> config.set("Opening.Cost." + cost.getCurrencyId(), cost.getAmount()));
        }

        config.set("Key.Required", this.keyRequired);
        config.set("Key.Ids", this.keyIds);

        config.set("Block.Positions", this.blockPositions.stream().map(WorldPos::serialize).toList());
        config.set("Block.Pushback.Enabled", this.pushbackEnabled);
        config.set("Block.Hologram.Enabled", this.hologramEnabled);
        config.set("Block.Hologram.Template", this.hologramTemplateId);
        config.set("Block.Hologram.Y_Offset", this.hologramYOffset);
        config.set("Block.Effect.Model", this.effectType);
        config.remove("Block.Effect.Particle");
        this.effectParticle.write(config, "Block.Effect.Particle");
    }

    private void writeRewards(@NotNull FileConfig config) {
        config.remove("Rewards.List");
        this.getRewards().forEach(reward -> this.writeReward(config, reward));
    }

    private void writeReward(@NotNull FileConfig config, @NotNull Reward reward) {
        reward.write(config, "Rewards.List." + reward.getId());
    }

    private void writeMilestones(@NotNull FileConfig config) {
        // Write milestones only if the feature is enabled.
        if (!Config.isMilestonesEnabled()) return;

        config.set("Milestones.Repeatable", this.milestonesRepeatable);
        config.remove("Milestones.List");
        int i = 0;
        for (Milestone milestone : this.milestones) {
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

    private void writeConfig(@NotNull Consumer<FileConfig> consumer) {
        FileConfig config = this.getConfig();

        consumer.accept(config);
        config.saveChanges();
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.CRATE.replacer(this);
    }

    @NotNull
    public UnaryOperator<String> replaceAllPlaceholders() {
        return Placeholders.CRATE_EDITOR.replacer(this);
    }

    @Nullable
    public GlobalCrateData getData() {
        return this.plugin.getDataManager().getCrateData(this.getId());
    }

    @Nullable
    public String getLatestOpener() {
        GlobalCrateData data = this.getData();
        return data == null ? null : data.getLatestOpener();
    }

    @Nullable
    public String getLastOpenerName() {
        String last = this.getLatestOpener();
        return last == null ? Lang.OTHER_LAST_OPENER_EMPTY.getString() : last;
    }

    @Nullable
    public String getLatestReward() {
        GlobalCrateData data = this.getData();
        if (data == null || data.getLatestRewardId() == null) return null;

        Reward reward = this.getReward(data.getLatestRewardId());
        return reward == null ? null : reward.getNameTranslated();
    }

    @Nullable
    public String getLastRewardName() {
        String last = this.getLatestReward();
        return last == null ? Lang.OTHER_LAST_REWARD_EMPTY.getString() : last;
    }

    public void createHologram() {
        this.manageHologram(handler -> handler.create(this));
    }

    public void removeHologram() {
        this.manageHologram(handler -> handler.remove(this));
    }

    public void recreateHologram() {
        this.manageHologram(handler -> {
            handler.remove(this);
            handler.refresh(this);
        });
    }

    private void manageHologram(@NotNull Consumer<HologramManager> consumer) {
        if (this.hologramEnabled) {
            this.plugin.manageHolograms(consumer);
        }
    }

    public boolean hasRewards() {
        return !this.rewardMap.isEmpty();
    }

    public boolean hasMilestones() {
        return Config.isMilestonesEnabled() && !this.milestones.isEmpty();
    }

    @NotNull
    public Set<CrateKey> getRequiredKeys() {
        return this.keyIds.stream().map(id -> plugin.getKeyManager().getKeyById(id)).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public boolean isGoodKey(@NotNull CrateKey key) {
        return this.keyIds.contains(key.getId());
    }

    public boolean isAllPhysicalKeys() {
        return this.getRequiredKeys().stream().noneMatch(CrateKey::isVirtual);
    }

    public boolean isAllVirtualKeys() {
        return this.getRequiredKeys().stream().allMatch(CrateKey::isVirtual);
    }

    public boolean isPreviewValid() {
        return this.plugin.getCrateManager().getPreviewById(this.previewId) != null;
    }

    public boolean isAnimationValid() {
        return this.plugin.getOpeningManager().getProviderById(this.animationId) != null;
    }

    public boolean isHologramTemplateValid() {
        return Config.getHologramTemplate(this.hologramTemplateId) != null;
    }

    public boolean isEffectEnabled() {
        return !this.getEffect().isDummy();
    }

    @NotNull
    public CrateEffect getEffect() {
        return EffectRegistry.getEffectOrDummy(this.effectType);
    }

    public boolean hasOpenCost() {
        return !this.openCostMap.isEmpty();
    }

    public boolean hasOpenCooldown() {
        return this.openCooldown != 0;
    }

    public boolean hasPermission(@NotNull Player player) {
        if (!this.isPermissionRequired()) return true;

        return player.hasPermission(this.getPermission());
    }

    public boolean hasCostBypassPermisssion(@NotNull Player player) {
        return player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST) || player.hasPermission(this.getCostBypassPermission());
    }

    public boolean hasCooldownBypassPermission(@NotNull Player player) {
        return player.hasPermission(Perms.BYPASS_CRATE_COOLDOWN);
    }

    public boolean canAffordOpen(@NotNull Player player) {
        if (!this.hasOpenCost()) return true;
        if (this.hasCostBypassPermisssion(player)) return true;

        return this.getOpenCosts().stream().allMatch(cost -> cost.hasEnough(player));
    }

    public void payForOpen(@NotNull Player player) {
        if (!this.hasOpenCost()) return;

        this.getOpenCosts().forEach(cost -> cost.withdraw(player));
    }

    public void refundForOpen(@NotNull Player player) {
        if (!this.hasOpenCost()) return;

        this.getOpenCosts().forEach(cost -> cost.deposit(player));
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
        HologramTemplate template = Config.getHologramTemplate(this.hologramTemplateId);
        return template == null ? Collections.emptyList() : template.getText();
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
            rewards.stream().map(Reward::getRarity).forEach(rewardRarity -> {
                rarities.putIfAbsent(rewardRarity, rewardRarity.getWeight());
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

        this.plugin.getCrateManager().removeCratePositions(this);
        this.blockPositions.add(pos);
        this.plugin.getCrateManager().addCratePositions(this);
    }

    public void clearBlockPositions() {
        this.plugin.getCrateManager().removeCratePositions(this);
        this.blockPositions.clear();
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
        this.name = name;
    }

    @NotNull
    public List<String> getDescription() {
        return this.description;
    }

    public void setDescription(@NotNull List<String> description) {
        this.description = description;
    }

    @NotNull
    public ItemProvider getItemProvider() {
        return this.itemProvider;
    }

    public void setItemProvider(@NotNull ItemProvider itemProvider) {
        this.itemProvider = itemProvider;
    }

    @NotNull
    public ItemStack getRawItem() {
        ItemStack itemStack = this.itemProvider.getItemStack();//new ItemStack(this.itemProvider);

        ItemUtil.editMeta(itemStack, meta -> {
            meta.setDisplayName(this.getNameTranslated());
            meta.setLore(NightMessage.asLegacy(this.description));
        });

        return itemStack;
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack item = this.getRawItem();

        ItemUtil.editMeta(item, meta -> {
            if (!this.isItemStackable()) meta.setMaxStackSize(1);
            PDCUtil.set(meta, Keys.crateId, this.getId());
        });

        return item;
    }

    public boolean isItemStackable() {
        return this.itemStackable;
    }

    public void setItemStackable(boolean itemStackable) {
        this.itemStackable = itemStackable;
    }

    @NotNull
    public String getPreviewId() {
        return this.previewId;
    }

    public void setPreviewId(@NotNull String previewId) {
        this.previewId = previewId.toLowerCase();
    }

    public boolean isPreviewEnabled() {
        return this.previewEnabled;
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        this.previewEnabled = previewEnabled;
    }

    @NotNull
    public String getAnimationId() {
        return this.animationId;
    }

    public void setAnimationId(@NotNull String animationId) {
        this.animationId = animationId.toLowerCase();
    }

    public boolean isAnimationEnabled() {
        return this.animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
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
    public Map<String, Cost> getOpenCostMap() {
        return this.openCostMap;
    }

    @NotNull
    public Set<Cost> getOpenCosts() {
        return new HashSet<>(this.openCostMap.values());
    }

    @Nullable
    public Cost getOpenCost(@NotNull String currency) {
        return this.openCostMap.get(currency.toLowerCase());
    }

    public void addOpenCost(@NotNull Cost cost) {
        this.openCostMap.put(cost.getCurrencyId(), cost);
    }

    public void removeOpenCost(@NotNull Cost cost) {
        this.openCostMap.remove(cost.getCurrencyId());
    }

    public boolean isKeyRequired() {
        return this.keyRequired;
    }

    public void setKeyRequired(boolean keyRequired) {
        this.keyRequired = keyRequired;
    }

    @NotNull
    public Set<String> getKeyIds() {
        return new HashSet<>(this.keyIds);
    }

    public boolean addKeyId(@NotNull String keyId) {
        return this.keyIds.add(keyId.toLowerCase());
    }

    public boolean removeKeyId(@NotNull String keyId) {
        return this.keyIds.remove(keyId.toLowerCase());
    }

    public void setKeyIds(@NotNull Set<String> keyIds) {
        this.keyIds.clear();
        this.keyIds.addAll(Lists.modify(keyIds, String::toLowerCase));
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
    public String getHologramTemplateId() {
        return this.hologramTemplateId;
    }

    public void setHologramTemplateId(@NotNull String hologramTemplateId) {
        this.hologramTemplateId = hologramTemplateId.toLowerCase();
    }

    public double getHologramYOffset() {
        return hologramYOffset;
    }

    public void setHologramYOffset(double hologramYOffset) {
        this.hologramYOffset = hologramYOffset;
    }

    @NotNull
    public String getEffectType() {
        return this.effectType;
    }

    public void setEffectType(@NotNull String effectType) {
        this.effectType = effectType;
    }

    @NotNull
    public UniParticle getEffectParticle() {
        return this.effectParticle;
    }

    public void setEffectParticle(@NotNull UniParticle wrapped) {
        if (!CrateUtils.isSupportedParticle(wrapped.getParticle())) {
            wrapped = UniParticle.of(Particle.CLOUD);
        }

        this.effectParticle = wrapped;
        this.effectParticle.validateData();
    }

    @NotNull
    public LinkedHashMap<String, Reward> getRewardsMap() {
        return this.rewardMap;
    }

    @NotNull
    public Set<Rarity> getRarities() {
        return this.getRewards().stream().map(Reward::getRarity).collect(Collectors.toSet());
    }

    @NotNull
    public Set<String> getRewardIds() {
        return new LinkedHashSet<>(this.rewardMap.keySet());
    }

    @NotNull
    public Set<Reward> getRewards() {
        return new LinkedHashSet<>(this.rewardMap.values());
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

    public void setRewards(@NotNull List<Reward> rewards) {
        this.rewardMap.clear();
        this.rewardMap.putAll(rewards.stream().collect(
            Collectors.toMap(Reward::getId, Function.identity(), (has, add) -> add, LinkedHashMap::new)));
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
        this.plugin.getDataManager().handleRewardRemoval(reward);
    }

    public void removeReward(@NotNull String id) {
        this.rewardMap.remove(id);
    }

    @NotNull
    public Set<Milestone> getMilestones() {
        return this.milestones;
    }

    @Nullable
    public Milestone getMilestone(int openings) {
        return this.milestones.stream().filter(milestone -> milestone.getOpenings() == openings).findFirst().orElse(null);
    }

    public boolean isMilestonesRepeatable() {
        return milestonesRepeatable;
    }

    public void setMilestonesRepeatable(boolean milestonesRepeatable) {
        this.milestonesRepeatable = milestonesRepeatable;
    }

    public int getMaxMilestone() {
        return this.milestones.stream().mapToInt(Milestone::getOpenings).max().orElse(0);
    }

    @Nullable
    public Milestone getNextMilestone(int openings) {
        return this.milestones.stream().filter(milestone -> milestone.getOpenings() > openings).min(Comparator.comparingInt(Milestone::getOpenings)).orElse(null);
    }
}
