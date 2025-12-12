package su.nightexpress.excellentcrates.crate;

import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.DialogKey;
import su.nightexpress.excellentcrates.dialog.generic.GenericItemDialog;

public class CrateDialogs {

    public static final DialogKey<CrateManager> CRATE_CREATION = new DialogKey<>("crate_creation");
    public static final DialogKey<Crate>        CRATE_NAME     = new DialogKey<>("crate_name");
    public static final DialogKey<Crate>                         CRATE_DESCRIPTION        = new DialogKey<>("crate_description");
    public static final DialogKey<GenericItemDialog.Data<Crate>> CRATE_ITEM               = new DialogKey<>("crate_item");
    public static final DialogKey<Crate>                         CRATE_PREVIEW            = new DialogKey<>("crate_preview");
    public static final DialogKey<Crate>                         CRATE_OPENING            = new DialogKey<>("crate_opening");
    public static final DialogKey<Crate>                         CRATE_OPENING_LIMITS     = new DialogKey<>("crate_opening_limits");
    public static final DialogKey<Crate>                         CRATE_EFFECT             = new DialogKey<>("crate_effect");
    public static final DialogKey<Crate>                         CRATE_PARTICLE           = new DialogKey<>("crate_particle");
    public static final DialogKey<Crate>                         CRATE_HOLOGRAM           = new DialogKey<>("crate_hologram");
    public static final DialogKey<Crate>                         CRATE_POST_OPEN_COMMANDS = new DialogKey<>("crate_post_open_commands");

}
