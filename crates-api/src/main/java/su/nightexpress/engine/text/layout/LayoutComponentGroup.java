package su.nightexpress.engine.text.layout;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LayoutComponentGroup(List<String> componentNames, String delimiter, String format) {

    public String render(Map<String, String> renderedComponents) {
        List<String> activeContent = this.componentNames.stream()
            .map(renderedComponents::get)
            .filter(Objects::nonNull)
            .filter(content -> !content.isEmpty())
            .toList();

        // If all components failed, return empty string so the group vanishes
        if (activeContent.isEmpty()) {
            return "";
        }

        // Join the active components
        String joined = String.join(this.delimiter, activeContent);

        // Wrap in the format if it exists
        return this.format != null ? this.format.replace(TextLayoutConstants.VALUE, joined) : joined;
    }
}