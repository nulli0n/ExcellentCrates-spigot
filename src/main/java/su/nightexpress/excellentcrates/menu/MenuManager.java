package su.nightexpress.excellentcrates.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Config;

import java.util.*;

public class MenuManager extends AbstractManager<ExcellentCrates> {

    private Map<String, CrateMenu> menuMap;

    public MenuManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.menuMap = new HashMap<>();
        this.plugin.getConfigManager().extractResources(Config.DIR_MENUS);

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_MENUS, true)) {
            try {
                CrateMenu menu = new CrateMenu(plugin, cfg);
                this.getMenuMap().put(menu.getId(), menu);
            }
            catch (Exception ex) {
                plugin.error("Could not load crate menu: '" + cfg.getFile().getName() + "'");
                ex.printStackTrace();
            }
        }

        this.plugin.info("Loaded " + this.getMenuMap().size() + " crate menus.");
    }

    @Override
    public void onShutdown() {
        if (this.menuMap != null) {
            this.menuMap.values().forEach(CrateMenu::clear);
            this.menuMap.clear();
        }
    }

    @NotNull
    public Map<String, CrateMenu> getMenuMap() {
        return menuMap;
    }

    @Nullable
    public CrateMenu getMenuById(@NotNull String id) {
        return this.getMenuMap().get(id.toLowerCase());
    }

    @NotNull
    public Collection<CrateMenu> getMenus() {
        return this.getMenuMap().values();
    }

    @NotNull
    public List<String> getMenuIds() {
        return new ArrayList<>(this.getMenuMap().keySet());
    }
}
