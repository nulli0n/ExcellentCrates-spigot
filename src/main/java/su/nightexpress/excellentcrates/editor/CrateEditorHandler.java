package su.nightexpress.excellentcrates.editor;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.editor.AbstractEditorHandler;
import su.nexmedia.engine.utils.EditorUtils;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.crate.editor.handler.EditorHandlerCrate;
import su.nightexpress.excellentcrates.crate.editor.handler.EditorHandlerReward;
import su.nightexpress.excellentcrates.key.editor.handler.EditorHandlerKey;

import java.io.File;
import java.util.Map;

public class CrateEditorHandler extends AbstractEditorHandler<ExcellentCrates, CrateEditorType> {

    public static JYML CRATE_LIST;
    public static JYML CRATE_MAIN;
    public static JYML CRATE_REWARD_LIST;
    public static JYML CRATE_REWARD_MAIN;

    public static JYML KEY_LIST;
    public static JYML KEY_MAIN;

    public CrateEditorHandler(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.plugin.getConfigManager().extract("editor");

        if (CRATE_LIST == null || !CRATE_LIST.reload()) {
            CRATE_LIST = new JYML(new File(plugin.getDataFolder() + "/editor/crate/list.yml"));
        }
        if (CRATE_MAIN == null || !CRATE_MAIN.reload()) {
            CRATE_MAIN = new JYML(new File(plugin.getDataFolder() + "/editor/crate/main.yml"));
        }
        if (CRATE_REWARD_LIST == null || !CRATE_REWARD_LIST.reload()) {
            CRATE_REWARD_LIST = new JYML(new File(plugin.getDataFolder() + "/editor/crate/rewards_list.yml"));
        }
        if (CRATE_REWARD_MAIN == null || !CRATE_REWARD_MAIN.reload()) {
            CRATE_REWARD_MAIN = new JYML(new File(plugin.getDataFolder() + "/editor/crate/rewards_main.yml"));
        }


        if (KEY_LIST == null || !KEY_LIST.reload()) {
            KEY_LIST = new JYML(new File(plugin.getDataFolder() + "/editor/key/list.yml"));
        }
        if (KEY_MAIN == null || !KEY_MAIN.reload()) {
            KEY_MAIN = new JYML(new File(plugin.getDataFolder() + "/editor/key/main.yml"));
        }

        this.addInputHandler(ICrate.class, new EditorHandlerCrate(this.plugin()));
        this.addInputHandler(ICrateReward.class, new EditorHandlerReward(this.plugin()));
        this.addInputHandler(ICrateKey.class, new EditorHandlerKey(this.plugin()));
    }

    @Override
    protected boolean onType(@NotNull Player player, @NotNull Object object, @NotNull CrateEditorType type, @NotNull String input) {
        if (type == CrateEditorType.CRATE_CREATE) {
            if (!plugin.getCrateManager().create(EditorUtils.fineId(input))) {
                EditorUtils.errorCustom(player, plugin.lang().Editor_Crate_Error_Create_Exists.getLocalized());
                return false;
            }
            //this.plugin.getEditor().getCratesEditor().open(player, 1);
            return true;
        }
        if (type == CrateEditorType.KEY_CREATE) {
            if (!plugin.getKeyManager().create(EditorUtils.fineId(input))) {
                EditorUtils.errorCustom(player, plugin.lang().Editor_Key_Error_Create_Exist.getLocalized());
                return false;
            }
            //this.plugin.getEditor().getKeysEditor().open(player, 1);
            return true;
        }

        return super.onType(player, object, type, input);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCrateBlockClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Map.Entry<CrateEditorType, Object> editor = this.getEditor(player);
        if (editor == null) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        if (editor.getKey() == CrateEditorType.CRATE_CHANGE_BLOCK_LOCATION) {
            if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

            ICrate crate = (ICrate) editor.getValue();
            crate.getBlockLocations().add(block.getLocation());
            e.setUseInteractedBlock(Result.DENY);
            e.setUseItemInHand(Result.DENY);

            crate.save();
            this.endEdit(player);
            //crate.getEditor().open(player, 1);
        }
    }
}
