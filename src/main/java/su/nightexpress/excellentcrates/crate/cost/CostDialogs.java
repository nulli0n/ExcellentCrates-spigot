package su.nightexpress.excellentcrates.crate.cost;

import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.DialogKey;

public class CostDialogs {

    public static final DialogKey<Crate> CREATION       = new DialogKey<>("cost_creation");
    public static final DialogKey<Cost>  NAME           = new DialogKey<>("cost_name");
    public static final DialogKey<Cost>  ENTRY_CREATION = new DialogKey<>("cost_entry_creation");

}
