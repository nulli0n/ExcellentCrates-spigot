package su.nightexpress.excellentcrates.crate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
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
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateAnimation;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.api.hook.HologramHandler;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.data.CrateUser;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateManager extends AbstractManager<ExcellentCrates> {

    private Map<String, ICrate> crates;

    public CrateManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.crates = new HashMap<>();
        this.plugin.getConfigManager().extract(Config.DIR_CRATES);
        this.plugin.getConfigManager().extract(Config.DIR_PREVIEWS);

        for (JYML cfgLegacy : JYML.loadAll(plugin.getDataFolder().getParentFile() + "/GoldenCrates/crates/", true)) {
            File exist = new File(plugin.getDataFolder() + Config.DIR_CRATES + cfgLegacy.getFile().getName());
            if (exist.exists()) {
                plugin.error("Could not convert '" + cfgLegacy.getFile().getName() + "': Such crate already exist!");
                continue;
            }

            Crate crateLegacy = Crate.fromLegacy(cfgLegacy);
            crateLegacy.save();
            plugin.info("Converted '" + cfgLegacy.getFile().getName() + "' Golden Crate crate!");
        }

        this.plugin.getServer().getScheduler().runTask(this.plugin, c -> {
            for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_CRATES, true)) {
                try {
                    Crate crate = new Crate(plugin, cfg);
                    this.crates.put(crate.getId(), crate);
                }
                catch (Exception ex) {
                    plugin.error("Could not load crate: " + cfg.getFile().getName());
                    ex.printStackTrace();
                }
            }
            this.plugin.info("Loaded " + this.getCratesMap().size() + " crates.");
        });

        this.addListener(new CrateListener(this));

        CrateEffectModel.start();
    }

    @Override
    public void onShutdown() {
        CrateEffectModel.shutdown();

        if (this.crates != null) {
            this.crates.values().forEach(ICrate::clear);
            this.crates.clear();
            this.crates = null;
        }
    }

    public boolean create(@NotNull String id) {
        if (this.getCrateById(id) != null) {
            return false;
        }

        ICrate crate = new Crate(this.plugin(), id);
        crate.save();
        this.getCratesMap().put(crate.getId(), crate);
        return true;
    }

    public boolean delete(@NotNull ICrate crate) {
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
        return this.getCrates().stream().filter(crate -> !crate.getKeyIds().isEmpty() || !keyOnly).map(ICrate::getId).toList();
    }

    @NotNull
    public Map<String, ICrate> getCratesMap() {
        return this.crates;
    }

    @NotNull
    public Collection<ICrate> getCrates() {
        return this.getCratesMap().values();
    }

    @Nullable
    public ICrate getCrateById(@NotNull String id) {
        return this.getCratesMap().get(id.toLowerCase());
    }

    @Nullable
    public ICrate getCrateByNPC(int id) {
        return this.getCrates().stream().filter(crate -> crate.isAttachedNPC(id)).findFirst().orElse(null);
    }

    @Nullable
    public ICrate getCrateByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getStringData(item, Keys.CRATE_ID);
        return id != null ? this.getCrateById(id) : null;
    }

    @Nullable
    public ICrate getCrateByBlock(@NotNull Block block) {
        return this.getCrateByLocation(block.getLocation());
    }

    @Nullable
    public ICrate getCrateByLocation(@NotNull Location loc) {
        return this.getCrates().stream().filter(crate -> crate.getBlockLocations().contains(loc)).findFirst().orElse(null);
    }

    public boolean spawnCrate(@NotNull ICrate crate, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        world.dropItemNaturally(location, crate.getItem());
        return true;
    }

    public void giveCrate(@NotNull Player player, @NotNull ICrate crate, int amount) {
        if (amount < 1) return;

        ItemStack crateItem = crate.getItem();
        crateItem.setAmount(Math.min(64, amount));
        PlayerUtil.addItem(player, crateItem);
    }

    public void interactCrate(@NotNull Player player, @NotNull ICrate crate, @NotNull CrateClickAction action,
                              @Nullable ItemStack item, @Nullable Block block) {

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

    public boolean openCrate(@NotNull Player player, @NotNull ICrate crate, boolean force, @Nullable ItemStack item, @Nullable Block block) {
        if (plugin.getAnimationManager().getAnimations().stream().anyMatch(a -> a.isOpening(player))) {
            return false;
        }

        if (!force && !crate.hasPermission(player)) {
            plugin.getMessage(Lang.ERROR_PERMISSION_DENY).send(player);
            return false;
        }
        int rewAmount = crate.rollRewardsAmount();

        CrateUser user = plugin.getUserManager().getUserData(player);
        if (!force && user.isCrateOnCooldown(crate)) {
            long expireDate = user.getCrateCooldown(crate);
            (expireDate < 0 ? plugin.getMessage(Lang.CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED) : plugin.getMessage(Lang.CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY))
                .replace("%time%", TimeUtil.formatTimeLeft(expireDate))
                .replace(Placeholders.CRATE_NAME, crate.getName())
                .send(player);
            return false;
        }

        ICrateKey crateKey = this.plugin.getKeyManager().getKeys(player, crate).stream().findFirst().orElse(null);
        if (!force) {
            if (!crate.getKeyIds().isEmpty() && crateKey == null) {
                plugin.getMessage(Lang.CRATE_OPEN_ERROR_NO_KEY).send(player);
                return false;
            }
        }

        double openCostMoney = crate.getOpenCost(OpenCostType.MONEY);
        double openCostExp = crate.getOpenCost(OpenCostType.EXP);
        if (force || player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST + OpenCostType.MONEY.name().toLowerCase())) {
            openCostMoney = 0D;
        }
        if (force || player.hasPermission(Perms.BYPASS_CRATE_OPEN_COST + OpenCostType.EXP.name().toLowerCase())) {
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

        /*if (!force && player.getInventory().firstEmpty() == -1) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE).replace(Placeholders.CRATE_NAME, crate.getName()).send(player);
            return false;
        }*/

        String animationConfig = crate.getAnimationConfig();
        ICrateAnimation animation = animationConfig == null ? null : this.plugin.getAnimationManager().getAnimationById(animationConfig);

        int emptySlots = 0;
        int maxSlots = (animation != null) ? animation.getRewardAmount() : crate.getMaxRewards();
        for (ItemStack item2 : player.getInventory().getStorageContents()) {
            if (item2 == null || item2.getType() == Material.AIR || item2.getType().isAir()) emptySlots++;
            }
        if (!force && emptySlots < maxSlots) {
            plugin.getMessage(Lang.CRATE_OPEN_ERROR_INVENTORY_SPACE_ALT).replace(Placeholders.CRATE_REWARDS, Integer.valueOf(maxSlots)).send(player);
            return false;
            }

        CrateOpenEvent preOpenEvent = new CrateOpenEvent(crate, player);
        plugin.getPluginManager().callEvent(preOpenEvent);
        if (preOpenEvent.isCancelled()) return false;

        // Take costs
        if (openCostMoney > 0 && VaultHook.hasEconomy()) VaultHook.takeMoney(player, openCostMoney);
        if (openCostExp > 0) player.setLevel(player.getLevel() - (int) openCostExp);

        if (animation != null) {
            animation.open(player, crate);
        }
        else {
            ICrateReward lastReward = null;
            for (int rewCount = 0; rewCount < rewAmount; rewCount++) {
                lastReward = crate.rollReward(player);
                if (lastReward != null) lastReward.give(player);
            }
            if (lastReward != null && block != null) {
                if (block.getState() instanceof Lidded lidded) {
                    lidded.open();
                    plugin.getServer().getScheduler().runTaskLater(plugin, lidded::close, 60L);
                }
                HologramHandler hologramHandler = plugin.getHologramHandler();
                if (hologramHandler != null) {
                    Location location = LocationUtil.getCenter(block.getLocation().add(0, 2, 0), false);
                    hologramHandler.createReward(player, lastReward, location);
                    plugin.getServer().getScheduler().runTaskLater(plugin, c -> hologramHandler.removeReward(player), 60L);
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

    public void setCrateCooldown(@NotNull Player player, @NotNull ICrate crate) {
        if (player.hasPermission(Perms.BYPASS_CRATE_COOLDOWN) || crate.getOpenCooldown() == 0) return;

        long cooldown = crate.getOpenCooldown();
        long endDate = cooldown < 0 ? -1 : System.currentTimeMillis() + cooldown * 1000L;

        CrateUser user = plugin.getUserManager().getUserData(player);
        user.setCrateCooldown(crate, endDate);
    }
}
