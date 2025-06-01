package su.nightexpress.excellentcrates.util.inspect;

import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.util.Plugins;

public class Inspectors {

    public static final Inspector<Crate> CRATE = Inspector.create(inspector -> {
            if (Plugins.hasEconomyBridge()) {
                inspector.addInspection(Inspections.CRATE_OPEN_COST);
            }

            inspector.addInspection(Inspections.CRATE_ITEM);
            inspector.addInspection(Inspections.CRATE_PREVIEW);
            inspector.addInspection(Inspections.CRATE_ANIMATION);
            inspector.addInspection(Inspections.CRATE_HOLOGRAM);
            inspector.addInspection(Inspections.CRATE_PARTICLE_DATA);
            inspector.addInspection(Inspections.CRATE_KEY_REQUIREMENT);
            inspector.addInspection(Inspections.CRATE_REWARDS);
        }
    );

    public static final Inspector<Reward> REWARD = Inspector.create(inspector -> inspector
        .addInspection(Inspections.REWARD_PREVIEW)
        .addInspection(Inspections.REWARD_CONTENT)
    );

    public static final Inspector<CrateKey> KEY = Inspector.create(inspector -> inspector
        .addInspection(Inspections.KEY_ITEM)
    );
}
