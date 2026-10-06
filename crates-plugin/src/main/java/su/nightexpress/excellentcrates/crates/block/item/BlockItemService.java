package su.nightexpress.excellentcrates.crates.block.item;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.excellentcrates.crates.block.settings.BlockSettings;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class BlockItemService {

    private final ReadOnlySettings<BlockSettings> settings;
    private final CratePlaceholders               cratePlaceholders;

    private final BlockRegistry blockRegistry;
    private final CrateRegistry crateRegistry;

    private final AdaptedKey blockIdKey;
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

        this.blockIdKey = domain.make("block-id");
        this.crateIdKey = domain.make("crate-id");
    }

    public ActionResult getBlockItem(Player player, Crate crate, CrateBlock block) {
        return this.deliverBlockItem(player, crate, block, BlocksLang.ITEM_OBTAIN_SUCCESS);
    }

    public ActionResult giveBlockItem(Player player, Crate crate, CrateBlock block) {
        return this.deliverBlockItem(player, crate, block, BlocksLang.ITEM_GIVE_SUCCESS);
    }

    /**
     * The single source of truth for item generation, delivery, and placeholder injection.
     */
    private ActionResult deliverBlockItem(Player receiver, Crate crate, CrateBlock block,
                                          MessageLocale successLocale) {

        ItemStack itemStack = this.getBlockItem(crate, block.getKey()).orElse(null);

        if (itemStack == null) {
            return ActionResult.fail(BlocksLang.ITEM_ERROR_NO_ITEM_STACK, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> block.key().asString())
            );
        }

        Players.addItem(receiver, itemStack);

        return ActionResult.ok(successLocale, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_ITEM, () -> ItemUtil.getNameSerialized(itemStack))
            .with(CommonPlaceholders.PLAYER_NAME, receiver::getName)
        );
    }

    public Optional<Identifier> getCrateId(ItemStack itemStack) {
        String rawKey = PDCUtil.getString(itemStack, this.crateIdKey.bukkit()).orElse(null);
        return rawKey == null ? Optional.empty() : IdentifierParser.parse(rawKey);
    }

    public Optional<Crate> getCrate(ItemStack itemStack) {
        return this.getCrateId(itemStack).map(id -> this.crateRegistry.get(id));
    }

    public Optional<AdaptedKey> getBlockId(ItemStack itemStack) {
        String rawKey = PDCUtil.getString(itemStack, this.blockIdKey.bukkit()).orElse(null);
        return rawKey == null ? Optional.empty() : BukkitKeys.parse(rawKey);
    }

    public Optional<Identifier> getBlockProviderId(ItemStack itemStack) {
        Optional<AdaptedKey> blockId = this.getBlockId(itemStack);
        return blockId.flatMap(key -> IdentifierParser.parse(key.namespace()));
    }

    public Optional<CrateBlock> getBlock(ItemStack itemStack) {
        return this.getBlockId(itemStack).map(this.blockRegistry::getBlock);
    }

    public Optional<BlockProvider<?>> getBlockProvider(ItemStack itemStack) {
        return this.getBlockProviderId(itemStack).map(this.blockRegistry::getProvider);
    }

    public Optional<ItemStack> getBlockItem(Crate crate, AdaptedKey blockId) {
        CrateBlock block = this.blockRegistry.getBlock(blockId);
        if (block == null) {
            return Optional.empty();
        }

        BlockSettings settings = this.settings.get();

        return block.getItemStack().map(itemStack -> {
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

                PDCUtil.set(meta, this.blockIdKey.bukkit(), blockId.asString());
                PDCUtil.set(meta, this.crateIdKey.bukkit(), crate.idString());
            });
            return itemStack;
        });
    }
}
