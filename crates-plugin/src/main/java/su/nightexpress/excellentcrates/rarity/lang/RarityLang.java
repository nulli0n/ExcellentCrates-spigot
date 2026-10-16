package su.nightexpress.excellentcrates.rarity.lang;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class RarityLang implements LangContainer {

    public static final TextLocale REWARD_SORTER_RARITY_ID = LangEntry
        .builder("rarity.reward.sorter.rarity_id")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.AMETHYST_SHARD) + " Rarity ID");

    public static final TextLocale REWARD_SORTER_RARITY_WEIGHT = LangEntry
        .builder("rarity.reward.sorter.rarity_weight")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NETHERITE_INGOT) + " Rarity Weight");

    private RarityLang() {
    }
}
