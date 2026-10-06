package su.nightexpress.excellentcrates.crates.history.appender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.crate.history.CrateHistoryAppender;
import su.nightexpress.excellentcrates.api.crate.history.HistoryLog;

@NullMarked
public class AsyncFileHistoryAppender implements CrateHistoryAppender, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileHistoryAppender.class);

    private final DateTimeFormatter     dateTimeFormatter;
    private final BlockingQueue<String> queue;
    private final Thread                workerThread;

    private volatile boolean isRunning;

    public AsyncFileHistoryAppender(Path logFilePath, DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.queue = new LinkedBlockingQueue<>();
        this.isRunning = true;

        // Initialize and start the worker thread for asynchronous file writing.
        this.workerThread = new Thread(() -> this.processQueue(logFilePath), "CrateHistory-Writer");
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    @Override
    public void append(HistoryLog log) {
        String time = this.dateTimeFormatter.format(log.timestamp());

        String logEntry = log.data().entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining(" | "));

        StringBuilder logBuilder = new StringBuilder()
            .append("[").append(time).append("] ")
            .append(logEntry);

        this.queue.offer(logBuilder.toString());
    }

    private void processQueue(Path logFilePath) {
        try (
            BufferedWriter writer = Files.newBufferedWriter(
                logFilePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            while (this.isRunning || !this.queue.isEmpty()) {
                // Wait for a new line for up to 100ms
                String line = this.queue.poll(100, TimeUnit.MILLISECONDS);

                if (line != null) {
                    writer.write(line);
                    writer.newLine();

                    // Flush only when the queue is empty.
                    // If 50 crates were opened in one tick, we will write them all and flush only once.
                    if (this.queue.isEmpty()) {
                        writer.flush();
                    }
                }
            }
        }
        catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to write crate history to file: {}", logFilePath, e);
        }
    }

    @Override
    public void close() {
        this.isRunning = false;

        try {
            // Give the worker thread up to 2 seconds to finish writing any remaining log entries in the queue.
            this.workerThread.join(2000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}