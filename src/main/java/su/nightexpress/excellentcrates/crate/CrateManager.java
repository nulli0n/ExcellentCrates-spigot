package su.nightexpress.excellentcrates.crate;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.api.menu.impl.Menu;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nexmedia.engine.hooks.external.VaultHook;
import su.nexmedia.engine.utils.*;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.CrateClickAction;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.api.event.CrateObtainRewardEvent;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.api.hologram.HologramHandler;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateReward;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.listener.CrateListener;
import su.nightexpress.excellentcrates.crate.task.CrateEffectTask;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;
import su.nightexpress.excellentcrates.opening.menu.OpeningMenu;

import java.util.*;

public class CrateManager extends AbstractManager<ExcellentCrates> {

    private final Map<String, Rarity>      rarityMap;
    private final Map<String, Crate>       crateMap;
    private final Map<String, OpeningMenu> openings;

    private CrateEffectTask effectTask;

    public CrateManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
        this.rarityMap = new HashMap<>();
        this.crateMap = new HashMap<>();
        this.openings = new HashMap<>();
    }

    @Override
    public void onLoad() {
        this.plugin.getConfigManager().extractResources(Config.DIR_CRATES);
        this.plugin.getConfigManager().extractResources(Config.DIR_PREVIEWS);
        this.plugin.getConfigManager().extractResources(Config.DIR_OPENINGS);

        JYML rarityConfig = JYML.loadOrExtract(plugin, Config.FILE_RARITY);
        for (String rarId : rarityConfig.getSection("")) {
            Rarity rarity = Rarity.read(rarityConfig, rarId, rarId);
            this.rarityMap.put(rarity.getId(), rarity);
        }
        this.plugin.info("Loaded " + this.getRarityMap().size() + " rarities!");

        for (JYML cfg : JYML.loadAll(this.plugin.getDataFolder() + Config.DIR_OPENINGS, true)) {
            OpeningMenu opening = new OpeningMenu(plugin, cfg);
            String id = cfg.getFile().getName().replace(".yml", "").toLowerCase();
            this.openings.put(id, opening);
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

        this.openings.values().forEach(Menu::clear);
        this.openings.clear();
        this.crateMap.values().forEach(Crate::clear);
        this.crateMap.clear();
        this.rarityMap.clear();
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

    public boolean create(@NotNull String id) {
        id = StringUtil.lowerCaseUnderscore(id);
        if (this.getCrateById(id) != null) {
            return false;
        }

        JYML cfg = new JYML(this.plugin.getDataFolder() + Config.DIR_CRATES, id + ".yml");
        Crate crate = new Crate(this.plugin, cfg);
        crate.setName(Lang.LIME + ChatColor.BOLD + StringUtil.capitalizeUnderscored(crate.getId()) + " Crate");
        crate.setOpeningConfig(null);
        crate.setPreviewConfig(Placeholders.DEFAULT);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZkN2ZkYjUwZjE0YzczMWM3MjdiMGUwZDE4OWI2YTg3NDMxOWZjMGQ3OWM4YTA5OWFjZmM3N2M3YjJkOTE5NiJ9fX0=");
        ItemUtil.mapMeta(item, meta -> {
            meta.setDisplayName(crate.getName());
        });
        crate.setItem(item);

        crate.setBlockPushbackEnabled(true);
        crate.setBlockHologramEnabled(false);
        crate.setBlockHologramOffsetY(1.5D);
        crate.setBlockHologramText(Arrays.asList(Lang.LIME + ChatColor.BOLD + crate.getName().toUpperCase(), Lang.GRAY + "Purchase keys at " + Lang.LIME + "http://samplesmp.com/store"));
        crate.setBlockEffectModel(CrateEffectModel.HELIX);
        crate.setBlockEffectParticle(SimpleParticle.of(Particle.REDSTONE, new Particle.DustOptions(Color.fromRGB(Rnd.get(256), Rnd.get(256), Rnd.get(256)), 1f)));
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
        return this.getCrates().stream().filter(crate -> !crate.getKeyIds().isEmpty() || !keyOnly).map(Crate::getId).toList();
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
    public Crate getCrateByLocation(@NotNull Location loc) {
        return this.getCrates().stream().filter(crate -> crate.getBlockLocations().contains(loc)).findFirst().orElse(null);
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

    public void interactCrate(@NotNull Player player, @NotNull Crate crate, @NotNull CrateClickAction action,
                              @Nullable ItemStack item, @Nullable Block block) {

        player.closeInventory();

        if (action == CrateClickAction.CRATE_PREVIEW) {
            crate.openPreview(player);
            return;
        }

        if (action == CrateClickAction.CRATE_OPEN) {
            boolean isOpened = this.openCrate(player, crate, false, item, block);
            if (!isOpened && block != null && crate.isBlockPushbackEnabled()) {
                player.setVelocity(player.getEyeLocation().getDirection().setY(Config.CRATE_PUSHBACK_Y.get()).multiply(Config.CRATE_PUSHBACK_MULTIPLY.get()));
            }
        }
    }

    public boolean openCrate(@NotNull Player player, @NotNull Crate crate, boolean force, @Nullable ItemStack item, @Nullable Block block) {
        PlayerOpeningData data = PlayerOpeningData.get(player);
        if (data != null && !data.isCompleted()) {
            return false;
        }

        if (!force && !crate.hasPermission(player)) {
            plugin.getMessage(Lang.ERROR_PERMISSION_DENY).send(player);
            return false;
        }

        CrateUser user = plugin.getUserManager().getUserData(player);
        if (!force && user.isCrateOnCooldown(crate)) {
            long expireDate = user.getCrateCooldown(crate);
            (expireDate < 0 ? plugin.getMessage(Lang.CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED) : plugin.getMessage(Lang.CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY))
                .replace(Placeholders.GENERIC_TIME, TimeUtil.formatTimeLeft(expireDate))
                .replace(crate.replacePlaceholders())
                .send(player);
            return false;
        }

        CrateKey crateKey = this.plugin.getKeyManager().getKeys(player, crate).stream().findFirst().orElse(null);
        if (!force && !crate.getKeyIds().isEmpty()) {
            if (crateKey == null) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_KEY).replace(crate.replacePlaceholders()).send(player);
                return false;
            }
            if (!crateKey.isVirtual() && Config.CRATE_HOLD_KEY_TO_OPEN.get()) {
                ItemStack main = player.getInventory().getItemInMainHand();
                if (!this.plugin.getKeyManager().isKey(main, crateKey)) {
                    plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_HOLD_KEY).replace(crate.replacePlaceholders()).send(player);
                    return false;
                }
            }
        }

        double openCostMoney = crate.getOpenCost(OpenCostType.MONEY);
        double openCostExp = crate.getOpenCost(OpenCostType.EXP);
        if (force || player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST_MONEY)) {
            openCostMoney = 0D;
        }
        if (force || player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST_EXP)) {
            openCostExp = 0D;
        }

        if (openCostMoney > 0 && VaultHook.hasEconomy()) {
            double balance = VaultHook.getBalance(player);
            if (balance < openCostMoney) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_COST_MONEY).replace(crate.replacePlaceholders()).send(player);
                return false;
            }
        }
        if (openCostExp > 0) {
            double balance = player.getLevel();
            if (balance < openCostExp) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_COST_EXP).replace(crate.replacePlaceholders()).send(player);
                return false;
            }
        }

        if (crate.getRewards(player).isEmpty()) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_REWARDS).replace(crate.replacePlaceholders()).send(player);
            return false;
        }

        if (!force && player.getInventory().firstEmpty() == -1) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE).replace(crate.replacePlaceholders()).send(player);
            return false;
        }

        CrateOpenEvent preOpenEvent = new CrateOpenEvent(crate, player);
        plugin.getPluginManager().callEvent(preOpenEvent);
        if (preOpenEvent.isCancelled()) return false;

        // Take costs
        if (openCostMoney > 0 && VaultHook.hasEconomy()) VaultHook.takeMoney(player, openCostMoney);
        if (openCostExp > 0) player.setLevel(player.getLevel() - (int) openCostExp);


        String animationConfig = crate.getOpeningConfig();
        OpeningMenu opening = animationConfig == null ? null : this.getOpening(animationConfig);
        if (opening != null) {
            opening.open(player, crate);
        }
        else {
            CrateReward reward = crate.rollReward(player);
            reward.give(player);

            CrateObtainRewardEvent rewardEvent = new CrateObtainRewardEvent(reward, player);
            plugin.getPluginManager().callEvent(rewardEvent);

            if (Config.CRATE_DISPLAY_REWARD_ABOVE_BLOCK.get() && block != null) {
                if (block.getState() instanceof Lidded lidded) {
                    lidded.open();
                    plugin.getServer().getScheduler().runTaskLater(plugin, lidded::close, 60L);
                }
                HologramHandler hologramHandler = plugin.getHologramHandler();
                if (hologramHandler != null) {
                    Location location = LocationUtil.getCenter(block.getLocation().add(0, 2, 0), false);
                    hologramHandler.createReward(player, reward, location);
                    plugin.getServer().getScheduler().runTaskLater(plugin, task -> hologramHandler.removeReward(player), 60L);
                }
            }
        }

        if (crateKey != null) {
            this.plugin.getKeyManager().takeKey(player, crateKey, 1);
        }
        if (item != null) {
            item.setAmount(item.getAmount() - 1);
        }

        this.setCrateCooldown(player, crate);
        user.setOpeningsAmount(crate, user.getOpeningsAmount(crate) + 1);
        user.saveData(this.plugin);
        return true;
    }

    public void setCrateCooldown(@NotNull Player player, @NotNull Crate crate) {
        if (player.hasPermission(Perms.BYPASS_CRATE_COOLDOWN) || crate.getOpenCooldown() == 0) return;

        long cooldown = crate.getOpenCooldown();
        long endDate = cooldown < 0 ? -1L : System.currentTimeMillis() + cooldown * 1000L;

        CrateUser user = plugin.getUserManager().getUserData(player);
        user.setCrateCooldown(crate, endDate);
    }
}
