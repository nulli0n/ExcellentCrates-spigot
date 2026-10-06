package su.nightexpress.engine.text.layout.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.text.layout.LayoutComponent;
import su.nightexpress.engine.text.layout.LayoutComponentGroup;
import su.nightexpress.engine.text.layout.TextLayout;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class TextLayoutCodec implements ConfigCodec<TextLayout> {

    public static final TextLayoutCodec INSTANCE = new TextLayoutCodec();

    @Override
    public TextLayout read(FileConfig config, String path) throws CodecReadException {
        Map<String, LayoutComponent> components = new HashMap<>();
        Map<String, LayoutComponentGroup> groups = new HashMap<>();

        String componentsPath = path + ".components";
        String groupsPath = path + ".groups";

        config.getSection(componentsPath).forEach(sId -> {
            LayoutComponent component = config.get(componentsPath + "." + sId, LayoutComponentCodec.INSTANCE);
            if (component == null) return;

            components.put(sId, component);
        });

        config.getSection(groupsPath).forEach(sId -> {
            LayoutComponentGroup group = config.get(groupsPath + "." + sId, LayoutComponentGroupCodec.INSTANCE);
            if (group == null) return;

            groups.put(sId, group);
        });

        List<String> text = config.getOrSet(path + ".text", ConfigCodecs.STRING_LIST, List.of());

        return new TextLayout(components, groups, text);
    }

    @Override
    public void write(FileConfig config, String path, TextLayout value) {
        config.set(path + ".components", null);
        config.set(path + ".groups", null);

        value.getComponents().forEach((sId, component) -> {
            config.set(path + ".components." + sId, component);
        });

        value.getGroups().forEach((sId, group) -> {
            config.set(path + ".groups." + sId, group);
        });

        config.set(path + ".text", value.getTextTemplate());
    }
}
