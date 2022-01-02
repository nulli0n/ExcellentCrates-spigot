package su.nightexpress.excellentcrates.crate.editor.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EditorUtils;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.crate.CrateReward;
import su.nightexpress.excellentcrates.editor.CrateEditorInputHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditorHandlerCrate extends CrateEditorInputHandler<ICrate> {

    public EditorHandlerCrate(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public boolean onType(@NotNull Player player, @NotNull ICrate crate, @NotNull CrateEditorType type, @NotNull String msg) {
        switch (type) {
            case CRATE_REWARD_CREATE -> {
                String id = EditorUtils.fineId(msg);
                if (crate.getReward(id) != null) {
                    EditorUtils.errorCustom(player, plugin.lang().Editor_Reward_Error_Create_Exist.getMsg());
                    return false;
                }
                ICrateReward reward = new CrateReward(crate, id);
                crate.addReward(reward);
            }
            case CRATE_CHANGE_BLOCK_HOLOGRAM_TEXT -> {
                List<String> list = crate.getBlockHologramText();
                list.add(msg);
                crate.setBlockHologramText(list);
            }
            case CRATE_CHANGE_BLOCK_HOLOGRAM_OFFSET_Y -> {
                double offset = StringUtil.getDouble(StringUtil.colorOff(msg), 0D);
                crate.setBlockHologramOffsetY(offset);
            }
            case CRATE_CHANGE_COOLDOWN -> {
                int cooldown = StringUtil.getInteger(StringUtil.colorOff(msg), 0);
                crate.setOpenCooldown(cooldown);
            }
            case CRATE_CHANGE_CITIZENS -> {
                int npcId = StringUtil.getInteger(StringUtil.colorOff(msg), -1);
                if (npcId < 0) {
                    EditorUtils.errorNumber(player, false);
                    return false;
                }

                Set<Integer> has = IntStream.of(crate.getAttachedCitizens()).boxed().collect(Collectors.toSet());
                has.add(npcId);
                crate.setAttachedCitizens(has.stream().mapToInt(i -> i).toArray());
            }
            case CRATE_CHANGE_CONFIG_TEMPLATE -> crate.setAnimationConfig(EditorUtils.fineId(msg));
            case CRATE_CHANGE_CONFIG_PREVIEW -> crate.setPreviewConfig(EditorUtils.fineId(msg));
            case CRATE_CHANGE_NAME -> crate.setName(msg);
            case CRATE_CHANGE_KEYS -> crate.getKeyIds().add(EditorUtils.fineId(msg));
            case CRATE_CHANGE_OPEN_COST_MONEY -> {
                double costMoney = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                if (costMoney < 0) {
                    EditorUtils.errorNumber(player, true);
                    return false;
                }
                crate.setOpenCost(OpenCostType.MONEY, costMoney);
            }
            case CRATE_CHANGE_OPEN_COST_EXP -> {
                double costExp = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                if (costExp < 0) {
                    EditorUtils.errorNumber(player, true);
                    return false;
                }
                crate.setOpenCost(OpenCostType.EXP, (int) costExp);
            }
            case CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_NAME -> crate.getBlockEffect().setParticleName(StringUtil.colorOff(msg));
            case CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_DATA -> crate.getBlockEffect().setParticleData(StringUtil.colorOff(msg));
            default -> { }
        }

        crate.save();
        return true;
    }
}
