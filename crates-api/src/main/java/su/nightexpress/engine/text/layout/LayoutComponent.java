package su.nightexpress.engine.text.layout;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public record LayoutComponent(String condition, String format, String fallback) {

    public String render(PlaceholderContext dataContext) {
        String resolvedCondition = dataContext.apply(this.condition);

        // If it's null, blank, OR remains unparsed (equals the original placeholder)
        if (resolvedCondition == null || resolvedCondition.isBlank() || resolvedCondition.equals(this.condition)) {
            return this.fallback == null ? "" : this.fallback;
        }

        return this.format.replace(TextLayoutConstants.VALUE, resolvedCondition);
    }
}