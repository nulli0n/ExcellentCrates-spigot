package su.nightexpress.excellentcrates.util;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class UIUtils {

    private UIUtils() {
    }

    public static String formatCommandList(List<String> commands) {
        if (commands.isEmpty()) {
            return Lang.UI_COMMAND_LIST_EMPTY.text();
        }

        StringBuilder builder = new StringBuilder();
        for (String command : commands) {
            builder.append(formatCommand(command)).append(TagWrappers.BR);
        }
        return builder.toString().trim();
    }

    public static String formatCommand(String command) {
        return Lang.UI_COMMAND_LIST_ENTRY.text().replace(CommonPlaceholders.GENERIC_ENTRY, command);
    }
}
