package su.nightexpress.excellentcrates.crate;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.hooks.external.VaultHook;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.PDCUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.CrateClickAction;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.api.hologram.HologramHandler;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.data.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;
import su.nightexpress.excellentcrates.opening.menu.OpeningMenu;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrateManager extends AbstractManager<ExcellentCrates> {

    private Map<String, Crate> crates;
    private Map<String, OpeningMenu> openings;

    public CrateManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.crates = new ConcurrentHashMap<>();
        this.openings = new HashMap<>();
        this.plugin.getConfigManager().extractResources(Config.DIR_CRATES);
        this.plugin.getConfigManager().extractResources(Config.DIR_PREVIEWS);
        this.plugin.getConfigManager().extractResources(Config.DIR_OPENINGS);

        for (JYML cfg : JYML.loadAll(this.plugin.getDataFolder() + Config.DIR_OPENINGS, true)) {
            try {
                OpeningMenu opening = new OpeningMenu(plugin, cfg);
                String id = cfg.getFile().getName().replace(".yml", "").toLowerCase();
                this.openings.put(id, opening);
            }
            catch (Exception e) {
                this.plugin.error("Crate opening not loaded: " + cfg.getFile().getName());
                e.printStackTrace();
            }
        }

        this.plugin.getServer().getScheduler().runTask(this.plugin, c -> {
            for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_CRATES, true)) {
                Crate crate = new Crate(plugin, cfg);
                if (crate.load()) {
                    this.crates.put(crate.getId(), crate);
                }
                else {
                    this.plugin.error("Crate not loaded: " + cfg.getFile().getName());
                }
            }
            this.plugin.info("Loaded " + this.getCratesMap().size() + " crates.");
            CrateEffectModel.start();
        });

        this.addListener(new CrateListener(this));
    }

    @Override
    public void onShutdown() {
        PlayerOpeningData.PLAYERS.values().forEach(data -> data.stop(true));
        PlayerOpeningData.PLAYERS.clear();

        CrateEffectModel.shutdown();

        if (this.openings != null) {
            this.openings.values().forEach(AbstractMenu::clear);
            this.openings.clear();
        }
        if (this.crates != null) {
            this.crates.values().forEach(Crate::clear);
            this.crates.clear();
            this.crates = null;
        }
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
        if (this.getCrateById(id) != null) {
            return false;
        }

        Crate crate = new Crate(this.plugin(), id);
        crate.save();
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
        return this.crates;
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
    public Crate getCrateByNPC(int id) {
        return this.getCrates().stream().filter(crate -> crate.isAttachedNPC(id)).findFirst().orElse(null);
    }

    @Nullable
    public Crate getCrateByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getStringData(item, Keys.CRATE_ID);
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
                player.setVelocity(player.getEyeLocation().getDirection().setY(Config.CRATE_PUSHBACK_Y).multiply(Config.CRATE_PUSHBACK_MULTIPLY));
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
                .replace(Placeholders.CRATE_NAME, crate.getName())
                .send(player);
            return false;
        }

        CrateKey crateKey = this.plugin.getKeyManager().getKeys(player, crate).stream().findFirst().orElse(null);
        if (!force) {
            if (!crate.getKeyIds().isEmpty() && crateKey == null) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_KEY).send(player);
                return false;
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
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_COST_MONEY).send(player);
                return false;
            }
        }
        if (openCostExp > 0) {
            double balance = player.getLevel();
            if (balance < openCostExp) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_COST_EXP).send(player);
                return false;
            }
        }

        if (crate.getRewards(player).isEmpty()) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_REWARDS).send(player);
            return false;
        }

        if (!force && player.getInventory().firstEmpty() == -1) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE).replace(Placeholders.CRATE_NAME, crate.getName()).send(player);
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
            //animation.open(player, crate);
            opening.open(player, crate);
        }
        else {
            CrateReward reward = crate.rollReward(player);
            if (reward != null) {
                reward.give(player);

                if (block != null) {
                    if (block.getState() instanceof Lidded lidded) {
                        lidded.open();
                        plugin.getServer().getScheduler().runTaskLater(plugin, lidded::close, 60L);
                    }
                    HologramHandler hologramHandler = plugin.getHologramHandler();
                    if (hologramHandler != null) {
                        Location location = LocationUtil.getCenter(block.getLocation().add(0, 2, 0), false);
                        hologramHandler.createReward(player, reward, location);
                        plugin.getServer().getScheduler().runTaskLater(plugin, c -> hologramHandler.removeReward(player), 60L);
                    }
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
        return true;
    }

    public void setCrateCooldown(@NotNull Player player, @NotNull Crate crate) {
        if (player.hasPermission(Perms.BYPASS_CRATE_COOLDOWN) || crate.getOpenCooldown() == 0) return;

        long cooldown = crate.getOpenCooldown();
        long endDate = cooldown < 0 ? -1 : System.currentTimeMillis() + cooldown * 1000L;

        CrateUser user = plugin.getUserManager().getUserData(player);
        user.setCrateCooldown(crate, endDate);
    }
}
