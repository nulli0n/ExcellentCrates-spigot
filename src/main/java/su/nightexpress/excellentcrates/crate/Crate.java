package su.nightexpress.excellentcrates.crate;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.utils.*;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.api.hook.HologramHandler;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.editor.CrateEditorCrate;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectSettings;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Crate extends AbstractLoadableItem<ExcellentCrates> implements ICrate {

	private String  name;
	private String  animationConfig;
	private String  previewConfig;
	private boolean isPermissionRequired;
	private int[]   attachedCitizens;

	private int openCooldown;
	private Map<OpenCostType, Double> openCostType;

	private Set<String> keyIds;
	private ItemStack   item;
	
	private Set<Location>       blockLocations;
	private boolean             blockPushbackEnabled;
	private boolean             blockHologramEnabled;
	private double blockHologramOffsetY;
	private List<String>        blockHologramText;
	private CrateEffectSettings blockEffect;

	private LinkedHashMap<String, ICrateReward> rewardMap;
	
	private CratePreview     preview;
	private CrateEditorCrate editor;

	public static Crate fromLegacy(@NotNull JYML cfg) {
		Crate crate = new Crate(ExcellentCrates.getInstance(), cfg.getFile().getName().replace(".yml", ""));

		crate.setName(cfg.getString("name", cfg.getFile().getName()));
		crate.setAnimationConfig(cfg.getString("template", null));
		crate.setPreviewConfig(cfg.getString("preview", Constants.DEFAULT));
		crate.setPermissionRequired(cfg.getBoolean("permission-required"));
		crate.setAttachedCitizens(cfg.getIntArray("attached-citizens"));

		crate.setOpenCooldown(cfg.getInt("cooldown"));
		crate.openCostType = new HashMap<>();
		crate.setOpenCost(OpenCostType.MONEY, cfg.getDouble("open-cost.vault"));
		crate.setOpenCost(OpenCostType.EXP, cfg.getDouble("open-cost.exp"));

		crate.setKeyIds(Sets.newHashSet(cfg.getString("key.id", "")));
		crate.setItem(cfg.getItem("item."));

		crate.setBlockLocations(new HashSet<>(LocationUtil.deserialize(cfg.getStringList("block.locations"))));
		crate.setBlockPushbackEnabled(cfg.getBoolean("block.pushback.enabled"));
		crate.setBlockHologramEnabled(cfg.getBoolean("block.hologram.enabled"));
		crate.setBlockHologramOffsetY(1.5D);
		crate.setBlockHologramText(cfg.getStringList("block.hologram.text"));

		CrateEffectModel model = cfg.getEnum("block.effects.type", CrateEffectModel.class, CrateEffectModel.SIMPLE);
		String particleName = cfg.getString("block.effects.particle", Particle.FLAME.name());
		String particleData = "";
		CrateEffectSettings crateEffectSettings = new CrateEffectSettings(model, particleName, particleData);
		crate.setBlockEffect(crateEffectSettings);

		crate.rewardMap = new LinkedHashMap<>();
		for (String rewId : cfg.getSection("rewards.list")) {
			String path = "rewards.list." + rewId + ".";

			String rewName = cfg.getString(path + "name", rewId);
			double rewChance = cfg.getDouble(path + "chance");
			boolean rBroadcast = false;
			ItemStack rewPreview = cfg.getItem64(path + "preview");
			if (rewPreview == null) rewPreview = new ItemStack(Material.BARRIER);

			int winLimitAmount = -1;
			long winLimitCooldown = 0L;

			List<String> rewCmds = cfg.getStringList(path + "cmds");
			List<ItemStack> rewItem = new ArrayList<>(Stream.of(cfg.getItem64(path + "item")).toList());

			ICrateReward reward = new CrateReward(crate, rewId, rewName, rewChance, rBroadcast,
				winLimitAmount, winLimitCooldown,
				rewPreview, rewItem, rewCmds);
			crate.rewardMap.put(rewId, reward);
		}

		return crate;
	}

	public Crate(@NotNull ExcellentCrates plugin, @NotNull String id) {
		super(plugin, plugin.getDataFolder() + Config.DIR_CRATES + id.toLowerCase() + ".yml");
		
		this.setName("&b" + StringUtil.capitalizeFully(this.getId() + " Crate"));
		this.setAnimationConfig(null); // None
		this.setPreviewConfig(Constants.DEFAULT);
		this.setPermissionRequired(false);
		this.setAttachedCitizens(new int[] {});

		this.setOpenCooldown(0);
		this.openCostType = new HashMap<>();
		this.setOpenCost(OpenCostType.MONEY, 0);
		this.setOpenCost(OpenCostType.EXP, 0);

		this.setKeyIds(new HashSet<>());

		ItemStack item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(this.getName());
		}
		this.setItem(item);
		
		this.setBlockLocations(new HashSet<>());
		this.setBlockPushbackEnabled(true);
		this.setBlockHologramEnabled(false);
		this.setBlockHologramOffsetY(1.5D);
		this.setBlockHologramText(Arrays.asList("&c&l" + this.getName().toUpperCase(), "&7Buy a key at &cwww.myserver.com"));
		this.setBlockEffect(new CrateEffectSettings(CrateEffectModel.HELIX, Particle.FLAME.name(), ""));

		this.setRewardsMap(new LinkedHashMap<>());
	}
	
	public Crate(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
		super(plugin, cfg);
		
		this.setName(cfg.getString("Name", this.getId()));
		this.setAnimationConfig(cfg.getString("Animation_Config"));
		this.setPreviewConfig(cfg.getString("Preview_Config"));
		this.setPermissionRequired(cfg.getBoolean("Permission_Required"));
		this.setAttachedCitizens(cfg.getIntArray("Attached_Citizens"));

		this.setOpenCooldown(cfg.getInt("Opening.Cooldown"));
		this.openCostType = new HashMap<>();
		for (OpenCostType openCostType : OpenCostType.values()) {
			this.setOpenCost(openCostType, cfg.getDouble("Opening.Cost." + openCostType.name()));
		}

		this.setKeyIds(cfg.getStringSet("Key.Ids"));
		this.setItem(cfg.getItemNew("Item.", this.getId()));

		this.setBlockLocations(new HashSet<>(LocationUtil.deserialize(cfg.getStringList("Block.Locations"))));
		this.setBlockPushbackEnabled(cfg.getBoolean("Block.Pushback.Enabled"));
		this.setBlockHologramEnabled(cfg.getBoolean("Block.Hologram.Enabled"));
		this.setBlockHologramOffsetY(cfg.getDouble("Block.Hologram.Offset.Y", 1.5D));
		this.setBlockHologramText(cfg.getStringList("Block.Hologram.Text"));
		
		CrateEffectModel model = cfg.getEnum("Block.Effect.Model", CrateEffectModel.class, CrateEffectModel.SIMPLE);
		String particleName = cfg.getString("Block.Effect.Particle.Name", Particle.FLAME.name());
		String particleData = cfg.getString("Block.Effect.Particle.Data", "");
		CrateEffectSettings crateEffectSettings = new CrateEffectSettings(model, particleName, particleData);
		this.setBlockEffect(crateEffectSettings);

		this.rewardMap = new LinkedHashMap<>();
		for (String rewId : cfg.getSection("Rewards.List")) {
			String path = "Rewards.List." + rewId + ".";

			String rewName = cfg.getString(path + "Name", rewId);
			double rewChance = cfg.getDouble(path + "Chance");
			boolean rBroadcast = cfg.getBoolean(path + "Broadcast");
			ItemStack rewPreview = cfg.getItem64(path + "Preview");
			if (rewPreview == null) rewPreview = new ItemStack(Material.BARRIER);

			int winLimitAmount = cfg.getInt(path + "Win_Limits.Amount", - 1);
			long winLimitCooldown = cfg.getLong(path + "Win_Limits.Cooldown", 0L);

			List<String> rewCmds = cfg.getStringList(path + "Commands");
			List<ItemStack> rewItem = new ArrayList<>(Stream.of(cfg.getItemList64(path + "Items")).toList());
			
			ICrateReward reward = new CrateReward(this, rewId, rewName, rewChance, rBroadcast,
				winLimitAmount, winLimitCooldown,
				rewPreview, rewItem, rewCmds);
			this.rewardMap.put(rewId, reward);
		}

		this.updateHologram();
	}
	
	@Override
	public void onSave() {
    	cfg.set("Name", this.getName());
		cfg.set("Animation_Config", this.getAnimationConfig());
		cfg.set("Preview_Config", this.getPreviewConfig());
    	cfg.set("Permission_Required", this.isPermissionRequired());
		cfg.setIntArray("Attached_Citizens", this.getAttachedCitizens());

		cfg.set("Opening.Cooldown", this.getOpenCooldown());
		for (OpenCostType openCostType : OpenCostType.values()) {
			cfg.set("Opening.Cost." + openCostType.name(), this.getOpenCost(openCostType));
		}

		cfg.set("Key.Ids", this.getKeyIds());
		cfg.setItemNew("Item", this.getItem());

    	cfg.set("Block.Locations", LocationUtil.serialize(new ArrayList<>(this.getBlockLocations())));
    	cfg.set("Block.Pushback.Enabled", this.isBlockPushbackEnabled());
    	cfg.set("Block.Hologram.Enabled", this.isBlockHologramEnabled());
    	cfg.set("Block.Hologram.Offset.Y", this.getBlockHologramOffsetY());
    	cfg.set("Block.Hologram.Text", this.getBlockHologramText());
    	cfg.set("Block.Effect.Model", this.getBlockEffect().getModel().name());
    	cfg.set("Block.Effect.Particle.Name", this.getBlockEffect().getParticleName());
    	cfg.set("Block.Effect.Particle.Data", this.getBlockEffect().getParticleData());

    	cfg.set("Rewards.List", null);
    	
    	for (Entry<String, ICrateReward> e : this.getRewardsMap().entrySet()) {
    		ICrateReward reward = e.getValue();
    		String path = "Rewards.List." + e.getKey() + ".";
    		
    		cfg.set(path + "Name", reward.getName());
    		cfg.set(path + "Chance", reward.getChance());
    		cfg.set(path + "Broadcast", reward.isBroadcast());
    		cfg.set(path + "Win_Limits.Amount", reward.getWinLimitAmount());
    		cfg.set(path + "Win_Limits.Cooldown", reward.getWinLimitCooldown());
			cfg.setItem64(path + "Preview", reward.getPreview());
    		cfg.set(path + "Commands", reward.getCommands());
    		cfg.setItemList64(path + "Items", reward.getItems());
    	}

		this.createPreview();
    	this.updateHologram();
	}

	@Override
	@NotNull
	public UnaryOperator<String> replacePlaceholders() {
		return str -> str
			.replace(PLACEHOLDER_ID, this.getId())
			.replace(PLACEHOLDER_NAME, this.getName())
			.replace(PLACEHOLDER_ANIMATION_CONFIG, String.valueOf(this.getAnimationConfig()))
			.replace(PLACEHOLDER_PREVIEW_CONFIG, String.valueOf(this.getPreviewConfig()))
			.replace(PLACEHOLDER_PERMISSION, Perms.CRATE + this.getId())
			.replace(PLACEHOLDER_PERMISSION_REQUIRED, plugin().lang().getBoolean(this.isPermissionRequired()))
			.replace(PLACEHOLDER_ATTACHED_CITIZENS, Arrays.toString(this.getAttachedCitizens()))
			.replace(PLACEHOLDER_OPENING_COOLDOWN, TimeUtil.formatTime(this.getOpenCooldown() * 1000L))
			.replace(PLACEHOLDER_OPENING_COST_EXP, NumberUtil.format(this.getOpenCost(OpenCostType.EXP)))
			.replace(PLACEHOLDER_OPENING_COST_MONEY, NumberUtil.format(this.getOpenCost(OpenCostType.MONEY)))
			.replace(PLACEHOLDER_KEY_IDS, String.join(DELIMITER_DEFAULT, this.getKeyIds()))
			.replace(PLACEHOLDER_ITEM_NAME, ItemUtil.getItemName(this.getItem()))
			.replace(PLACEHOLDER_ITEM_LORE, String.join("\n", ItemUtil.getLore(this.getItem())))
			.replace(PLACEHOLDER_BLOCK_PUSHBACK_ENABLED, plugin().lang().getBoolean(this.isBlockPushbackEnabled()))
			.replace(PLACEHOLDER_BLOCK_HOLOGRAM_ENABLED, plugin().lang().getBoolean(this.isBlockHologramEnabled()))
			.replace(PLACEHOLDER_BLOCK_HOLOGRAM_OFFSET_Y, NumberUtil.format(this.getBlockHologramOffsetY()))
			.replace(PLACEHOLDER_BLOCK_HOLOGRAM_TEXT, String.join("\n", this.getBlockHologramText()))
			.replace(PLACEHOLDER_BLOCK_LOCATIONS, String.join(DELIMITER_DEFAULT, this.getBlockLocations().stream().map(location -> {
				String x = NumberUtil.format(location.getX());
				String y = NumberUtil.format(location.getY());
				String z = NumberUtil.format(location.getZ());
				String world = location.getWorld() == null ? "null" : location.getWorld().getName();
				return x + ", " + y + ", " + z + " in " + world;
			}).toList()))
			.replace(PLACEHOLDER_BLOCK_EFFECT_MODEL, this.getBlockEffect().getModel().name())
			.replace(PLACEHOLDER_BLOCK_EFFECT_PARTICLE_NAME, this.getBlockEffect().getParticleName())
			.replace(PLACEHOLDER_BLOCK_EFFECT_PARTICLE_DATA, this.getBlockEffect().getParticleData())
			;
	}

	@Override
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
			this.rewardMap.values().forEach(ICrateReward::clear);
			this.rewardMap.clear();
			this.rewardMap = null;
		}
	}

	@Override
	@NotNull
	public CrateEditorCrate getEditor() {
		if (this.editor == null) {
			this.editor = new CrateEditorCrate(this.plugin, this);
		}
		return this.editor;
	}

	@Override
	public void createPreview() {
		this.preview = new CratePreview(this, JYML.loadOrExtract(plugin(), Config.DIR_PREVIEWS + this.getPreviewConfig() + ".yml"));
	}

	@Override
	@Nullable
	public CratePreview getPreview() {
		if (this.preview == null) {
			this.createPreview();
		}
		return this.preview;
	}

	@Override
	public void openPreview(@NotNull Player player) {
		if (this.getPreview() == null) return;
		this.getPreview().open(player, 1);
	}

	@Override
	@NotNull
	public ICrateReward rollReward() {
		Map<ICrateReward, Double> map = new HashMap<>();
		for (ICrateReward reward : this.getRewards()) {
			map.put(reward, reward.getChance());
		}
		ICrateReward crate = Rnd.get(map);
		if (crate == null) {
			throw new IllegalStateException("Unable to roll crate reward for: " + this.getId());
		}
		return crate;
	}

	@Override
	@Nullable
	public ICrateReward rollReward(@NotNull Player player) {
		Map<ICrateReward, Double> map = new HashMap<>();
		for (ICrateReward reward : this.getRewards(player)) {
			map.put(reward, reward.getChance());
		}
		return Rnd.get(map);
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
	public boolean isPermissionRequired() {
		return isPermissionRequired;
	}
	
	@Override
	public void setPermissionRequired(boolean isPermissionRequired) {
		this.isPermissionRequired = isPermissionRequired;
	}
	
	@Override
	public boolean hasPermission(@NotNull Player player) {
		return !this.isPermissionRequired() || (player.hasPermission(Perms.CRATE + this.getId()));
	}
	
	@Override
	@Nullable
	public String getAnimationConfig() {
		return this.animationConfig;
	}
	
	@Override
	public void setAnimationConfig(@Nullable String animationConfig) {
		this.animationConfig = animationConfig == null ? null : animationConfig.toLowerCase();
	}
	
	@Override
	@Nullable
	public String getPreviewConfig() {
		return this.previewConfig;
	}
	
	@Override
	public void setPreviewConfig(@Nullable String previewConfig) {
		this.previewConfig = previewConfig == null ? null : previewConfig.toLowerCase();
	}

	@Override
	public int[] getAttachedCitizens() {
		return this.attachedCitizens;
	}

	@Override
	public void setAttachedCitizens(int[] npcIds) {
		this.attachedCitizens = npcIds;
	}

	@Override
	public int getOpenCooldown() {
		return this.openCooldown;
	}

	@Override
	public void setOpenCooldown(int openCooldown) {
		this.openCooldown = openCooldown;
	}

	@Override
	public double getOpenCost(@NotNull OpenCostType openCostType) {
		return this.openCostType.getOrDefault(openCostType, 0D);
	}

	@Override
	public void setOpenCost(@NotNull OpenCostType openCost, double amount) {
		this.openCostType.put(openCost, amount);
	}
	
	@Override
	@NotNull
	public Set<String> getKeyIds() {
		return keyIds;
	}
	
	@Override
	public void setKeyIds(@NotNull Set<String> keyIds) {
		this.keyIds = new HashSet<>(keyIds.stream().map(String::toLowerCase).toList());
	}
	
	@Override
	@NotNull
	public ItemStack getItem() {
		return new ItemStack(this.item);
	}
	
	@Override
	public void setItem(@NotNull ItemStack item) {
		this.item = new ItemStack(item);
		PDCUtil.setData(this.item, Keys.CRATE_ID, this.getId());
	}
	
	@Override
	@NotNull
	public Set<Location> getBlockLocations() {
		return blockLocations;
	}
	
	@Override
	public void setBlockLocations(@NotNull Set<Location> blockLocations) {
		blockLocations.removeIf(location -> location.getBlock().isEmpty());
		this.blockLocations = blockLocations;
	}
	
	@Override
	public boolean isBlockPushbackEnabled() {
		return this.blockPushbackEnabled;
	}
	
	@Override
	public void setBlockPushbackEnabled(boolean blockPushback) {
		this.blockPushbackEnabled = blockPushback;
	}
	
	@Override
	public boolean isBlockHologramEnabled() {
		return this.blockHologramEnabled;
	}
	
	@Override
	public void setBlockHologramEnabled(boolean blockHologramEnabled) {
		this.blockHologramEnabled = blockHologramEnabled;
	}

	@Override
	public double getBlockHologramOffsetY() {
		return blockHologramOffsetY;
	}

	@Override
	public void setBlockHologramOffsetY(double blockHologramOffsetY) {
		this.blockHologramOffsetY = blockHologramOffsetY;
	}

	@Override
	@NotNull
	public List<String> getBlockHologramText() {
		return new ArrayList<>(this.blockHologramText);
	}
	
	@Override
	public void setBlockHologramText(@NotNull List<String> blockHologramText) {
		this.blockHologramText = StringUtil.color(blockHologramText);
	}

	@Override
	public void createHologram() {
		if (!this.isBlockHologramEnabled()) return;

		HologramHandler hologramHandler = plugin.getHologramHandler();
		if (hologramHandler == null) return;

		hologramHandler.create(this);
	}

	@Override
	public void removeHologram() {
		HologramHandler hologramHandler = plugin.getHologramHandler();
		if (hologramHandler == null) return;

		hologramHandler.remove(this);
	}

	@Override
	public void updateHologram() {
		this.removeHologram();
		this.createHologram();
	}

	@Override
	@NotNull
	public CrateEffectSettings getBlockEffect() {
		return this.blockEffect;
	}

	public void setBlockEffect(@NotNull CrateEffectSettings blockEffect) {
		this.blockEffect = blockEffect;
	}
	
	@Override
	@NotNull
	public LinkedHashMap<String, ICrateReward> getRewardsMap() {
		return this.rewardMap;
	}
	
	@Override
	public void setRewardsMap(@NotNull LinkedHashMap<String, ICrateReward> rewards) {
		this.rewardMap = rewards;
	}

	@Override
	@NotNull
	public Collection<ICrateReward> getRewards(@NotNull Player player) {
		return this.getRewards().stream().filter(reward -> reward.canWin(player)).toList();
	}
}
