package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.cost.CostTypeId;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.EcoCostEntry;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.KeyCostEntry;
import su.nightexpress.excellentcrates.crate.cost.type.impl.EcoCostType;
import su.nightexpress.excellentcrates.crate.cost.type.impl.KeyCostType;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.excellentcrates.crate.reward.RewardFactory;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.hologram.HologramManager;
import su.nightexpress.excellentcrates.hologram.HologramTemplate;
import su.nightexpress.excellentcrates.registry.CratesRegistries;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.manager.ConfigBacked;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.problem.ProblemCollector;
import su.nightexpress.nightcore.util.problem.ProblemReporter;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Crate implements ConfigBacked {

    private final CratesPlugin plugin;
    private final Path         filePath;
    private final String       id;

    private final Set<WorldPos>                 blockPositions;
    private final Set<Milestone>                milestones;
    private final Map<String, Cost>             costMap;
    private final LinkedHashMap<String, Reward> rewardMap;

    private String      name;
    private List<String> description;
    private AdaptedItem  item;
    private boolean      itemStackable;

    private boolean previewEnabled;
    private String  previewId;
    private boolean openingEnabled;
    private String  openingId;

    private boolean openingCooldownEnabled;
    private int     openingCooldownTime;

    private boolean permissionRequired;

    private boolean milestonesRepeatable;
    private boolean     pushbackEnabled;

    private boolean hologramEnabled;
    private String  hologramTemplateId;
    private double  hologramYOffset;

    private String      effectType;
    private UniParticle effectParticle;

    private boolean dirty;

    public Crate(@NotNull CratesPlugin plugin, @NotNull Path path, @NotNull String id) {
        this.plugin = plugin;
        this.filePath = path;
        this.id = id;

        this.costMap = new LinkedHashMap<>();
        this.rewardMap = new LinkedHashMap<>();
        this.blockPositions = new HashSet<>();
        this.milestones = new HashSet<>();
        this.description = new ArrayList<>();
    }

    public void load() throws IllegalStateException {
        if (!this.hasFile()) {
            // TODO Throw
            return;
        }

        this.loadConfig().edit(this::load);
    }

    private void load(@NotNull FileConfig config) throws IllegalStateException {
        if (!config.contains("_dataver")) {
            config.set("_dataver", 600);

            Path source = this.getPath();
            Path target = Path.of(source.getParent() + "/backups", source.getFileName() + ".backup535");
            FileUtil.createFileIfNotExists(target);

            try {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException exception) {
                this.plugin.error("Could not backup crate file: " + source);
                exception.printStackTrace();
            }
        }

        if (config.contains("Item")) {
            ItemStack itemStack = config.getCosmeticItem("Item").getItemStack();
            AdaptedItem provider = ItemHelper.vanilla(itemStack);
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
        if (config.contains("Opening.Cooldown")) {
            int old = config.getInt("Opening.Cooldown");
            config.set("OpeningCooldown.Enabled", old != 0);
            config.set("OpeningCooldown.Value", this.openingCooldownTime);
            config.remove("Opening");
        }

        this.setName(config.getString("Name", this.getId()));
        this.setDescription(config.getStringList("Description"));

        this.setItem(ItemHelper.read(config, "ItemProvider").orElse(ItemHelper.vanilla(CrateUtils.getDefaultItem(this))));
        this.setItemStackable(config.getBoolean("ItemStackable", true));

        this.setPreviewEnabled(config.getBoolean("Preview.Enabled"));
        this.setPreviewId(config.getString("Preview.Id", Placeholders.DEFAULT));
        this.setOpeningEnabled(config.getBoolean("Animation.Enabled"));
        this.setOpeningId(config.getString("Animation.Id", Placeholders.DEFAULT));

        this.setOpeningCooldownEnabled(config.getBoolean("OpeningCooldown.Enabled"));
        this.setOpeningCooldownTime(config.getInt("OpeningCooldown.Value"));

        this.setPermissionRequired(config.getBoolean("Permission_Required"));

        if (config.contains("Opening.Cost") || config.contains("Key.Ids")) {
            boolean keyRequired = config.getBoolean("Key.Required");
            Set<String> oldKeys = config.getStringSet("Key.Ids");

            if (!oldKeys.isEmpty() && CratesRegistries.getCostType(CostTypeId.KEY) instanceof KeyCostType keyType) {
                oldKeys.forEach(keyId -> {
                    KeyCostEntry entry = keyType.createEmpty();
                    entry.setKeyId(keyId);
                    entry.setAmount(1);

                    Cost cost = new Cost("key_" + keyId, keyRequired, keyId + " Key", ItemHelper.vanilla(new ItemStack(Material.TRIAL_KEY)), Collections.singletonList(entry));
                    config.set("CostOptions." + cost.getId(), cost);
                });
            }

            if (CratesRegistries.getCostType(CostTypeId.CURRENCY) instanceof EcoCostType ecoType) {
                for (String curId : config.getSection("Opening.Cost")) {
                    Currency currency = EconomyBridge.getCurrency(curId);
                    if (currency == null) continue;

                    double amount = config.getDouble("Opening.Cost." + curId);

                    EcoCostEntry entry = ecoType.createEmpty();
                    entry.setCurrencyId(curId);
                    entry.setAmount(amount);

                    Cost cost = new Cost("eco_" + curId, keyRequired, curId + " Currency", ItemHelper.adapt(currency.getIcon()), Collections.singletonList(entry));
                    config.set("CostOptions." + cost.getId(), cost);
                }
            }

            config.remove("Opening.Cost");
            config.remove("Key");
        }

        config.getSection("CostOptions").forEach(sId -> {
            Cost cost = Cost.read(config, "CostOptions." + sId, sId);
            this.addCost(cost);
        });

        this.blockPositions.addAll(config.getStringList("Block.Positions").stream().map(WorldPos::deserialize).toList());
        if (!Config.isCrateInAirBlocksAllowed()) {
            this.blockPositions.removeIf(pos -> {
                Block block = pos.toBlock();
                return block != null && block.isEmpty();
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
    }

    public void saveForce() {
        this.markDirty();
        this.saveIfDirty();
    }

    public void saveIfDirty() {
        if (this.dirty) {
            this.loadConfig().edit(this::write);
            this.dirty = false;
        }
    }

    private void write(@NotNull FileConfig config) {
        this.writeSettings(config);
        this.writeRewards(config);
        this.writeMilestones(config);
    }

    private void writeSettings(@NotNull FileConfig config) {
        config.set("Name", this.name);
        config.set("Description", this.description);
        config.set("ItemProvider", this.item);
        config.set("ItemStackable", this.itemStackable);
        config.set("Permission_Required", this.permissionRequired);

        config.set("Preview.Enabled", this.previewEnabled);
        config.set("Preview.Id", this.previewId);
        config.set("Animation.Enabled", this.openingEnabled);
        config.set("Animation.Id", this.openingId);

        config.set("OpeningCooldown.Enabled", this.openingCooldownEnabled);
        config.set("OpeningCooldown.Value", this.openingCooldownTime);

        config.remove("CostOptions");
        this.getCosts().forEach(cost -> config.set("CostOptions." + cost.getId(), cost));

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

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.CRATE.replacer(this);
    }

    public boolean hasProblems() {
        return !this.collectProblems().isEmpty();
    }

    @NotNull
    public ProblemReporter collectProblems() {
        ProblemCollector collector = new ProblemCollector(this.getName(), this.filePath.toString());

        if (!this.item.isValid()) collector.report(Lang.INSPECTIONS_GENERIC_ITEM.get(false));
        if (this.isPreviewEnabled() && !this.isPreviewValid()) collector.report(Lang.INSPECTIONS_CRATE_PREVIEW.get(false));
        if (this.isOpeningEnabled() && !this.isOpeningValid()) collector.report(Lang.INSPECTIONS_CRATE_OPENING.get(false));
        if (this.isHologramEnabled() && !this.isHologramTemplateValid()) collector.report(Lang.INSPECTIONS_CRATE_HOLOGRAM.get(false));

        this.costMap.values().forEach(cost -> {
            ProblemReporter reporter = cost.collectProblems();
            if (reporter.isEmpty()) return;

            collector.children("Problems in '" + cost.getId() + "' cost option.", reporter);
        });

        this.rewardMap.values().forEach(reward -> {
            ProblemReporter reporter = reward.collectProblems();
            if (reporter.isEmpty()) return;

            collector.children("Problems in '" + reward.getId() + "' reward.", reporter);
        });

        return collector;
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
        return last == null ? Lang.OTHER_LAST_OPENER_EMPTY.text() : last;
    }

    @Nullable
    public String getLatestReward() {
        GlobalCrateData data = this.getData();
        if (data == null || data.getLatestRewardId() == null) return null;

        Reward reward = this.getReward(data.getLatestRewardId());
        return reward == null ? null : reward.getName();
    }

    @Nullable
    public String getLastRewardName() {
        String last = this.getLatestReward();
        return last == null ? Lang.OTHER_LAST_REWARD_EMPTY.text() : last;
    }

    public void createHologram() {
        this.manageHologram(handler -> handler.render(this));
    }

    public void removeHologram() {
        this.manageHologram(handler -> handler.discard(this));
    }

    public void recreateHologram() {
        this.manageHologram(handler -> {
            handler.discard(this);
            handler.render(this);
        });
    }

    private void manageHologram(@NotNull Consumer<HologramManager> consumer) {
        if (this.hologramEnabled) {
            this.plugin.getHologramManager().ifPresent(consumer);
        }
    }

    public boolean hasRewards() {
        return !this.rewardMap.isEmpty();
    }

    public boolean hasMilestones() {
        return Config.isMilestonesEnabled() && !this.milestones.isEmpty();
    }

    public boolean isPreviewValid() {
        return this.plugin.getCrateManager().getPreviewById(this.previewId) != null;
    }

    public boolean isOpeningValid() {
        return this.plugin.getOpeningManager().getProviderById(this.openingId) != null;
    }

    public boolean isHologramTemplateValid() {
        return Config.getHologramTemplate(this.hologramTemplateId) != null;
    }

    public boolean isEffectEnabled() {
        return !this.getEffect().isDummy();
    }

    @NotNull
    public CrateEffect getEffect() {
        return CratesRegistries.effectOrDummy(this.effectType);
    }

    public boolean hasPermission(@NotNull Player player) {
        if (!this.isPermissionRequired()) return true;

        return player.hasPermission(this.getPermission());
    }

    public boolean hasCooldownBypassPermission(@NotNull Player player) {
        return player.hasPermission(Perms.BYPASS_CRATE_COOLDOWN);
    }

    @NotNull
    public String getPermission() {
        return Perms.PREFIX_CRATE + this.getId();
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

    public int countRewards() {
        return this.rewardMap.size();
    }

    public int countMilestones() {
        return this.milestones.size();
    }

    public int countMaxOpenings(@NotNull Player player) {
        return this.getCosts().stream().filter(Cost::isEnabled).mapToInt(cost -> cost.countMaxOpenings(player)).max().orElse(-1);
    }

    public void markDirty() {
        this.dirty = true;
    }

    public boolean hasFile() {
        return Files.exists(this.filePath);
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @Override
    @NotNull
    public Path getPath() {
        return this.filePath;
    }

    @NotNull
    public String getName() {
        return this.name;
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
    public AdaptedItem getItem() {
        return this.item;
    }

    public void setItem(@NotNull AdaptedItem item) {
        this.item = item;
    }

    @NotNull
    public ItemStack getRawItemStack() {
        return this.getItemStack(false);
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.getItemStack(true);
    }

    @NotNull
    public ItemStack getItemStack(boolean fullData) {
        ItemStack itemStack = this.item.itemStack().orElse(CrateUtils.getDefaultItem(this));

        ItemUtil.editMeta(itemStack, meta -> {
            //ItemUtil.setCustomName(meta, this.name);
            //ItemUtil.setLore(meta, this.description);

            if (fullData) {
                if (!this.isItemStackable()) meta.setMaxStackSize(1);
                PDCUtil.set(meta, Keys.crateId, this.getId());
            }
        });

        return itemStack;
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
    public String getOpeningId() {
        return this.openingId;
    }

    public void setOpeningId(@NotNull String openingId) {
        this.openingId = openingId.toLowerCase();
    }

    public boolean isOpeningEnabled() {
        return this.openingEnabled;
    }

    public void setOpeningEnabled(boolean openingEnabled) {
        this.openingEnabled = openingEnabled;
    }



    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    public void setPermissionRequired(boolean isPermissionRequired) {
        this.permissionRequired = isPermissionRequired;
    }

    public boolean isOpeningCooldownEnabled() {
        return this.openingCooldownEnabled;
    }

    public void setOpeningCooldownEnabled(boolean openingCooldownEnabled) {
        this.openingCooldownEnabled = openingCooldownEnabled;
    }

    public int getOpeningCooldownTime() {
        return this.openingCooldownTime;
    }

    public void setOpeningCooldownTime(int openingCooldownTime) {
        this.openingCooldownTime = openingCooldownTime;
    }

    @NotNull
    public Map<String, Cost> getCostMap() {
        return this.costMap;
    }

    @NotNull
    public List<Cost> getCosts() {
        return new ArrayList<>(this.costMap.values());
    }

    public void addCost(@NotNull Cost cost) {
        this.costMap.put(cost.getId(), cost);
    }

    public void removeCost(@NotNull Cost cost) {
        this.costMap.remove(cost.getId());
    }

    public boolean hasCost(@NotNull String id) {
        return this.costMap.containsKey(id);
    }

    @Nullable
    public Cost getCost(@NotNull String id) {
        return this.costMap.get(id);
    }

    @NotNull
    public Optional<Cost> getFirstCost() {
        return this.getCosts().stream().filter(Cost::isAvailable).findFirst();
    }

    public boolean hasCost() {
        return !this.costMap.isEmpty() && this.getCosts().stream().anyMatch(Cost::isAvailable);
    }

    public boolean hasMultipleCosts() {
        return this.getCosts().stream().filter(Cost::isAvailable).count() >= 2;
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
        if (!CrateUtils.isSupportedParticle(wrapped.getParticle()) || wrapped.getParticle() == null) {
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
