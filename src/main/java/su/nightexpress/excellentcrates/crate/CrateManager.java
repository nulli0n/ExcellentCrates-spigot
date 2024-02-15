package su.nightexpress.excellentcrates.crate;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.Menu;
import su.nexmedia.engine.utils.*;
import su.nexmedia.engine.utils.random.Rnd;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.api.event.CrateObtainRewardEvent;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.crate.impl.*;
import su.nightexpress.excellentcrates.crate.listener.CrateListener;
import su.nightexpress.excellentcrates.crate.menu.CrateSource;
import su.nightexpress.excellentcrates.crate.menu.MilestonesMenu;
import su.nightexpress.excellentcrates.crate.menu.PreviewMenu;
import su.nightexpress.excellentcrates.crate.task.CrateEffectTask;
import su.nightexpress.excellentcrates.crate.task.HologramUpdateTask;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.hologram.HologramType;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;
import su.nightexpress.excellentcrates.opening.menu.OpeningMenu;
import su.nightexpress.excellentcrates.util.InteractType;

import java.util.*;

public class CrateManager extends AbstractManager<ExcellentCratesPlugin> {

    private final Map<String, Rarity>      rarityMap;
    private final Map<String, Crate>       crateMap;
    private final Map<String, PreviewMenu> previews;
    private final Map<String, OpeningMenu> openings;

    private MilestonesMenu milestonesMenu;
    private CrateEffectTask effectTask;
    private HologramUpdateTask hologramUpdateTask;

