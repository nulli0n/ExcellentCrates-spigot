package su.nightexpress.excellentcrates.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.Colorizer;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CrateLogger {

    public static void logReward(@NotNull Player player, @NotNull Reward reward) {
        Crate crate = reward.getCrate();

        String text = player.getName() + " won " + reward.getName() +
            " (ID: " + reward.getId() + ", Weight: " + reward.getWeight() + ")" +
            " from " + crate.getName() + " (ID: " + crate.getId() + ")";
        log(text);
    }

    private static void log(@NotNull String text) {
        if (!Config.LOGS_TO_CONSOLE.get() && !Config.LOGS_TO_FILE.get()) return;

        text = Colorizer.strip(text);

        if (Config.LOGS_TO_CONSOLE.get()) {
            ExcellentCratesAPI.PLUGIN.info(text);
        }
        if (Config.LOGS_TO_FILE.get()) {
            String date = LocalDateTime.now().format(Config.LOGS_DATE_FORMAT.get());
            String path = ExcellentCratesAPI.PLUGIN.getDataFolder() + "/" + Config.FILE_LOGS;
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
