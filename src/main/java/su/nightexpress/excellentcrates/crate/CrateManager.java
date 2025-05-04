package su.nightexpress.excellentcrates.crate;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.excellentcrates.crate.effect.EffectRegistry;
import su.nightexpress.excellentcrates.crate.impl.*;
import su.nightexpress.excellentcrates.crate.listener.CrateListener;
import su.nightexpress.excellentcrates.crate.menu.MilestonesMenu;
import su.nightexpress.excellentcrates.crate.menu.PreviewMenu;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.data.reward.RewardLimit;
import su.nightexpress.excellentcrates.hologram.HologramTemplate;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.excellentcrates.util.inspect.Inspectors;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.tag.Tags;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.io.File;
import java.util.*;

public class CrateManager extends AbstractManager<CratesPlugin> {

    private final Map<String, Rarity>      rarityByIdMap;
    private final Map<String, Crate>       crateByIdMap;
    private final Map<WorldPos, Crate>     crateByPosMap;
    private final Map<String, PreviewMenu> previewByIdMap;
    private final Map<UUID, Long>          previewCooldown;

    private MilestonesMenu milestonesMenu;

    public CrateManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.rarityByIdMap = new HashMap<>();
        this.crateByIdMap = new HashMap<>();
        this.crateByPosMap = new HashMap<>();
        this.previewByIdMap = new HashMap<>();
        this.previewCooldown = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.updateHologramTemplates();

        this.loadRarities();
        this.loadPreviews();
        this.loadCrates();
        this.loadUI();
        this.plugin.runTask(this::runInspections); // After everything is loaded.

        this.addListener(new CrateListener(this.plugin, this));