    public CrateManager(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin);
        this.rarityMap = new HashMap<>();
        this.crateMap = new HashMap<>();
        this.previews = new HashMap<>();
        this.openings = new HashMap<>();
    }

    @Override
    public void onLoad() {
        this.plugin.getConfigManager().extractResources(Config.DIR_CRATES);
        this.plugin.getConfigManager().extractResources(Config.DIR_PREVIEWS);
        this.plugin.getConfigManager().extractResources(Config.DIR_OPENINGS);

        this.milestonesMenu = new MilestonesMenu(this.plugin);

        JYML rarityConfig = JYML.loadOrExtract(plugin, Config.FILE_RARITY);
        for (String rarId : rarityConfig.getSection("")) {
            Rarity rarity = Rarity.read(rarityConfig, rarId, rarId);
            this.rarityMap.put(rarity.getId(), rarity);
        }
        this.plugin.info("Loaded " + this.getRarityMap().size() + " rarities!");

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_OPENINGS, true)) {
            OpeningMenu opening = new OpeningMenu(plugin, cfg);
            String id = cfg.getFile().getName().replace(".yml", "").toLowerCase();
            this.openings.put(id, opening);
        }

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)) {
            PreviewMenu menu = new PreviewMenu(plugin, cfg);
            String id = cfg.getFile().getName().replace(".yml", "").toLowerCase();
            this.previews.put(id, menu);
        }

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_CRATES, true)) {
            Crate crate = new Crate(plugin, cfg);
            if (crate.load()) {
                this.crateMap.put(crate.getId(), crate);
            }
            else this.plugin.error("Crate not loaded: '" + cfg.getFile().getName() + "'.");
        }
        this.plugin.info("Loaded " + this.getCratesMap().size() + " crates.");

        this.plugin.runTaskLater(task -> {
            this.getCrates().forEach(Crate::loadLocations);
        }, 60L);

        this.effectTask = new CrateEffectTask(this.plugin);
        this.effectTask.start();

        if (this.plugin.getHologramHandler() != null && Config.CRATE_HOLOGRAM_HANDLER.get() == HologramType.INTERNAL) {
            this.hologramUpdateTask = new HologramUpdateTask(this.plugin);
            this.hologramUpdateTask.start();
        }

        this.addListener(new CrateListener(this));
    }

    @Override
    public void onShutdown() {
        PlayerOpeningData.PLAYERS.values().forEach(data -> data.stop(true));
        PlayerOpeningData.PLAYERS.clear();

        if (this.effectTask != null) {
            this.effectTask.stop();
            this.effectTask = null;
            Arrays.asList(CrateEffectModel.values()).forEach(model -> model.getEffect().reset());
        }
        if (this.hologramUpdateTask != null) {
            this.hologramUpdateTask.stop();
        }

        if (this.milestonesMenu != null) this.milestonesMenu.clear();
        this.openings.values().forEach(Menu::clear);
        this.openings.clear();
        this.previews.values().forEach(ConfigMenu::clear);
        this.previews.clear();
        this.crateMap.values().forEach(Crate::clear);
        this.crateMap.clear();
        this.rarityMap.clear();
    }

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
    public Rarity getMostCommonRarity() {
        return this.getRarities().stream().max(Comparator.comparingDouble(Rarity::getChance)).orElseThrow();
    }

    @Nullable
    public OpeningMenu getOpening(@NotNull Crate crate) {
        String config = crate.getOpeningConfig();
        return config == null ? null : this.getOpening(config);
    }

    @Nullable
    public OpeningMenu getOpening(@NotNull String id) {
        return this.getOpeningsMap().get(id.toLowerCase());
    }

    @NotNull
    public Collection<OpeningMenu> getOpenings() {
        return this.getOpeningsMap().values();
    }

    @NotNull
    public Map<String, OpeningMenu> getOpeningsMap() {
        return openings;
    }

    @Nullable
    public PreviewMenu getPreview(@NotNull Crate crate) {
        String config = crate.getPreviewConfig();
        return config == null ? null : this.getPreview(config);
    }

    @Nullable
    public PreviewMenu getPreview(@NotNull String id) {
        return this.getPreviewsMap().get(id.toLowerCase());
    }

    @NotNull
    public Collection<PreviewMenu> getPreviews() {
        return this.getPreviewsMap().values();
    }

    @NotNull
    public Map<String, PreviewMenu> getPreviewsMap() {
        return previews;
    }

    public boolean create(@NotNull String id) {
        id = StringUtil.lowerCaseUnderscore(id);
        if (this.getCrateById(id) != null) {
            return false;
        }

        JYML cfg = new JYML(this.plugin.getDataFolder() + Config.DIR_CRATES, id + ".yml");
        Crate crate = new Crate(this.plugin, cfg);
        crate.setName(Colors2.GREEN + StringUtil.capitalizeUnderscored(crate.getId()) + " Crate");
        crate.setOpeningConfig(null);
        crate.setPreviewConfig(Placeholders.DEFAULT);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZkN2ZkYjUwZjE0YzczMWM3MjdiMGUwZDE4OWI2YTg3NDMxOWZjMGQ3OWM4YTA5OWFjZmM3N2M3YjJkOTE5NiJ9fX0=");
        ItemUtil.mapMeta(item, meta -> {
            meta.setDisplayName(crate.getName());
            meta.addItemFlags(ItemFlag.values());
        });
        crate.setItem(item);

        crate.setPushbackEnabled(true);
        crate.setHologramEnabled(true);
        crate.setHologramTemplate(Placeholders.DEFAULT);
        crate.setEffectModel(CrateEffectModel.HELIX);
        crate.setEffectParticle(UniParticle.redstone(Color.fromRGB(Rnd.get(256), Rnd.get(256), Rnd.get(256)), 1f));
        crate.save();
        crate.load();

        this.getCratesMap().put(crate.getId(), crate);
        return true;
    }

    public boolean delete(@NotNull Crate crate) {
        if (crate.getFile().delete()) {
            crate.clear();
            this.getCratesMap().remove(crate.getId());
            return true;
        }
        return false;
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
        return this.crateMap;
    }

    @NotNull
    public Collection<Crate> getCrates() {
        return this.getCratesMap().values();
    }

    @Nullable
    public Crate getCrateById(@NotNull String id) {
        return this.getCratesMap().get(id.toLowerCase());
    }

    @Nullable
    public Crate getCrateByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getString(item, Keys.CRATE_ID).orElse(null);
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
        PlayerUtil.addItem(player, crateItem);
    }

    public void previewCrate(@NotNull Player player, @NotNull CrateSource source) {
        Crate crate = source.getCrate();
        PreviewMenu menu = this.getPreview(crate);
        if (menu == null) return;

        menu.open(player, source, 1);
    }

    public void interactCrate(@NotNull Player player, @NotNull Crate crate, @NotNull InteractType action,
                              @Nullable ItemStack item, @Nullable Block block) {

        player.closeInventory();

        if (action == InteractType.CRATE_PREVIEW) {
            this.previewCrate(player, new CrateSource(crate, item, block));
            return;
        }

        if (action == InteractType.CRATE_OPEN || action == InteractType.CRATE_MASS_OPEN) {
            OpenSettings settings = OpenSettings.create(block, item);
            if (action == InteractType.CRATE_MASS_OPEN) {
                //settings.setBulk(true);
                settings.setSkipAnimation(true);
            }

            CrateUser user = this.plugin.getUserManager().getUserData(player);
            user.setIgnoreSync(true);

            boolean isOpened = this.openCrate(player, crate, settings);
            if (!isOpened) {
                if (block != null && crate.isPushbackEnabled()) {
                    player.setVelocity(player.getEyeLocation().getDirection().setY(Config.CRATE_PUSHBACK_Y.get()).multiply(Config.CRATE_PUSHBACK_MULTIPLY.get()));
                }
            }
            else {
                if (action == InteractType.CRATE_MASS_OPEN) {
                    // Use 'for' instead of while to prevent spending keys that are possible to be as rewards in that crate.
                    // So open no more than player currently have.
                    int has = plugin.getKeyManager().getKeysAmount(player, crate);
                    for (int keys = 0; keys < has; keys++) {
                        if (!this.openCrate(player, crate, settings)) {
                            break;
                        }
                    }
                }

                this.plugin.runTaskAsync(task -> {
                    crate.saveLastOpenData();
                    this.plugin.getData().saveUser(user);
                    user.setIgnoreSync(false);
                });
            }
        }
    }

    public boolean openCrate(@NotNull Player player, @NotNull Crate crate, @NotNull OpenSettings settings) {
        PlayerOpeningData data = PlayerOpeningData.get(player);
        if (data != null && !data.isCompleted()) {
            return false;
        }
        // Stop mass open (mostly only this case) if crate itemstack is out.
        if (settings.getCrateItem() != null && settings.getCrateItem().getAmount() <= 0) {
            return false;
        }

        if (!settings.isForce() && !crate.hasPermission(player)) {
            plugin.getMessage(Lang.ERROR_PERMISSION_DENY).send(player);
            return false;
        }

        CrateUser user = plugin.getUserManager().getUserData(player);
        if (!settings.isForce() && user.isCrateOnCooldown(crate)) {
            long expireDate = user.getCrateCooldown(crate);
            this.plugin.getMessage(expireDate < 0 ? Lang.CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED : Lang.CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY)
                .replace(Placeholders.GENERIC_TIME, TimeUtil.formatTimeLeft(expireDate))
                .replace(crate.replacePlaceholders())
                .send(player);
            return false;
        }

        CrateKey key = this.plugin.getKeyManager().getKeys(player, crate).stream().findFirst().orElse(null);
        if (!settings.isForce() && crate.isKeyRequired()) {
            if (key == null) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_KEY).replace(crate.replacePlaceholders()).send(player);
                return false;
            }
            if (!key.isVirtual() && Config.CRATE_HOLD_KEY_TO_OPEN.get()) {
                ItemStack main = player.getInventory().getItemInMainHand();
                if (!this.plugin.getKeyManager().isKey(main, crate)) {
                    plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_HOLD_KEY).replace(crate.replacePlaceholders()).send(player);
                    return false;
                }
            }
        }

        var openCost = new HashMap<>(crate.getOpenCostMap());

        openCost.keySet().removeIf(currency -> {
            if (settings.isForce()) return true;
            return player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST) || player.hasPermission(Perms.PREFIX_BYPASS_OPEN_COST + crate.getId());
        });

        for (var entry : openCost.entrySet()) {
            Currency currency = entry.getKey();
            double amount = entry.getValue();
            if (currency.getHandler().getBalance(player) < amount) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_CANT_AFFORD)
                    .replace(Placeholders.GENERIC_AMOUNT, currency.format(amount))
                    .replace(crate.replacePlaceholders()).send(player);
                return false;
            }
        }

        if (crate.getRewards(player).isEmpty()) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_REWARDS).replace(crate.replacePlaceholders()).send(player);
            return false;
        }

        if (!settings.isForce() && player.getInventory().firstEmpty() == -1) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE).replace(crate.replacePlaceholders()).send(player);
            return false;
        }

        CrateOpenEvent openEvent = new CrateOpenEvent(crate, player);
        plugin.getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled()) return false;

        // Take costs
        openCost.forEach((currency, amount) -> currency.getHandler().take(player, amount));
        // Take key
        if (key != null) {
            this.plugin.getKeyManager().takeKey(player, key, 1);
        }
        // Take crate item stack
        ItemStack item = settings.getCrateItem();
        if (item != null) {
            item.setAmount(item.getAmount() - 1);
        }

        OpeningMenu opening = this.getOpening(crate);
        if (opening != null) {
            opening.open(player, crate);

            if (settings.isSkipAnimation()) {
                PlayerOpeningData.clean(player);
                player.closeInventory();
            }
        }
        else {
            Reward reward = crate.rollReward(player);
            reward.give(player);

            CrateObtainRewardEvent rewardEvent = new CrateObtainRewardEvent(reward, player);
            plugin.getPluginManager().callEvent(rewardEvent);

            Block block = settings.getCrateBlock();
            if (!settings.isSkipAnimation() && Config.CRATE_DISPLAY_REWARD_ABOVE_BLOCK.get() && block != null) {
                if (block.getState() instanceof Lidded lidded) {
                    lidded.open();
                    plugin.runTaskLater(task -> lidded.close(), 60L);
                }
                HologramHandler hologramHandler = plugin.getHologramHandler();
                if (hologramHandler != null) {
                    Location location = LocationUtil.getCenter(block.getLocation().add(0, 2, 0), false);
                    hologramHandler.createReward(player, reward, location);
                    plugin.runTaskLater(task -> hologramHandler.removeReward(player), 60L);
                }
            }
        }

        crate.setLastOpener(player.getDisplayName());

        this.setCrateCooldown(player, crate);
        this.addOpenings(player, crate, 1);
        this.proceedMilestones(player, crate);
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
            Reward reward = milestone == null ? null : crate.getMilestoneReward(milestone);
            if (reward != null) {
                reward.giveContent(player);
                plugin.getMessage(Lang.CRATE_OPEN_MILESTONE_COMPLETED)
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
}
