package su.nightexpress.excellentcrates.key.dialog;

import su.nightexpress.excellentcrates.dialog.DialogKey;
import su.nightexpress.excellentcrates.dialog.generic.GenericItemDialog;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;

public class KeyDialogs {

    public static final DialogKey<KeyManager> CREATION = new DialogKey<>("key_creation");
    public static final DialogKey<CrateKey>   NAME     = new DialogKey<>("key_name");
    public static final DialogKey<GenericItemDialog.Data<CrateKey>> ITEM = new DialogKey<>("key_item");
}
