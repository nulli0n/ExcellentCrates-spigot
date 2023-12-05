package su.nightexpress.excellentcrates.command;

import su.nexmedia.engine.api.command.CommandFlag;

public class CommandFlags {

    public static final CommandFlag<Boolean> SILENT = CommandFlag.booleanFlag("s");
    public static final CommandFlag<Boolean> NO_SAVE = CommandFlag.booleanFlag("nosave");
}
