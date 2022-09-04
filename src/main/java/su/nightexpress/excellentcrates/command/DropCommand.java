package su.nightexpress.excellentcrates.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.config.EngineConfig;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.config.Lang;

import java.util.Arrays;
import java.util.List;

public class DropCommand extends AbstractCommand<ExcellentCrates> {

    public DropCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"drop"}, Perms.COMMAND_DROP);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_DROP_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_DROP_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 2) {
            return LocationUtil.getWorldNames();
        }
        if (arg == 3) {
            return Arrays.asList("<x>", NumberUtil.format(player.getLocation().getX()));
        }
        if (arg == 4) {
            return Arrays.asList("<y>", NumberUtil.format(player.getLocation().getY()));
        }
        if (arg == 5) {
            return Arrays.asList("<z>", NumberUtil.format(player.getLocation().getZ()));
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 6) {
            this.printUsage(sender);
            return;
        }

        ICrate crate = plugin.getCrateManager().getCrateById(args[1]);
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).replace(Placeholders.CRATE_ID, args[1]).send(sender);
            return;
        }

        World world = plugin.getServer().getWorld(args[2]);
        if (world == null) {
            plugin.getMessage(Lang.ERROR_WORLD_INVALID).replace("%world%", args[2]).send(sender);
            return;
        }

        double x = StringUtil.getDouble(args[3], 0, true);
        double y = StringUtil.getDouble(args[4], 0, true);
        double z = StringUtil.getDouble(args[5], 0, true);
        Location location = new Location(world, x, y, z);

        if (!plugin.getCrateManager().spawnCrate(crate, location)) return;

        plugin.getMessage(Lang.COMMAND_DROP_DONE)
            .replace(Placeholders.CRATE_ID, crate.getId())
            .replace(Placeholders.CRATE_NAME, crate.getName())
            .replace("%x%", NumberUtil.format(x))
            .replace("%y%", NumberUtil.format(y))
            .replace("%z%", NumberUtil.format(z))
            .replace("%world%", EngineConfig.getWorldName(world.getName()))
            .send(sender);
    }
}
