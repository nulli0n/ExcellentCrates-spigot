package su.nightexpress.excellentcrates.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.menu.impl.MenuConfig;

import java.util.*;

public class MenuManager extends AbstractManager<ExcellentCrates> {

    private final Map<String, MenuConfig> menuMap;

    public MenuManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
        this.menuMap = new HashMap<>();
    }

    @Override
    public void onLoad() {
        this.plugin.getConfigManager().extractResources(Config.DIR_MENUS);

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_MENUS, true)) {
            MenuConfig menu = new MenuConfig(plugin, cfg);
            if (menu.load()) {
                this.getMenuMap().put(menu.getId(), menu);
            }
            else this.plugin.error("Menu not loaded: '" + cfg.getFile().getName() + "'!");
        }

        this.plugin.info("Loaded " + this.getMenuMap().size() + " crate menus.");
    }

    @Override
    public void onShutdown() {
        this.getMenus().forEach(MenuConfig::clear);
        this.getMenuMap().clear();
    }

    @NotNull
    public Map<String, MenuConfig> getMenuMap() {
        return this.menuMap;
    }

    @Nullable
    public MenuConfig getMenuById(@NotNull String id) {
        return this.getMenuMap().get(id.toLowerCase());
    }

    @NotNull
    public Collection<MenuConfig> getMenus() {
        return this.getMenuMap().values();
    }

    @NotNull
    public List<String> getMenuIds() {
        return new ArrayList<>(this.getMenuMap().keySet());
    }
}
