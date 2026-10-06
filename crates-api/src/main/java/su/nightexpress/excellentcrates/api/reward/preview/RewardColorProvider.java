package su.nightexpress.excellentcrates.api.reward.preview;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardColorProvider {

    /**
     * Gets the color associated with the given reward.
     *
     * @param reward the reward for which to get the color
     * @return the color of the reward
     */
    Color getColor(Reward reward, Color defaultColor);
}
