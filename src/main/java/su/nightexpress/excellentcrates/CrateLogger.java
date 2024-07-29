package su.nightexpress.excellentcrates;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CrateLogger {

    private final CratesPlugin plugin;

    public CrateLogger(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
    }

    public void logReward(@NotNull Player player, @NotNull Reward reward) {
        Crate crate = reward.getCrate();

        String text = player.getName() + " won " + reward.getName() +
            " (ID: " + reward.getId() + ", Weight: " + reward.getWeight() + ")" +
            " from " + crate.getName() + " (ID: " + crate.getId() + ")";

        this.log(text);
    }

    private void log(@NotNull String text) {
        if (!Config.LOGS_TO_CONSOLE.get() && !Config.LOGS_TO_FILE.get()) return;

        text = Colorizer.strip(NightMessage.asLegacy(text));

        if (Config.LOGS_TO_CONSOLE.get()) {
            this.plugin.info(text);
        }
        if (Config.LOGS_TO_FILE.get()) {
            String date = LocalDateTime.now().format(Config.LOGS_DATE_FORMAT.get());
            String path = this.plugin.getDataFolder() + "/" + Config.FILE_LOGS;
            BufferedWriter output;
            try {
                output = new BufferedWriter(new FileWriter(path, true));
                output.append("[").append(date).append("] ").append(text);
                output.newLine();
                output.close();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
