package su.nightexpress.excellentcrates.crate;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.editor.*;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectModel;
import su.nightexpress.excellentcrates.crate.impl.*;
import su.nightexpress.excellentcrates.crate.listener.CrateListener;
import su.nightexpress.excellentcrates.crate.menu.CratesEditorMenu;
import su.nightexpress.excellentcrates.crate.menu.MilestonesMenu;
import su.nightexpress.excellentcrates.crate.menu.PreviewMenu;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.hologram.HologramType;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.impl.BasicOpening;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.text.tag.Tags;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.io.File;
import java.util.*;

public class CrateManager extends AbstractManager<CratesPlugin> {

    private final Map<String, Rarity>      rarityMap;
    private final Map<String, Crate>       crateMap;
    private final Map<String, PreviewMenu> previewMap;

    private CratesEditorMenu editorMenu;
    private MilestonesMenu   milestonesMenu;

    private CrateListEditor       cratesEditor;
    private CrateMainEditor       crateSettingsEditor;
    private CrateParticleEditor   crateParticleEditor;
    private CrateMilestonesEditor milestonesEditor;
    private CratePlacementEditor  placementEditor;
    private RewardListEditor      rewardsEditor;
    private RewardMainEditor      rewardSettingsEditor;
    private RewardSortEditor      rewardSortEditor;

