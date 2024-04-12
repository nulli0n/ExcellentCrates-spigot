package su.nightexpress.excellentcrates.command;


import su.nightexpress.nightcore.command.CommandFlag;

public class CommandFlags {

    public static final CommandFlag<Boolean> SILENT  = CommandFlag.booleanFlag("s");
    public static final CommandFlag<Boolean> NO_SAVE = CommandFlag.booleanFlag("nosave");
    public static final CommandFlag<Boolean> FORCE   = CommandFlag.booleanFlag("f");
}
