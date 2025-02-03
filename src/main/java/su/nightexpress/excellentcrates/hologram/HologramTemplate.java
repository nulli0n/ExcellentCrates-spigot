package su.nightexpress.excellentcrates.hologram;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class HologramTemplate implements Writeable {

    private final String id;
    private final List<String> text;

    public HologramTemplate(@NotNull String id, @NotNull List<String> text) {
        this.id = id.toLowerCase();
        this.text = text;
    }

    @NotNull
    public static HologramTemplate read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        List<String> text = config.getStringList(path + ".Text");

        return new HologramTemplate(id, text);
    }

    @NotNull
    public static Map<String, HologramTemplate> getDefaultTemplates() {
        Map<String, HologramTemplate> map = new HashMap<>();

        HologramTemplate template1 = new HologramTemplate(Placeholders.DEFAULT, Lists.newList(
            LIGHT_YELLOW.enclose(BOLD.enclose(CRATE_NAME)),
            LIGHT_GRAY.enclose("You have " + LIGHT_YELLOW.enclose("%excellentcrates_keys_" + CRATE_ID + "%") + " keys"),
            LIGHT_GRAY.enclose("Edit templates in " + LIGHT_YELLOW.enclose("config.yml")))
        );

        HologramTemplate template2 = new HologramTemplate("example", Lists.newList(
            LIGHT_BLUE.enclose(BOLD.enclose(CRATE_NAME)),
            LIGHT_GRAY.enclose("Another hologram template."),
            LIGHT_GRAY.enclose("Edit templates in " + LIGHT_BLUE.enclose("config.yml")))
        );

        map.put(template1.getId(), template1);
        map.put(template2.getId(), template2);

        return map;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Text", this.text);
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public List<String> getText() {
        return new ArrayList<>(this.text);
    }
}
