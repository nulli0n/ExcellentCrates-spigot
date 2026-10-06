package su.nightexpress.excellentcrates.api.common.preview;

import java.util.List;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record NameAndLore(String name, List<String> lore) {
}