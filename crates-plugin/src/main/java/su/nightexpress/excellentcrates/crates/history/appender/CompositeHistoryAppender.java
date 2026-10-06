package su.nightexpress.excellentcrates.crates.history.appender;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.history.CrateHistoryAppender;
import su.nightexpress.excellentcrates.api.crate.history.HistoryLog;

@NullMarked
public class CompositeHistoryAppender implements CrateHistoryAppender {

    private final List<CrateHistoryAppender> appenders;

    public CompositeHistoryAppender(CrateHistoryAppender... appenders) {
        this(List.of(appenders));
    }

    public CompositeHistoryAppender(List<CrateHistoryAppender> appenders) {
        this.appenders = List.copyOf(appenders);
    }

    @Override
    public void append(HistoryLog log) {
        for (CrateHistoryAppender appender : appenders) {
            appender.append(log);
        }
    }
}
