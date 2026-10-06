package su.nightexpress.excellentcrates.crates.history.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.crates.history.appender.AsyncFileHistoryAppender;

@NullMarked
public class AsyncFileHistoryCloseController extends BasePluginComponent {

    private final AsyncFileHistoryAppender appender;

    public AsyncFileHistoryCloseController(AsyncFileHistoryAppender appender) {
        super();
        this.appender = appender;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {
        this.appender.close();
    }

    @Override
    protected void onStart() {

    }
}
