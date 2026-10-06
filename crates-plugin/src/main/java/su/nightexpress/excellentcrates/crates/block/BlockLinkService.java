package su.nightexpress.excellentcrates.crates.block;

import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.BlockLinker;
import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.excellentcrates.crates.block.position.DefaultCratePosition;
import su.nightexpress.excellentcrates.crates.block.position.DefaultCratePositionRegistry;
import su.nightexpress.excellentcrates.crates.data.CrateDataService;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class BlockLinkService implements BlockLinker {

    private final CrateDataService             dataService;
    private final DefaultCratePositionRegistry cratePositions;

    public BlockLinkService(CrateDataService dataService, DefaultCratePositionRegistry cratePositions) {
        this.dataService = dataService;
        this.cratePositions = cratePositions;
    }

    @Override
    public ActionResult linkCrateBlock(Crate crate, Location location) {
        World world = location.getWorld();
        ExactPos blockPos = ExactPos.from(location);
        AdaptedKey worldKey = BukkitKeys.getKey(world);

        BlockComponent component = crate.getComponentOrNull(CrateComponentKeys.BLOCK);
        if (component == null) {
            return ActionResult.fail(BlocksLang.ERROR_NO_BLOCKS_COMPONENT);
        }

        component.addBlockPosition(worldKey, blockPos);

        CratePosition cratePosition = new DefaultCratePosition(worldKey, blockPos);
        this.cratePositions.registerPosition(crate.getId(), cratePosition);
        this.dataService.markDirty(crate);

        return ActionResult.ok(BlocksLang.LINK_SUCCESS);
    }

    @Override
    public ActionResult unlinkCrateBlock(Crate crate, Location location) {
        World world = location.getWorld();
        ExactPos blockPos = ExactPos.from(location);
        AdaptedKey worldKey = BukkitKeys.getKey(world);

        BlockComponent component = crate.getComponentOrNull(CrateComponentKeys.BLOCK);
        if (component != null) {
            component.removeBlockPosition(worldKey, blockPos);
        }

        CratePosition cratePosition = new DefaultCratePosition(worldKey, blockPos);
        this.cratePositions.unregisterPosition(cratePosition);
        this.dataService.markDirty(crate);

        return ActionResult.ok(BlocksLang.UNLINK_SUCCESS);
    }
}
