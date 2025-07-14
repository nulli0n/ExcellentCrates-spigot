package su.nightexpress.excellentcrates.hologram.handler;

import org.bukkit.entity.EntityType;
import su.nightexpress.excellentcrates.hologram.HologramHandler;

public abstract class AbstractHologramHandler implements HologramHandler {

    protected final EntityType hologramType;

    public AbstractHologramHandler() {
        this.hologramType = EntityType.TEXT_DISPLAY;
    }
}
