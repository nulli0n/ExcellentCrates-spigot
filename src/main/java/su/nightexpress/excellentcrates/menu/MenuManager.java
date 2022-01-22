package su.nightexpress.excellentcrates.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrateMenu;
import su.nightexpress.excellentcrates.config.Config;

import java.util.*;

public class MenuManager extends AbstractManager<ExcellentCrates> {

    private Map<String, ICrateMenu> menuMap;

    public MenuManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.menuMap = new HashMap<>();
        this.plugin.getConfigManager().extract(Config.DIR_MENUS);

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_MENUS, true)) {
            try {
                ICrateMenu menu = new CrateMenu(plugin, cfg);
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
            this.menuMap.values().forEach(ICrateMenu::clear);
            this.menuMap.clear();
        }
    }

    @NotNull
    public Map<String, ICrateMenu> getMenuMap() {
        return menuMap;
    }

    @Nullable
    public ICrateMenu getMenuById(@NotNull String id) {
        return this.getMenuMap().get(id.toLowerCase());
    }

    @NotNull
    public Collection<ICrateMenu> getMenus() {
        return this.getMenuMap().values();
    }

    @NotNull
    public List<String> getMenuIds() {
        return new ArrayList<>(this.getMenuMap().keySet());
    }
}
