package su.nightexpress.excellentcrates.editor;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.editor.EditorObject;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.api.manager.IListener;
import su.nexmedia.engine.editor.EditorManager;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;

import java.io.File;

public class CrateEditorHandler extends AbstractManager<ExcellentCrates> implements IListener {

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

        this.registerListeners();
    }

    @Override
    protected void onShutdown() {
        this.unregisterListeners();
    }

    @Override
    public void registerListeners() {
        this.plugin.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCrateBlockClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        EditorObject<?, ?> editor = EditorManager.getEditorInput(player);
        if (editor == null) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        if (editor.getType() == CrateEditorType.CRATE_CHANGE_BLOCK_LOCATION) {
            if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

            ICrate crate = (ICrate) editor.getObject();
            crate.getBlockLocations().add(block.getLocation());
            e.setUseInteractedBlock(Result.DENY);
            e.setUseItemInHand(Result.DENY);

            crate.save();
            EditorManager.endEdit(player);
            //crate.getEditor().open(player, 1);
        }
    }
}
