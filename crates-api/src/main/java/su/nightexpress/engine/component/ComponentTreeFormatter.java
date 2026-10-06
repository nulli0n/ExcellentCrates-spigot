package su.nightexpress.engine.component;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class ComponentTreeFormatter {

    private static final String HEADER = TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("Plugin Components:");

    private static final String BUNDLE_FORMAT  = TagWrappers.GREEN.wrap("%s") + " " + TagWrappers.GRAY.wrap("(Bundle)");
    private static final String MODULE_FORMAT  = TagWrappers.BLUE.wrap("%s") + " " + TagWrappers.GRAY.wrap("(Module)");
    private static final String DEFAULT_FORMAT = TagWrappers.GRAY.wrap("%s");

    private static final String BRANCH      = TagWrappers.GOLD.wrap("├── ");
    private static final String LAST_BRANCH = TagWrappers.GOLD.wrap("└── ");
    private static final String INDENT      = "    ";
    private static final String VERTICAL    = TagWrappers.GOLD.wrap("│   ");

    private static final String STATUS_ON  = TagWrappers.GREEN.wrap("[ON]");
    private static final String STATUS_OFF = TagWrappers.RED.wrap("[OFF]");

    private ComponentTreeFormatter() {
    }

    public static String buildTree(PluginComponent rootNode) {
        StringBuilder builder = new StringBuilder();
        builder.append(HEADER).append("\n");

        appendComponent(rootNode, builder, "", true);

        return builder.toString();
    }

    private static void appendComponent(PluginComponent component, StringBuilder builder, String prefix,
                                        boolean isLast) {
        String displayName;
        if (component instanceof NamedComponentBundle namedBundle) {
            displayName = String.format(BUNDLE_FORMAT, namedBundle.getName());
        }
        else if (component instanceof ModuleComponent module) {
            displayName = String.format(MODULE_FORMAT, module.idString());
        }
        else {
            displayName = String.format(DEFAULT_FORMAT, component.getClass().getSimpleName());
        }

        String statusText = component.isRunning() ? STATUS_ON : STATUS_OFF;

        String marker = isLast ? LAST_BRANCH : BRANCH;
        builder.append(prefix)
            .append(marker)
            .append(displayName)
            .append(" ")
            .append(statusText)
            .append("\n");

        // If this component is a container, traverse its children
        if (component instanceof ComponentBundle core) {
            List<PluginComponent> children = core.getComponents(); // Retrieves the inner list

            // Calculate the prefix for the next level deep
            String childPrefix = prefix + (isLast ? INDENT : VERTICAL);

            for (int index = 0; index < children.size(); index++) {
                boolean isChildLast = index == children.size() - 1;
                appendComponent(children.get(index), builder, childPrefix, isChildLast);
            }
        }
    }
}
