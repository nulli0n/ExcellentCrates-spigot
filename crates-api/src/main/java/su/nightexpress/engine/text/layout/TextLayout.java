package su.nightexpress.engine.text.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class TextLayout {

    private final Map<String, LayoutComponent>      components;
    private final Map<String, LayoutComponentGroup> groups;
    private final List<String>                      textTemplate;

    public TextLayout(Map<String, LayoutComponent> components,
                      Map<String, LayoutComponentGroup> groups,
                      List<String> textTemplate) {
        this.components = Collections.unmodifiableMap(new HashMap<>(components));
        this.groups = Collections.unmodifiableMap(new HashMap<>(groups));
        this.textTemplate = Collections.unmodifiableList(new ArrayList<>(textTemplate));
    }

    public static Builder builder() {
        return new Builder();
    }

    private static String wrap(String key) {
        return "%" + key + "%";
    }

    public List<String> render(PlaceholderContext dataContext) {
        Map<String, String> layoutVariables = new HashMap<>();
        Map<String, String> wrappedLayoutVariables = new HashMap<>();

        // Render all base components using the data placeholders
        this.components.forEach((key, component) -> layoutVariables.put(key, component.render(dataContext)));

        // Render all groups using the pre-rendered components
        this.groups.forEach((key, group) -> layoutVariables.put(key, group.render(layoutVariables)));

        layoutVariables.forEach((key, value) -> wrappedLayoutVariables.put(wrap(key), value));

        // Create a context exclusively for the layout components/groups
        PlaceholderContext layoutContext = PlaceholderContext.builder()
            .with(wrappedLayoutVariables)
            .build();

        List<String> finalText = new ArrayList<>(this.textTemplate.size());

        for (String originalLine : this.textTemplate) {
            // Preserve lines that the user intentionally left blank in the config
            if (originalLine.isEmpty()) {
                finalText.add("");
                continue;
            }

            // Replace layout components and groups
            String layoutReplaced = layoutContext.apply(originalLine);

            // Replace data placeholders
            String fullyReplaced = dataContext.apply(layoutReplaced);

            // If the line contained placeholders but they all 
            // resolved to empty strings (or were just left as spaces), drop the line.
            if (fullyReplaced.isBlank()) {
                continue;
            }

            // Line Breaking: Handle <br> or \n
            StringUtil.splitDelimiters(fullyReplaced, finalText::add);
        }

        return finalText;
    }

    public Map<String, LayoutComponent> getComponents() {
        return components;
    }

    public Map<String, LayoutComponentGroup> getGroups() {
        return groups;
    }

    public List<String> getTextTemplate() {
        return textTemplate;
    }

    public static class Builder {

        private final Map<String, LayoutComponent>      components   = new HashMap<>();
        private final Map<String, LayoutComponentGroup> groups       = new HashMap<>();
        private final List<String>                      textTemplate = new ArrayList<>();

        public Builder withComponent(String key, LayoutComponent component) {
            this.components.put(key, component);
            return this;
        }

        public Builder withGroup(String key, LayoutComponentGroup group) {
            this.groups.put(key, group);
            return this;
        }

        public Builder withTextTemplate(List<String> textTemplate) {
            this.textTemplate.clear();
            this.textTemplate.addAll(textTemplate);
            return this;
        }

        public TextLayout build() {
            return new TextLayout(components, groups, textTemplate);
        }
    }
}