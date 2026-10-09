package su.nightexpress.excellentcrates.crates.block.item;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.excellentcrates.crates.block.settings.BlockSettings;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class BlockItemService {

    private final ReadOnlySettings<BlockSettings> settings;
    private final CratePlaceholders               cratePlaceholders;

    private final BlockRegistry blockRegistry;
    private final CrateRegistry crateRegistry;

    private final AdaptedKey crateIdKey;

    public BlockItemService(ReadOnlySettings<BlockSettings> settings,
                            CratePlaceholders cratePlaceholders,
                            BlockRegistry blockRegistry,
                            CrateRegistry crateRegistry,
                            KeyDomain domain) {
        this.settings = settings;
        this.cratePlaceholders = cratePlaceholders;
        this.blockRegistry = blockRegistry;
        this.crateRegistry = crateRegistry;

        this.crateIdKey = domain.make("crate-id");
    }

    public boolean isSupportedBlockItem(ItemStack itemStack) {
        return this.blockRegistry.getProviders()
            .stream()
            .anyMatch(provider -> provider.isBlock(itemStack));
    }

    public boolean isAssignedBlockItem(ItemStack itemStack) {
        return this.getCrateId(itemStack).isPresent();
    }

    public ActionResult assignCrateToBlockInHand(Player player, Crate crate) {
        PlayerInventory inventory = player.getInventory();

        ItemStack itemStack = inventory.getItemInMainHand();
        ActionResult result = this.assignCrateToBlockItem(crate, itemStack, assigned -> {
            inventory.setItemInMainHand(assigned);
        });

        return result;
    }

    public ActionResult assignCrateToBlockItem(Crate crate, ItemStack itemStack, Consumer<ItemStack> callback) {
        if (itemStack.getType().isAir()) {
            return ActionResult.fail(BlocksLang.ASSIGN_NO_ITEM_IN_HAND);
        }

        if (!this.isSupportedBlockItem(itemStack)) {
            return ActionResult.fail(BlocksLang.ASSIGN_NOT_A_BLOCK);
        }

        if (this.isAssignedBlockItem(itemStack)) {
            return ActionResult.fail(BlocksLang.ASSIGN_ALREADY_ASSIGNED);
        }

        ItemStack assigned = this.getBlockItem(crate, itemStack);
        callback.accept(assigned);

        return ActionResult.ok(BlocksLang.ASSIGN_SUCCESS);
    }

    public Optional<Identifier> getCrateId(ItemStack itemStack) {
        String rawKey = PDCUtil.getString(itemStack, this.crateIdKey.bukkit()).orElse(null);
        return rawKey == null ? Optional.empty() : IdentifierParser.parse(rawKey);
    }

    public Optional<Crate> getCrate(ItemStack itemStack) {
        return this.getCrateId(itemStack).map(id -> this.crateRegistry.get(id));
    }

    public ItemStack getBlockItem(Crate crate, ItemStack itemInHand) {
        BlockSettings settings = this.settings.get();

        ItemStack itemStack = new ItemStack(itemInHand);

        PlaceholderContext context = PlaceholderContext.builder()
            .with(SharedPlaceholders.NAME, () -> ItemUtil.getNameSerialized(itemStack))
            .with(SharedPlaceholders.LORE, () -> String.join("\n", ItemUtil.getLoreSerialized(itemStack)))
            .apply(this.cratePlaceholders.basePlaceholders(crate))
            .build();

        String fullName = context.apply(settings.blockName());
        List<String> fullLore = context.apply(settings.blockLore());

        ItemUtil.editMeta(itemStack, meta -> {
            ItemUtil.setCustomName(meta, fullName);
            ItemUtil.setLore(meta, fullLore);

            PDCUtil.set(meta, this.crateIdKey.bukkit(), crate.idString());
        });

        return itemStack;
    }
}
