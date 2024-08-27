package su.nightexpress.excellentcrates.opening.inventory.script;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.stream.Collectors;

public class ScriptCompiledAction {

    private final ScriptAction action;
    private final ParameterResult parameterResult;

    public ScriptCompiledAction(@NotNull ScriptAction action, @NotNull ParameterResult parameterResult) {
        this.action = action;
        this.parameterResult = parameterResult;
    }

    @Nullable
    public static ScriptCompiledAction compile(@NotNull String str) {
        int indexStart = str.indexOf('[');
        int indexEnd = str.indexOf(']');
        if (indexStart < 0 || indexEnd < 0) return null;

        String actionName = str.substring(indexStart + 1, indexEnd);
        ScriptAction action = ScriptActions.getByName(actionName);
        if (action == null) return null;

        ScriptCompiledAction entry = new ScriptCompiledAction(action, new ParameterResult());

        action.getParameters().forEach(parameter -> {
            int index = str.indexOf(parameter.getName());
            if (index < 0) return;

            index += parameter.getName().length();
            if (str.charAt(index) == ':') {
                String sub = str.substring(index + 1);
                String content = StringUtil.parseQuotedContent(sub);
                entry.getParameters().add(parameter, parameter.getParser().apply(content));
            }
        });

        return entry;
    }

    @NotNull
    public String toRaw() {
        String action = "[" + this.getAction().getName() + "] ";
        String params = this.getParameters().getParams().entrySet().stream().map(entry -> {
            return entry.getKey().getName() + ":\"" + entry.getValue() + "\"";
        }).collect(Collectors.joining(" "));

        return action + params;
    }

    public ScriptAction getAction() {
        return action;
    }

    public ParameterResult getParameters() {
        return parameterResult;
    }
}
