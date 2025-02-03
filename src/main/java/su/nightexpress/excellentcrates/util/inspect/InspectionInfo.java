package su.nightexpress.excellentcrates.util.inspect;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;

public class InspectionInfo {

    private final InspectionResult result;
    private final String message;

    public InspectionInfo(InspectionResult result, String message) {
        this.result = result;
        this.message = message;
    }

    public static InspectionInfo good(@NotNull String message) {
        return new InspectionInfo(InspectionResult.GOOD, message);
    }

    public static InspectionInfo bad(@NotNull String message) {
        return new InspectionInfo(InspectionResult.BAD, message);
    }

    public static InspectionInfo warn(@NotNull String message) {
        return new InspectionInfo(InspectionResult.WARN, message);
    }

    @NotNull
    public InspectionResult getResult() {
        return this.result;
    }

    @NotNull
    public String getMessage() {
        return this.message;
    }

    @NotNull
    public String getColored() {
        return switch (this.result) {
            case GOOD -> Lang.goodEntry(this.message);
            case WARN -> Lang.warnEntry(this.message);
            case BAD -> Lang.badEntry(this.message);
        };
    }
}
