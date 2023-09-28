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
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.*;
import su.nexmedia.engine.utils.random.Rnd;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.api.hologram.HologramHandler;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.editor.CrateMainEditor;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Crate extends AbstractConfigHolder<ExcellentCrates> implements Placeholder {

    private String name;
    private String openingConfig;
    private String previewConfig;
    private boolean isPermissionRequired;

    private       int                       openCooldown;
    private final Map<OpenCostType, Double> openCostType;

    private Set<String> keyIds;
    private ItemStack   item;

    private final Set<Location> blockLocations;
    private       boolean       blockPushbackEnabled;
    private boolean             blockHologramEnabled;
    private double              blockHologramOffsetY;
    private List<String>        blockHologramText;
    private CrateEffectModel blockEffectModel;
    private UniParticle blockEffectParticle;

    private LinkedHashMap<String, CrateReward> rewardMap;

    private CratePreview    preview;
    private CrateMainEditor editor;

    private boolean milestonesRepeatable;

    private final Set<Milestone> milestones;
    private final PlaceholderMap placeholderMap;

    public Crate(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.setKeyIds(new HashSet<>());
        this.openCostType = new HashMap<>();
        this.setRewardsMap(new LinkedHashMap<>());
        this.blockLocations = new HashSet<>();
        this.milestones = new HashSet<>();

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CRATE_ID, this.getId())
            .add(Placeholders.CRATE_NAME, this::getName)
            .add(Placeholders.CRATE_ANIMATION_CONFIG, () -> String.valueOf(this.getOpeningConfig()))
            .add(Placeholders.CRATE_PREVIEW_CONFIG, () -> String.valueOf(this.getPreviewConfig()))
            .add(Placeholders.CRATE_PERMISSION, this::getPermission)
            .add(Placeholders.CRATE_PERMISSION_REQUIRED, () -> LangManager.getBoolean(this.isPermissionRequired()))
            .add(Placeholders.CRATE_OPENING_COOLDOWN, () -> {
                if (this.getOpenCooldown() == 0L) return "-";
                if (this.getOpenCooldown() < 0L) return LangManager.getPlain(Lang.OTHER_ONE_TIMED);

                return TimeUtil.formatTime(this.getOpenCooldown() * 1000L);
            })
            .add(Placeholders.CRATE_OPENING_COST_EXP, () -> NumberUtil.format(this.getOpenCost(OpenCostType.EXP)))
            .add(Placeholders.CRATE_OPENING_COST_MONEY, () -> NumberUtil.format(this.getOpenCost(OpenCostType.MONEY)))
            .add(Placeholders.CRATE_KEY_IDS, () -> String.join(", ", this.getKeyIds()))
            .add(Placeholders.CRATE_ITEM_NAME, () -> ItemUtil.getItemName(this.getItem()))
            .add(Placeholders.CRATE_ITEM_LORE, () -> String.join("\n", ItemUtil.getLore(this.getItem())))
            .add(Placeholders.CRATE_BLOCK_PUSHBACK_ENABLED, () -> LangManager.getBoolean(this.isBlockPushbackEnabled()))
            .add(Placeholders.CRATE_BLOCK_HOLOGRAM_ENABLED, () -> LangManager.getBoolean(this.isBlockHologramEnabled()))
            .add(Placeholders.CRATE_BLOCK_HOLOGRAM_OFFSET_Y, () -> NumberUtil.format(this.getBlockHologramOffsetY()))
            .add(Placeholders.CRATE_BLOCK_HOLOGRAM_TEXT, () -> String.join("\n", this.getBlockHologramText()))
            .add(Placeholders.CRATE_BLOCK_LOCATIONS, () -> String.join("\n", this.getBlockLocations().stream().map(location -> {
                String x = NumberUtil.format(location.getX());
                String y = NumberUtil.format(location.getY());
                String z = NumberUtil.format(location.getZ());
                String world = location.getWorld() == null ? "null" : location.getWorld().getName();
                return x + ", " + y + ", " + z + " in " + world;
            }).toList()))
            .add(Placeholders.CRATE_BLOCK_EFFECT_MODEL, () -> this.getBlockEffectModel().name())
            .add(Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_NAME, () -> this.getBlockEffectParticle().getParticle().name())
            .add(Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_DATA, () -> String.valueOf(this.getBlockEffectParticle().getData()))
            .add(Placeholders.CRATE_MILESTONES_REPEATABLE, () -> LangManager.getBoolean(this.isMilestonesRepeatable()))
        ;
    }

    @Override
    public boolean load() {
        this.setName(cfg.getString("Name", this.getId()));
        this.setOpeningConfig(cfg.getString("Animation_Config"));
        this.setPreviewConfig(cfg.getString("Preview_Config"));
        this.setPermissionRequired(cfg.getBoolean("Permission_Required"));

        this.setOpenCooldown(cfg.getInt("Opening.Cooldown"));
        for (OpenCostType openCostType : OpenCostType.values()) {
            this.setOpenCost(openCostType, cfg.getDouble("Opening.Cost." + openCostType.name()));
        }

        this.setKeyIds(cfg.getStringSet("Key.Ids"));
        this.setItem(cfg.getItem("Item"));

        //this.getBlockLocations().addAll(LocationUtil.deserialize(cfg.getStringList("Block.Locations")));
        this.setBlockPushbackEnabled(cfg.getBoolean("Block.Pushback.Enabled"));
        this.setBlockHologramEnabled(cfg.getBoolean("Block.Hologram.Enabled"));
        this.setBlockHologramOffsetY(cfg.getDouble("Block.Hologram.Offset.Y", 1.5D));
        this.setBlockHologramText(cfg.getStringList("Block.Hologram.Text"));

        CrateEffectModel model = cfg.getEnum("Block.Effect.Model", CrateEffectModel.class, CrateEffectModel.SIMPLE);
        UniParticle particle = UniParticle.read(cfg, "Block.Effect.Particle");
        this.setBlockEffectModel(model);
        this.setBlockEffectParticle(particle);

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

            CrateReward reward = new CrateReward(this, rewId, rewName, rewChance, rarity, rBroadcast,
                winLimitAmount, winLimitCooldown,
                rewPreview, rewItem, rewCmds, ignoredForPerms);
            this.rewardMap.put(rewId, reward);
        }

        this.setMilestonesRepeatable(cfg.getBoolean("Milestones.Repeatable"));
        for (String sId : cfg.getSection("Milestones.List")) {
            this.getMilestones().add(Milestone.read(cfg, "Milestones.List." + sId));
        }

        this.createPreview();
        return true;
    }

    public void loadLocations() {
        this.getBlockLocations().addAll(LocationUtil.deserialize(cfg.getStringList("Block.Locations")));
        this.updateHologram();
    }

    @Override
    public void onSave() {
        cfg.set("Name", this.getName());
        cfg.set("Animation_Config", this.getOpeningConfig());
        cfg.set("Preview_Config", this.getPreviewConfig());
        cfg.set("Permission_Required", this.isPermissionRequired());

        cfg.set("Opening.Cooldown", this.getOpenCooldown());
        for (OpenCostType openCostType : OpenCostType.values()) {
            cfg.set("Opening.Cost." + openCostType.name(), this.getOpenCost(openCostType));
        }

        cfg.set("Key.Ids", this.getKeyIds());
        cfg.setItem("Item", this.getRawItem());

        cfg.set("Block.Locations", LocationUtil.serialize(new ArrayList<>(this.getBlockLocations())));
        cfg.set("Block.Pushback.Enabled", this.isBlockPushbackEnabled());
        cfg.set("Block.Hologram.Enabled", this.isBlockHologramEnabled());
        cfg.set("Block.Hologram.Offset.Y", this.getBlockHologramOffsetY());
        cfg.set("Block.Hologram.Text", this.getBlockHologramText());
        cfg.set("Block.Effect.Model", this.getBlockEffectModel().name());
        cfg.remove("Block.Effect.Particle");
        this.getBlockEffectParticle().write(cfg, "Block.Effect.Particle");

        cfg.set("Rewards.List", null);
        for (Entry<String, CrateReward> e : this.getRewardsMap().entrySet()) {
            CrateReward reward = e.getValue();
            String path = "Rewards.List." + e.getKey() + ".";

            cfg.set(path + "Name", reward.getName());
            cfg.set(path + "Chance", reward.getChance());
            cfg.set(path + "Rarity", reward.getRarity().getId());
            cfg.set(path + "Broadcast", reward.isBroadcast());
            cfg.set(path + "Win_Limits.Amount", reward.getWinLimitAmount());
            cfg.set(path + "Win_Limits.Cooldown", reward.getWinLimitCooldown());
            cfg.setItemEncoded(path + "Preview", reward.getPreview());
            cfg.set(path + "Commands", reward.getCommands());
            cfg.setItemsEncoded(path + "Items", reward.getItems());
            cfg.set(path + "Ignored_For_Permissions", reward.getIgnoredForPermissions());
        }

        cfg.set("Milestones.Repeatable", this.isMilestonesRepeatable());
        cfg.set("Milestones.List", null);
        int i = 0;
        for (Milestone milestone : this.getMilestones()) {
            milestone.write(cfg, "Milestones.List." + (i++));
        }
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    public void clear() {
        this.removeHologram();
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
        if (this.preview != null) {
            this.preview.clear();
            this.preview = null;
        }
        if (this.rewardMap != null) {
            this.rewardMap.values().forEach(CrateReward::clear);
            this.rewardMap.clear();
            this.rewardMap = null;
        }
    }

    @NotNull
    public CrateMainEditor getEditor() {
        if (this.editor == null) {
            this.editor = new CrateMainEditor(this);
        }
        return this.editor;
    }

    @Nullable
    public CratePreview getPreview() {
        return this.preview;
    }

    public void createPreview() {
        if (this.getPreviewConfig() == null) {
            return;
        }
        if (this.preview != null) {
            this.preview.clear();
            this.preview = null;
        }
        this.preview = new CratePreview(this, JYML.loadOrExtract(plugin(), Config.DIR_PREVIEWS + this.getPreviewConfig() + ".yml"));
    }

    public void openPreview(@NotNull Player player) {
        if (this.getPreview() == null) return;
        this.getPreview().open(player, 1);
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

    @NotNull
    public String getPermission() {
        return Perms.PREFIX_CRATE + this.getId();
    }

    public boolean hasPermission(@NotNull Player player) {
        return !this.isPermissionRequired() || (player.hasPermission(this.getPermission()));
    }

    public int getOpenCooldown() {
        return this.openCooldown;
    }

    public void setOpenCooldown(int openCooldown) {
        this.openCooldown = openCooldown;
    }

    public double getOpenCost(@NotNull OpenCostType openCostType) {
        return this.openCostType.getOrDefault(openCostType, 0D);
    }

    public void setOpenCost(@NotNull OpenCostType openCost, double amount) {
        this.openCostType.put(openCost, amount);
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

    public boolean isBlockPushbackEnabled() {
        return this.blockPushbackEnabled;
    }

    public void setBlockPushbackEnabled(boolean blockPushback) {
        this.blockPushbackEnabled = blockPushback;
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

    public boolean isBlockHologramEnabled() {
        return this.blockHologramEnabled;
    }

    public void setBlockHologramEnabled(boolean blockHologramEnabled) {
        this.blockHologramEnabled = blockHologramEnabled;
    }

    public double getBlockHologramOffsetY() {
        return blockHologramOffsetY;
    }

    public void setBlockHologramOffsetY(double blockHologramOffsetY) {
        this.blockHologramOffsetY = blockHologramOffsetY;
    }

    @NotNull
    public List<String> getBlockHologramText() {
        return new ArrayList<>(this.blockHologramText);
    }

    public void setBlockHologramText(@NotNull List<String> blockHologramText) {
        this.blockHologramText = Colorizer.apply(blockHologramText);
    }

    @NotNull
    public Location getBlockHologramLocation(@NotNull Location loc) {
        double offset = this.getBlockHologramOffsetY();
        return LocationUtil.getCenter(loc.clone()).add(0D, offset, 0D);
    }

    public void createHologram() {
        if (!this.isBlockHologramEnabled()) return;

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

    @NotNull
    public CrateEffectModel getBlockEffectModel() {
        return blockEffectModel;
    }

    public void setBlockEffectModel(@NotNull CrateEffectModel blockEffectModel) {
        this.blockEffectModel = blockEffectModel;
    }

    @NotNull
    public UniParticle getBlockEffectParticle() {
        return blockEffectParticle;
    }

    public void setBlockEffectParticle(@NotNull UniParticle blockEffectParticle) {
        this.blockEffectParticle = blockEffectParticle;
    }

    @NotNull
    public LinkedHashMap<String, CrateReward> getRewardsMap() {
        return this.rewardMap;
    }

    public void setRewardsMap(@NotNull LinkedHashMap<String, CrateReward> rewards) {
        this.rewardMap = rewards;
    }

    @NotNull
    public Collection<CrateReward> getRewards() {
        return this.getRewardsMap().values();
    }

    @NotNull
    public List<CrateReward> getRewards(@NotNull Rarity rarity) {
        return this.getRewards().stream().filter(reward -> reward.getRarity() == rarity).toList();
    }

    @NotNull
    public List<CrateReward> getRewards(@NotNull Player player) {
        return this.getRewards().stream().filter(reward -> reward.canWin(player)).toList();
    }

    @NotNull
    public List<CrateReward> getRewards(@NotNull Player player, @NotNull Rarity rarity) {
        return this.getRewards().stream().filter(reward -> reward.getRarity() == rarity && reward.canWin(player)).toList();
    }

    public void setRewards(@NotNull List<CrateReward> rewards) {
        this.setRewardsMap(rewards.stream().collect(
            Collectors.toMap(CrateReward::getId, Function.identity(), (has, add) -> add, LinkedHashMap::new)));
    }

    @Nullable
    public CrateReward getReward(@NotNull String id) {
        return this.getRewardsMap().get(id.toLowerCase());
    }

    @Nullable
    public CrateReward getMilestoneReward(int openings) {
        Milestone milestone = this.getMilestone(openings);
        return milestone == null ? null : this.getMilestoneReward(milestone);
    }

    @Nullable
    public CrateReward getMilestoneReward(@NotNull Milestone milestone) {
        return this.getReward(milestone.getRewardId());
    }

    public void addReward(@NotNull CrateReward crateReward) {
        this.getRewardsMap().put(crateReward.getId(), crateReward);
    }

    public void removeReward(@NotNull CrateReward crateReward) {
        this.removeReward(crateReward.getId());
    }

    public void removeReward(@NotNull String id) {
        this.getRewardsMap().remove(id);
    }

    @NotNull
    public CrateReward rollReward() {
        return this.rollReward(null);
    }

    @NotNull
    public CrateReward rollReward(@Nullable Player player) {
        Collection<CrateReward> allRewards = player == null ? this.getRewards() : this.getRewards(player);

        Map<Rarity, Double> rarities = new HashMap<>();
        allRewards.stream().map(CrateReward::getRarity).forEach(rarity -> {
            rarities.putIfAbsent(rarity, rarity.getChance());
        });

        Rarity rarity = Rnd.getByWeight(rarities);

        Map<CrateReward, Double> rewards = new HashMap<>();
        allRewards.stream().filter(reward -> reward.getRarity() == rarity).forEach(reward -> {
            rewards.put(reward, reward.getChance());
        });
        return Rnd.getByWeight(rewards);
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
