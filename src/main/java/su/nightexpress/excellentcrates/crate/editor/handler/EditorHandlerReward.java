package su.nightexpress.excellentcrates.crate.editor.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EditorUtils;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.editor.CrateEditorInputHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

public class EditorHandlerReward extends CrateEditorInputHandler<ICrateReward> {

    public EditorHandlerReward(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public boolean onType(@NotNull Player player, @NotNull ICrateReward reward, @NotNull CrateEditorType type, @NotNull String msg) {
        switch (type) {
            case CRATE_REWARD_CHANGE_CHANCE -> {
                double chance = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                if (chance < 0) {
                    EditorUtils.errorNumber(player, true);
                    return false;
                }
                reward.setChance(chance);
            }
            case CRATE_REWARD_CHANGE_COMMANDS -> reward.getCommands().add(StringUtil.colorOff(msg));
            case CRATE_REWARD_CHANGE_NAME -> reward.setName(msg);
            case CRATE_REWARD_CHANGE_WIN_LIMITS_AMOUNT -> reward.setWinLimitAmount(StringUtil.getInteger(StringUtil.colorOff(msg), -1, true));
            case CRATE_REWARD_CHANGE_WIN_LIMITS_COOLDOWN -> reward.setWinLimitCooldown(StringUtil.getInteger(StringUtil.colorOff(msg), 0, true));
            default -> { }
        }

        reward.getCrate().save();
        return true;
    }
}
