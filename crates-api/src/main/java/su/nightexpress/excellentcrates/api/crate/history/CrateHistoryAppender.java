package su.nightexpress.excellentcrates.api.crate.history;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CrateHistoryAppender {

    void append(HistoryLog log);
}
