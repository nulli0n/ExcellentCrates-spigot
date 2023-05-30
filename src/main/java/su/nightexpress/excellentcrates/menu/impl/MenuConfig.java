package su.nightexpress.excellentcrates.menu.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractConfigHolder;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Placeholders;

public class MenuConfig extends AbstractConfigHolder<ExcellentCrates> implements ICleanable, Placeholder {

    private final MenuView menuView;
    private final PlaceholderMap placeholderMap;

    public MenuConfig(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.menuView = new MenuView(this);

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.MENU_ID, this::getId)
        ;
    }

    @Override
    public boolean load() {

        return true;
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @Override
    public void onSave() {

    }

    @Override
    public void clear() {
        this.menuView.clear();
    }

    @NotNull
    public MenuView getMenu() {
        return this.menuView;
    }

    public void open(@NotNull Player player) {
        this.menuView.open(player, 1);
    }
}
