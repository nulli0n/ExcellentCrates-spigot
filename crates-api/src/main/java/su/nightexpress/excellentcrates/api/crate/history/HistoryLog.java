package su.nightexpress.excellentcrates.api.crate.history;

import java.time.LocalDateTime;
import java.util.Map;

public record HistoryLog(LocalDateTime timestamp, Map<String, String> data) {

}