    public CrateManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.rarityMap = new HashMap<>();
        this.crateMap = new HashMap<>();
        this.previewMap = new HashMap<>();
    }

    @Override
    public void onLoad() {
        this.loadRarities();
        if (this.rarityMap.isEmpty()) {
            this.plugin.error("No rarities are available! You must have at least 1 rarity for the plugin to work.");
            this.plugin.getPluginManager().disablePlugin(this.plugin);
            return;
        }

        this.loadPreviews();
        this.loadCrates();



        this.editorMenu = new CratesEditorMenu(this.plugin);
        this.cratesEditor = new CrateListEditor(this.plugin, this);
        this.crateSettingsEditor = new CrateMainEditor(this.plugin);
        this.crateParticleEditor = new CrateParticleEditor(this.plugin);
        this.milestonesEditor = new CrateMilestonesEditor(this.plugin);
        this.placementEditor = new CratePlacementEditor(this.plugin);
        this.rewardsEditor = new RewardListEditor(this.plugin);
        this.rewardSettingsEditor = new RewardMainEditor(this.plugin);
        this.rewardSortEditor = new RewardSortEditor(this.plugin);

        this.milestonesMenu = new MilestonesMenu(this.plugin);

        if (this.plugin.getHologramHandler() != null && Config.CRATE_HOLOGRAM_HANDLER.get() == HologramType.INTERNAL) {
            this.addTask(plugin.createAsyncTask(this::updateCrateHolograms).setSecondsInterval(Config.CRATE_HOLOGRAM_UPDATE_INTERVAL.get()));
        }

        this.addTask(plugin.createAsyncTask(this::playCrateEffects).setTicksInterval(1L));
        this.addTask(plugin.createTask(() -> BasicOpening.tickVisuals(this.plugin)).setSecondsInterval(1));

        this.addListener(new CrateListener(this.plugin, this));
    }

    private void loadRarities() {
        FileConfig config = this.plugin.getConfig();
        
        if (!config.contains("Rewards.Rarities")) {
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
                rarities.add(new Rarity(this.plugin, "common", Tags.WHITE.enclose("Common"), 10, true));
                rarities.add(new Rarity(this.plugin, "uncommon", Tags.LIGHT_BLUE.enclose("Uncommon"), 5, false));
                rarities.add(new Rarity(this.plugin, "rare", Tags.LIGHT_GREEN.enclose("Rare"), 2, false));
                rarities.add(new Rarity(this.plugin, "legendary", Tags.LIGHT_ORANGE.enclose("Legendary"), 1, false));
            }

            rarities.forEach(rarity -> {
                rarity.write(config, "Rewards.Rarities." + rarity.getId());
            });
        }

        config.getSection("Rewards.Rarities").forEach(rarityId -> {
            Rarity rarity = Rarity.read(this.plugin, config, "Rewards.Rarities." + rarityId, rarityId);
            this.rarityMap.put(rarity.getId(), rarity);
        });

        this.plugin.info("Loaded " + this.rarityMap.size() + " rarities!");
    }

    private void loadPreviews() {
        for (FileConfig cfg : FileConfig.loadAll(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)) {
            PreviewMenu menu = new PreviewMenu(plugin, cfg);
            String id = cfg.getFile().getName().replace(".yml", "").toLowerCase();
            this.previewMap.put(id, menu);
        }
    }

    private void loadCrates() {
        for (File file : FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_CRATES, false)) {
            Crate crate = new Crate(plugin, file);
            this.loadCrate(crate);
        }
        this.plugin.info("Loaded " + this.crateMap.size() + " crates.");

        // Load locations when all worlds loaded.
        this.plugin.runTask(task -> this.getCrates().forEach(Crate::loadLocations));
        this.plugin.runTaskAsync(task -> this.getCrates().forEach(Crate::loadRewardWinDatas));
    }

    private void loadCrate(@NotNull Crate crate) {
        if (crate.load()) {
            this.crateMap.put(crate.getId(), crate);
        }
        else this.plugin.error("Crate not loaded: '" + crate.getFile().getName() + "'.");
    }

    @Override
    protected void onShutdown() {
        BasicOpening.clearVisuals(this.plugin);
        Arrays.asList(EffectModel.values()).forEach(model -> model.getEffect().reset());

        if (this.editorMenu != null) this.editorMenu.clear();
        if (this.cratesEditor != null) this.cratesEditor.clear();
        if (this.crateSettingsEditor != null) this.crateSettingsEditor.clear();
        if (this.crateParticleEditor != null) this.crateParticleEditor.clear();
        if (this.milestonesEditor != null) this.milestonesEditor.clear();
        if (this.placementEditor != null) this.placementEditor.clear();
        if (this.rewardsEditor != null) this.rewardsEditor.clear();
        if (this.rewardSettingsEditor != null) this.rewardSettingsEditor.clear();
        if (this.rewardSortEditor != null) this.rewardSortEditor.clear();

        if (this.milestonesMenu != null) this.milestonesMenu.clear();

        this.previewMap.values().forEach(ConfigMenu::clear);
        this.previewMap.clear();

        this.crateMap.values().forEach(crate -> {
            crate.saveLastOpenData();
            crate.clear();
        });
        this.crateMap.clear();

        this.rarityMap.clear();
    }

    /*@NotNull
    public CratesEditorMenu getEditorMenu() {
        return editorMenu;
    }

    @NotNull
    public CrateListEditor getCratesEditor() {
        return cratesEditor;
    }

    @NotNull
    public CrateMainEditor getCrateSettingsEditor() {
        return crateSettingsEditor;
    }

    @NotNull
    public CrateParticleEditor getCrateParticleEditor() {
        return crateParticleEditor;
    }

    @NotNull
    public CrateMilestonesEditor getMilestonesEditor() {
        return milestonesEditor;
    }

    @NotNull
    public CratePlacementEditor getPlacementEditor() {
        return placementEditor;
    }

    @NotNull
    public RewardListEditor getRewardsEditor() {
        return rewardsEditor;
    }

    @NotNull
    public RewardMainEditor getRewardSettingsEditor() {
        return rewardSettingsEditor;
    }*/

    @NotNull
    public MilestonesMenu getMilestonesMenu() {
        return milestonesMenu;
    }

    @NotNull
    public Map<String, Rarity> getRarityMap() {
        return rarityMap;
    }

    @NotNull
    public Collection<Rarity> getRarities() {
        return this.getRarityMap().values();
    }

    @Nullable
    public Rarity getRarity(@NotNull String id) {
        return this.getRarityMap().get(id.toLowerCase());
    }

    @NotNull
    public Rarity getDefaultRarity() {
        return this.getRarities().stream().filter(Rarity::isDefault).findFirst().orElse(this.getRarities().stream().findAny().orElseThrow());
    }

    @NotNull
    public Map<String, PreviewMenu> getPreviewMap() {
        return Collections.unmodifiableMap(this.previewMap);
    }

    @Nullable
    public PreviewMenu getPreview(@NotNull Crate crate) {
        String config = crate.getPreviewConfig();
        return config == null ? null : this.getPreview(config);
    }

    @Nullable
    public PreviewMenu getPreview(@NotNull String id) {
        return this.previewMap.get(id.toLowerCase());
    }

    @NotNull
    public Collection<PreviewMenu> getPreviews() {
        return this.getPreviewMap().values();
    }

    @NotNull
    public List<String> getPreviewNames() {
        return new ArrayList<>(this.previewMap.keySet());
    }


    public void openEditor(@NotNull Player player) {
        this.editorMenu.open(player, this.plugin);
    }

    public void openCratesEditor(@NotNull Player player) {
        this.cratesEditor.open(player, this);
    }

    public void openCrateEditor(@NotNull Player player, @NotNull Crate crate) {
        this.crateSettingsEditor.open(player, crate);
    }

    public void openCrateParticleEditor(@NotNull Player player, @NotNull Crate crate) {
        this.crateParticleEditor.open(player, crate);
    }

    public void openMilestonesEditor(@NotNull Player player, @NotNull Crate crate) {
        this.milestonesEditor.open(player, crate);
    }

    public void openPlacementEditor(@NotNull Player player, @NotNull Crate crate) {
        this.placementEditor.open(player, crate);
    }

    public void openRewardsEditor(@NotNull Player player, @NotNull Crate crate) {
        this.rewardsEditor.open(player, crate);
    }

    public void openRewardSortEditor(@NotNull Player player, @NotNull Crate crate) {
        this.rewardSortEditor.open(player, crate);
    }

    public void openRewardEditor(@NotNull Player player, @NotNull Reward reward) {
        this.rewardSettingsEditor.open(player, reward);
    }


    public boolean isCrate(@NotNull ItemStack item) {
        return this.getCrateByItem(item) != null;
    }

    @NotNull
    public List<String> getCrateIds(boolean keyOnly) {
        return this.getCrates().stream().filter(crate -> crate.isKeyRequired() || !keyOnly).map(Crate::getId).toList();
    }

    @NotNull
    public Map<String, Crate> getCratesMap() {
        return Collections.unmodifiableMap(this.crateMap);
    }

    @NotNull
    public Collection<Crate> getCrates() {
        return this.getCratesMap().values();
    }

    @Nullable
    public Crate getCrateById(@NotNull String id) {
        return this.crateMap.get(id.toLowerCase());
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
        return this.getCrates().stream().filter(crate -> crate.getBlockLocations().contains(location)).findFirst().orElse(null);
    }

    public void updateCrateHolograms() {
        this.getCrates().forEach(Crate::updateHologram);
    }

    public boolean create(@NotNull String id) {
        id = StringUtil.lowerCaseUnderscore(id);
        if (this.getCrateById(id) != null) {
            return false;
        }

        File file = new File(plugin.getDataFolder() + Config.DIR_CRATES, id + ".yml");
        FileUtil.create(file);

        Crate crate = new Crate(this.plugin, file);
        crate.setName(StringUtil.capitalizeUnderscored(crate.getId()) + " Crate");
        crate.setOpeningConfig(null);
        crate.setPreviewConfig(Placeholders.DEFAULT);

        ItemStack item = ItemUtil.getSkinHead(Placeholders.SKIN_NEW_CRATE);
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(crate.getName());
            meta.addItemFlags(ItemFlag.values());
        });
        crate.setItem(item);

        crate.setPushbackEnabled(true);
        crate.setHologramEnabled(true);
        crate.setHologramTemplate(Placeholders.DEFAULT);
        crate.setEffectModel(EffectModel.HELIX);
        crate.setEffectParticle(UniParticle.redstone(Color.fromRGB(Rnd.get(256), Rnd.get(256), Rnd.get(256)), 1f));
        crate.save();

        this.loadCrate(crate);
        return true;
    }

    public boolean delete(@NotNull Crate crate) {
        if (crate.getFile().delete()) {
            crate.clear();
            crate.deleteRewardWinDatas();
            this.crateMap.remove(crate.getId());
            return true;
        }
        return false;
    }

    public boolean spawnCrate(@NotNull Crate crate, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        world.dropItemNaturally(location, crate.getItem());
        return true;
    }

    public void giveCrate(@NotNull Player player, @NotNull Crate crate, int amount) {
        if (amount < 1) return;

        ItemStack crateItem = crate.getItem();
        crateItem.setAmount(Math.min(64, amount));
        Players.addItem(player, crateItem);
    }

    public void previewCrate(@NotNull Player player, @NotNull CrateSource source) {
        Crate crate = source.getCrate();
        PreviewMenu menu = this.getPreview(crate);
        if (menu == null) return;

        menu.open(player, source);
    }

    public void interactCrate(@NotNull Player player, @NotNull Crate crate, @NotNull InteractType action, @Nullable ItemStack item, @Nullable Block block) {
        player.closeInventory();

        CrateSource source = new CrateSource(crate, item, block);

        if (action == InteractType.CRATE_PREVIEW) {
            this.previewCrate(player, source);
            return;
        }

        if (action == InteractType.CRATE_OPEN || action == InteractType.CRATE_MASS_OPEN) {
            OpenSettings settings = new OpenSettings().setSkipAnimation(action == InteractType.CRATE_MASS_OPEN).setSaveData(false);

            int keys = plugin.getKeyManager().getKeysAmount(player, crate);
            int openings = action == InteractType.CRATE_MASS_OPEN && crate.isKeyRequired() ? Math.max(1, keys) : 1;
            int massLimit = Math.max(1, Config.CRATE_MASS_OPENING_LIMIT.get());
            if (openings > massLimit) {
                openings = massLimit;
            }

            for (int spent = 0; spent < openings; spent++) {
                // Save user & reward data for the latest iteration only.
                if (openings == 1 || spent == (openings - 1)) {
                    settings.setSaveData(true);
                }

                if (!this.openCrate(player, source, settings)) {
                    if (spent == 0) {
                        if (block != null && crate.isPushbackEnabled()) {
                            player.setVelocity(player.getEyeLocation().getDirection().setY(Config.CRATE_PUSHBACK_Y.get()).multiply(Config.CRATE_PUSHBACK_MULTIPLY.get()));
                        }
                        return;
                    }
                    break;
                }
            }
        }
    }

    public boolean openCrate(@NotNull Player player, @NotNull CrateSource source, @NotNull OpenSettings settings) {
        Opening openingData = this.plugin.getOpeningManager().getOpeningData(player);
        if (openingData != null && !openingData.isCompleted()) {
            return false;
        }

        Crate crate = source.getCrate();

        // Stop mass open (mostly only this case) if crate itemstack is out.
        if (source.getItem() != null && source.getItem().getAmount() <= 0) {
            return false;
        }

        if (!settings.isForce() && !crate.hasPermission(player)) {
            Lang.ERROR_NO_PERMISSION.getMessage(plugin).send(player);
            return false;
        }

        if (!settings.isForce() && player.getInventory().firstEmpty() == -1) {
            Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE.getMessage().replace(crate.replacePlaceholders()).send(player);
            return false;
        }

        CrateUser user = plugin.getUserManager().getUserData(player);
        if (!settings.isForce() && user.isCrateOnCooldown(crate)) {
            long expireDate = user.getCrateCooldown(crate);
            (expireDate < 0 ? Lang.CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED : Lang.CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY).getMessage()
                .replace(Placeholders.GENERIC_TIME, TimeUtil.formatDuration(expireDate))
                .replace(crate.replacePlaceholders())
                .send(player);
            return false;
        }

        if (!settings.isForce() && crate.isKeyRequired()) {
            if (!this.plugin.getKeyManager().hasKey(player, crate)) {
                Lang.CRATE_OPEN_ERROR_NO_KEY.getMessage().replace(crate.replacePlaceholders()).send(player);
                return false;
            }
            if (Config.CRATE_HOLD_KEY_TO_OPEN.get() && crate.isAllPhysicalKeys()) {
                ItemStack main = player.getInventory().getItemInMainHand();
                if (!this.plugin.getKeyManager().isKey(main, crate)) {
                    Lang.CRATE_OPEN_ERROR_NO_HOLD_KEY.getMessage().replace(crate.replacePlaceholders()).send(player);
                    return false;
                }
            }
        }

        if (!settings.isForce() && !crate.hasCostBypassPermisssion(player)) {
            for (var entry : crate.getOpenCostMap().entrySet()) {
                Currency currency = entry.getKey();
                double amount = entry.getValue();
                if (currency.getHandler().getBalance(player) < amount) {
                    Lang.CRATE_OPEN_ERROR_CANT_AFFORD.getMessage()
                        .replace(Placeholders.GENERIC_AMOUNT, currency.format(amount))
                        .replace(crate.replacePlaceholders()).send(player);
                    return false;
                }
            }
        }

        if (crate.getRewards(player).isEmpty()) {
            Lang.CRATE_OPEN_ERROR_NO_REWARDS.getMessage().replace(crate.replacePlaceholders()).send(player);
            return false;
        }

        CrateOpenEvent openEvent = new CrateOpenEvent(crate, player);
        plugin.getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled()) return false;

        CrateKey key = null;
        if (!settings.isForce()) {
            // Take costs
            crate.getOpenCostMap().forEach((currency, amount) -> currency.getHandler().take(player, amount));

            // Take key
            if (crate.isKeyRequired()) {
                key = this.plugin.getKeyManager().takeKey(player, crate);
            }

            // Take crate item stack
            ItemStack item = source.getItem();
            if (item != null) {
                item.setAmount(item.getAmount() - 1);
            }
        }

        Opening opening = this.plugin.getOpeningManager().createOpening(player, source, key);
        opening.setRefundable(!settings.isForce());
        opening.setSaveData(settings.isSaveData());

        if (!this.plugin.getOpeningManager().startOpening(player, opening, settings.isSkipAnimation())) {
            this.plugin.getOpeningManager().stopOpening(player);
            return false;
        }
        return true;
    }

    public void addOpenings(@NotNull Player player, @NotNull Crate crate, int amount) {
        if (amount == 0) return;

        CrateUser user = plugin.getUserManager().getUserData(player);
        int has = user.getOpeningsAmount(crate) ;
        user.setOpeningsAmount(crate, has + amount);
    }

    public void proceedMilestones(@NotNull Player player, @NotNull Crate crate) {
        if (crate.getMilestones().isEmpty()) return;

        CrateUser user = plugin.getUserManager().getUserData(player);

        int milestonesMax = crate.getMaxMilestone();
        int milestones = user.getMilestones(crate) + 1;

        if (crate.isMilestonesRepeatable() || milestones <= milestonesMax) {
            Milestone milestone = crate.getMilestone(milestones);
            Reward reward = milestone == null ? null : milestone.getReward();
            if (reward != null) {
                reward.giveContent(player);
                Lang.CRATE_OPEN_MILESTONE_COMPLETED.getMessage()
                    .replace(crate.replacePlaceholders())
                    .replace(Placeholders.MILESTONE_OPENINGS, NumberUtil.format(milestones))
                    .replace(reward.replacePlaceholders())
                    .send(player);
            }

            if (milestones >= milestonesMax && crate.isMilestonesRepeatable()) {
                milestones = 0;
            }
            user.setMilestones(crate, milestones);
        }
    }

    public void setCrateCooldown(@NotNull Player player, @NotNull Crate crate) {
        if (player.hasPermission(Perms.BYPASS_CRATE_COOLDOWN) || crate.getOpenCooldown() == 0) return;

        long cooldown = crate.getOpenCooldown();
        long endDate = cooldown < 0 ? -1L : System.currentTimeMillis() + cooldown * 1000L;

        CrateUser user = plugin.getUserManager().getUserData(player);
        user.setCrateCooldown(crate, endDate);
    }

    public void playCrateEffects() {
        this.getCrates().forEach(crate -> {
            if (crate.getEffectModel() == EffectModel.NONE) return;

            UniParticle particle = crate.getEffectParticle();
            AbstractEffect effect = crate.getEffectModel().getEffect();

            new HashSet<>(crate.getBlockLocations()).forEach(location -> {
                World world = location.getWorld();
                int chunkX = location.getBlockX() >> 4;
                int chunkZ = location.getBlockZ() >> 4;
                if (world == null || !world.isChunkLoaded(chunkX, chunkZ)) return;

                Location center = LocationUtil.getCenter(location.clone());
                effect.step(center, particle);
            });
        });

        Arrays.asList(EffectModel.values()).forEach(model -> model.getEffect().addStep());
    }
}