        this.addAsyncTask(this::playCrateEffects, 1L);
    }

    @Override
    protected void onShutdown() {
        if (this.milestonesMenu != null) this.milestonesMenu.clear();

        this.previewByIdMap.values().forEach(PreviewMenu::clear);
        this.previewByIdMap.clear();
        this.crateByIdMap.clear();
        this.crateByPosMap.clear();
        this.rarityByIdMap.clear();
    }

    private void updateHologramTemplates() {
        FileConfig config = this.plugin.getConfig();
        if (!config.contains("Crate.Holograms.Templates")) return;

        config.remove("Crate.Holograms.TemplateList"); // Remove newly generated stuff.
        config.getSection("Crate.Holograms.Templates").forEach(sId -> {
            List<String> text = config.getStringList("Crate.Holograms.Templates." + sId);
            HologramTemplate template = new HologramTemplate(sId, text);
            template.write(config, "Crate.Holograms.TemplateList." + sId);
        });
        config.remove("Crate.Holograms.Templates"); // Remove old shit

        Config.CRATE_HOLOGRAM_TEMPLATES.read(config); // Re-read
    }

    private void loadRarities() {
        FileConfig config = this.plugin.getConfig();
        
        if (config.getSection("Rewards.Rarities").isEmpty()) {
            Set<Rarity> rarities = new HashSet<>();

            File oldFile = new File(plugin.getDataFolder(), "rarity.yml");
            if (oldFile.exists()) {
                FileConfig oldConfig = new FileConfig(oldFile);
                for (String id : oldConfig.getSection("")) {
                    rarities.add(Rarity.read(plugin, oldConfig, id, id));
                }
                oldFile.delete();
            }
            if (rarities.isEmpty()) {
                rarities.add(new Rarity(this.plugin, "common", Tags.WHITE.wrap("Common"), 70));
                rarities.add(new Rarity(this.plugin, "rare", Tags.LIGHT_GREEN.wrap("Rare"), 25));
                rarities.add(new Rarity(this.plugin, "mythic", Tags.LIGHT_PURPLE.wrap("Mythic"), 5));
            }

            rarities.forEach(rarity -> {
                rarity.write(config, "Rewards.Rarities." + rarity.getId());
            });
        }

        config.getSection("Rewards.Rarities").forEach(rarityId -> {
            Rarity rarity = Rarity.read(this.plugin, config, "Rewards.Rarities." + rarityId, rarityId);
            this.rarityByIdMap.put(rarity.getId(), rarity);
        });

        this.plugin.info("Loaded " + this.rarityByIdMap.size() + " rarities!");
    }

    private void loadPreviews() {
        File dir = new File(plugin.getDataFolder().getAbsolutePath(), Config.DIR_PREVIEWS);
        if (!dir.exists() && dir.mkdirs()) {
            new PreviewMenu(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_PREVIEWS, Placeholders.DEFAULT + ".yml"));
        }

        for (FileConfig config : FileConfig.loadAll(plugin.getDataFolder() + Config.DIR_PREVIEWS, false)) {
            PreviewMenu menu = new PreviewMenu(plugin, config);
            String id = config.getFile().getName().replace(".yml", "").toLowerCase();
            this.previewByIdMap.put(id, menu);
        }
    }

    private void loadUI() {
        if (Config.isMilestonesEnabled()) {
            this.milestonesMenu = new MilestonesMenu(this.plugin);
        }
    }

    private void loadCrates() {
        for (File file : FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_CRATES, false)) {
            Crate crate = new Crate(plugin, file);
            this.loadCrate(crate);
        }
        this.plugin.info("Loaded " + this.crateByIdMap.size() + " crates.");
    }

    private void loadCrate(@NotNull Crate crate) {
        if (crate.load()) {
            this.crateByIdMap.put(crate.getId(), crate);
            this.addCratePositions(crate);
        }
        else this.plugin.error("Crate not loaded: '" + crate.getFile().getName() + "'.");
    }

    private void runInspections() {
        this.getCrates().forEach(crate -> {
            String filePath = crate.getFile().getPath();

            Inspectors.CRATE.printConsole(plugin, crate, "Problems in '" + crate.getId() + "' crate (" + filePath + "):");

            crate.getRewards().forEach(reward -> {
                Inspectors.REWARD.printConsole(plugin, reward, "Problems in '" + reward.getId() + "' reward (" + filePath + "):");
            });
        });
    }

    @NotNull
    public Map<String, Rarity> getRarityByIdMap() {
        return this.rarityByIdMap;
    }

    @NotNull
    public Set<Rarity> getRarities() {
        return new HashSet<>(this.rarityByIdMap.values());
    }

    @Nullable
    public Rarity getRarity(@NotNull String id) {
        return this.rarityByIdMap.get(id.toLowerCase());
    }

    @NotNull
    public Set<String> getRarityIds() {
        return new HashSet<>(this.rarityByIdMap.keySet());
    }

    @NotNull
    public Rarity getMostCommonRarity() {
        return this.getRarities().stream().max(Comparator.comparing(Rarity::getWeight)).orElseThrow();
    }

    @NotNull
    public Map<String, PreviewMenu> getPreviewByIdMap() {
        return this.previewByIdMap;
    }

    @Nullable
    public PreviewMenu getPreviewById(@NotNull String id) {
        return this.previewByIdMap.get(id.toLowerCase());
    }

    @NotNull
    public Set<PreviewMenu> getPreviews() {
        return new HashSet<>(this.previewByIdMap.values());
    }

    @NotNull
    public List<String> getPreviewNames() {
        return new ArrayList<>(this.previewByIdMap.keySet());
    }

    public void openMilestones(@NotNull Player player, @NotNull CrateSource source) {
        if (this.milestonesMenu != null) {
            this.milestonesMenu.open(player, source);
        }
    }



    @NotNull
    public List<String> getCrateIds() {
        return new ArrayList<>(this.crateByIdMap.keySet());
    }

    @NotNull
    public Map<String, Crate> getCratesMap() {
        return this.crateByIdMap;
    }

    @NotNull
    public Set<Crate> getCrates() {
        return new HashSet<>(this.crateByIdMap.values());
    }

    public boolean isCrate(@NotNull ItemStack item) {
        return this.getCrateByItem(item) != null;
    }

    @Nullable
    public Crate getCrateById(@NotNull String id) {
        return this.crateByIdMap.get(id.toLowerCase());
    }

    @Nullable
    public Crate getCrateByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getString(item, Keys.crateId).orElse(null);
        return id != null ? this.getCrateById(id) : null;
    }

    @Nullable
    public Crate getCrateByBlock(@NotNull Block block) {
        return this.getCrateByLocation(block.getLocation());
    }

    @Nullable
    public Crate getCrateByLocation(@NotNull Location location) {
        WorldPos pos = WorldPos.from(location);
        return this.crateByPosMap.get(pos);
    }

    public void removeCratePositions(@NotNull Crate crate) {
        crate.getBlockPositions().forEach(this.crateByPosMap::remove);
    }

    public void addCratePositions(@NotNull Crate crate) {
        crate.getBlockPositions().forEach(pos -> this.crateByPosMap.put(pos, crate));
    }

    public boolean create(@NotNull String id) {
        id = CrateUtils.createID(id);
        if (this.getCrateById(id) != null) return false;

        File file = new File(plugin.getDataFolder() + Config.DIR_CRATES, id + ".yml");
        FileUtil.create(file);

        String name = StringUtil.capitalizeUnderscored(id) + " Crate";
        ItemStack item = NightItem.asCustomHead(Placeholders.SKULL_CRATE)
            .setDisplayName(name)
            .setHideComponents(true)
            .getItemStack();

        Crate crate = new Crate(this.plugin, file);
        crate.setName(name);
        crate.setDescription(new ArrayList<>());
        crate.setItemProvider(ItemTypes.vanilla(item));
        crate.setAnimationEnabled(true);
        crate.setAnimationId(Placeholders.DEFAULT);
        crate.setPreviewEnabled(true);
        crate.setPreviewId(Placeholders.DEFAULT);
        crate.setPushbackEnabled(true);
        crate.setHologramEnabled(true);
        crate.setHologramTemplateId(Placeholders.DEFAULT);
        crate.setEffectType(EffectId.HELIX);
        crate.setEffectParticle(UniParticle.of(Particle.CLOUD));
        crate.save();

        this.loadCrate(crate);
        return true;
    }

    public boolean delete(@NotNull Crate crate) {
        if (!crate.getFile().delete()) return false;

        crate.removeHologram();

        this.plugin.getDataManager().handleCrateRemoval(crate);
        this.crateByIdMap.remove(crate.getId());
        this.crateByPosMap.values().removeIf(stored -> stored == crate);
        return true;
    }

    public boolean dropCrateItem(@NotNull Crate crate, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        world.dropItemNaturally(location, crate.getItem());
        return true;
    }

    public void giveCrateItem(@NotNull Player player, @NotNull Crate crate, int amount) {
        amount = Math.max(1, amount);

        ItemStack crateItem = crate.getItem();
        Players.addItem(player, crateItem, amount);
    }

    public void previewCrate(@NotNull Player player, @NotNull CrateSource source) {
        Crate crate = source.getCrate();
        if (!crate.isPreviewEnabled()) return;

        PreviewMenu menu = this.getPreviewById(crate.getPreviewId());
        if (menu == null) return;

        menu.open(player, source);
    }

    public void interactCrate(@NotNull Player player, @NotNull Crate crate, @NotNull InteractType action, @Nullable ItemStack item, @Nullable Block block) {
        //player.closeInventory();

        CrateSource source = new CrateSource(crate, item, block);

        if (action == InteractType.CRATE_PREVIEW) {
            this.previewCrate(player, source);
            return;
        }

        if (action == InteractType.CRATE_OPEN || action == InteractType.CRATE_MASS_OPEN) {
            OpenSettings settings = new OpenSettings().setSkipAnimation(action == InteractType.CRATE_MASS_OPEN);

            int keys = plugin.getKeyManager().getKeysAmount(player, crate);
            int openings = action == InteractType.CRATE_MASS_OPEN && crate.isKeyRequired() ? Math.max(1, keys) : 1;
            int massLimit = Math.max(1, Config.CRATE_MASS_OPENING_LIMIT.get());
            if (openings > massLimit) {
                openings = massLimit;
            }

            for (int spent = 0; spent < openings; spent++) {
                if (!this.openCrate(player, source, settings)) {
                    if (spent == 0 && block != null && crate.isPushbackEnabled()) {
                        player.setVelocity(player.getEyeLocation().getDirection().setY(Config.CRATE_PUSHBACK_Y.get()).multiply(Config.CRATE_PUSHBACK_MULTIPLY.get()));
                    }
                    break;
                }
            }
        }
    }

    public boolean openCrate(@NotNull Player player, @NotNull CrateSource source, @NotNull OpenSettings settings) {
        // Wait until crate datas and reward limits are loaded.
        if (!this.plugin.getDataManager().isDataLoaded()) {
            return false;
        }

        // Check if player is in other opening or if crate block is occupied by others.
        if (!this.plugin.getOpeningManager().isOpeningAvailable(player, source)) {
            Lang.CRATE_OPEN_ERROR_ALREADY.getMessage().send(player);
            return false;
        }

        // Stop mass open (mostly only this case) if crate itemstack is out.
        if (source.getItem() != null && source.getItem().getAmount() <= 0) {
            return false;
        }

        Crate crate = source.getCrate();
        if (!settings.isForce() && !crate.hasPermission(player)) {
            Lang.ERROR_NO_PERMISSION.getMessage(plugin).send(player);
            return false;
        }

        if (!settings.isForce() && player.getInventory().firstEmpty() == -1) {
            Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE.getMessage().send(player, replacer -> replacer.replace(crate.replacePlaceholders()));
            return false;
        }

        CrateKey key = null;
        CrateUser user = plugin.getUserManager().getOrFetch(player);
        UserCrateData crateData = user.getCrateData(crate);

        if (!settings.isForce() && crate.hasOpenCooldown() && crateData.hasCooldown()) {
            (crateData.isCooldownPermanent() ? Lang.CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED : Lang.CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY).getMessage().send(player, replacer -> replacer
                .replace(Placeholders.GENERIC_TIME, TimeFormats.formatDuration(crateData.getOpenCooldown(), TimeFormatType.LITERAL))
                .replace(crate.replacePlaceholders())
            );
            return false;
        }

        if (!settings.isForce() && crate.isKeyRequired()) {
            key = this.plugin.getKeyManager().getOpenKey(player, crate);
            if (key == null) {
                boolean holdRequired = Config.isKeyHoldRequired() && !crate.isAllVirtualKeys();
                (holdRequired ? Lang.CRATE_OPEN_ERROR_NO_HOLD_KEY : Lang.CRATE_OPEN_ERROR_NO_KEY).getMessage().send(player, replacer -> replacer.replace(crate.replacePlaceholders()));
                return false;
            }
        }

        if (!settings.isForce() && !crate.canAffordOpen(player)) {
            Lang.CRATE_OPEN_ERROR_TOO_EXPENSIVE.getMessage().send(player, replacer -> replacer.replace(crate.replacePlaceholders()));
            return false;
        }

        if (crate.getRewards(player).isEmpty()) {
            Lang.CRATE_OPEN_ERROR_NO_REWARDS.getMessage().send(player, replacer -> replacer.replace(crate.replacePlaceholders()));
            return false;
        }

        CrateOpenEvent openEvent = new CrateOpenEvent(crate, player);
        plugin.getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled()) return false;

        Opening opening = this.plugin.getOpeningManager().createOpening(player, source, key);
        opening.setRefundable(!settings.isForce());

        this.plugin.getOpeningManager().startOpening(player, opening, settings.isSkipAnimation());

        if (!settings.isForce()) {
            // Take costs
            crate.payForOpen(player);

            // Take key
            if (crate.isKeyRequired() && key != null) {
                this.plugin.getKeyManager().takeKey(player, key, 1);
            }

            // Take crate item stack
            ItemStack item = source.getItem();
            if (item != null) {
                item.setAmount(item.getAmount() - 1);
            }
        }

        return true;
    }

    public boolean triggerMilestones(@NotNull Player player, @NotNull Crate crate, int progress) {
        if (!crate.hasMilestones()) return false;

        int maxProgress = crate.getMaxMilestone();
        if (!crate.isMilestonesRepeatable() && progress > maxProgress) return false;

        Milestone milestone = crate.getMilestone(progress);
        if (milestone == null) return false;

        Reward reward = milestone.getReward();
        if (reward == null) return false;

        reward.giveContent(player);

        Lang.CRATE_OPEN_MILESTONE_COMPLETED.getMessage().send(player, replacer -> replacer
            .replace(crate.replacePlaceholders())
            .replace(Placeholders.MILESTONE_OPENINGS, NumberUtil.format(progress))
            .replace(reward.replacePlaceholders())
        );

        return true;
    }

    public void giveReward(@NotNull Player player, @NotNull Reward reward) {
        reward.giveContent(player);

        Crate crate = reward.getCrate();
        GlobalCrateData globalData = this.plugin.getDataManager().getCrateDataOrCreate(crate);

        globalData.setLatestReward(reward);
        globalData.setSaveRequired(true);

        Lang.CRATE_OPEN_REWARD_INFO.getMessage().send(player, replacer -> replacer
            .replace(crate.replacePlaceholders())
            .replace(reward.replacePlaceholders())
        );

        if (reward.isBroadcast()) {
            Lang.CRATE_OPEN_REWARD_BROADCAST.getMessage().broadcast(replacer -> replacer
                .replace(Placeholders.forPlayerWithPAPI(player))
                .replace(crate.replacePlaceholders())
                .replace(reward.replacePlaceholders())
            );
        }

        this.addRollCount(player, reward);
        this.plugin.getCrateLogger().logReward(player, reward);
    }

    public int getGlobalRollsLeft(@NotNull Reward reward) {
        if (!reward.hasGlobalLimit()) return -1;

        RewardLimit limit = this.plugin.getDataManager().getRewardLimit(reward, null);
        if (limit == null) return reward.getGlobalLimits().getAmount();

        limit.resetIfReady();

        return Math.max(0, reward.getGlobalLimits().getAmount() - limit.getAmount());
    }

    public int getPersonalRollsLeft(@NotNull Reward reward, @NotNull Player player) {
        if (!reward.hasPersonalLimit()) return -1;

        RewardLimit limit = this.plugin.getDataManager().getRewardLimit(reward, player);
        if (limit == null) return reward.getPlayerLimits().getAmount();

        limit.resetIfReady();

        return Math.max(0, reward.getPlayerLimits().getAmount() - limit.getAmount());
    }

    public int getAvailableRolls(@NotNull Player player, @NotNull Reward reward) {
        int globalLeft = this.getGlobalRollsLeft(reward);
        int playerLeft = this.getPersonalRollsLeft(reward, player);

        if (globalLeft < 0 || playerLeft < 0) {
            return Math.max(playerLeft, globalLeft);
        }

        return Math.min(playerLeft, globalLeft);
    }

    public void addRollCount(@NotNull Player player, @NotNull Reward reward) {
        if (player.hasPermission(Perms.BYPASS_REWARD_LIMIT)) return;

        if (reward.hasGlobalLimit()) {
            RewardLimit limit = this.plugin.getDataManager().getRewardLimitOrCreate(reward, null);
            limit.resetIfReady();

            limit.addRoll(1);
            limit.updateResetTime(reward.getGlobalLimits());
            limit.setSaveRequired(true);
        }

        if (reward.hasPersonalLimit()) {
            RewardLimit limit = this.plugin.getDataManager().getRewardLimitOrCreate(reward, player);
            limit.resetIfReady();

            limit.addRoll(1);
            limit.updateResetTime(reward.getPlayerLimits());
            limit.setSaveRequired(true);
        }
    }

    public void setPreviewCooldown(@NotNull Player player) {
        long timestamp = System.currentTimeMillis() + Config.CRATE_PREVIEW_COOLDOWN.get();
        this.previewCooldown.put(player.getUniqueId(), timestamp);
    }

    public long getPreviewCooldown(@NotNull Player player) {
        long timestamp = this.previewCooldown.getOrDefault(player.getUniqueId(), 0L);
        if (System.currentTimeMillis() < timestamp) {
            return timestamp;
        }

        this.removePreviewCooldown(player);
        return 0L;
    }

    public boolean hasPreviewCooldown(@NotNull Player player) {
        return this.getPreviewCooldown(player) > 0L;
    }

    public void removePreviewCooldown(@NotNull Player player) {
        this.previewCooldown.remove(player.getUniqueId());
    }

    public void playCrateEffects() {
        this.getCrates().forEach(crate -> {
            CrateEffect effect = crate.getEffect();
            if (effect.isDummy()) return;

            UniParticle particle = crate.getEffectParticle();

            crate.getBlockPositions().forEach(worldPos -> {
                if (!worldPos.isChunkLoaded()) return;

                Location location = worldPos.toLocation();
                if (location == null) return;

                CrateUtils.getPlayersForEffects(location).forEach(player -> {
                    effect.playStep(location, particle, player);
                });
            });
        });

        EffectRegistry.getEffects().forEach(CrateEffect::addTickCount);
    }
}
