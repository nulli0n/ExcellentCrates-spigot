package su.nightexpress.excellentcrates;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.command.basic.*;
import su.nightexpress.excellentcrates.command.key.KeyCommand;
import su.nightexpress.excellentcrates.config.*;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.currency.CurrencyManager;
import su.nightexpress.excellentcrates.data.DataHandler;
import su.nightexpress.excellentcrates.data.UserManager;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.editor.EditorManager;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.hologram.HologramType;
import su.nightexpress.excellentcrates.hologram.impl.HologramDecentHandler;
import su.nightexpress.excellentcrates.hologram.impl.HologramInternalHandler;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.hooks.impl.PlaceholderHook;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.excellentcrates.menu.MenuManager;
import su.nightexpress.excellentcrates.opening.OpeningManager;
import su.nightexpress.excellentcrates.util.Creator;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.command.base.ReloadSubCommand;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.Version;

public class CratesPlugin extends NightDataPlugin<CrateUser> {

    private DataHandler dataHandler;
    private UserManager userManager;

    private EditorManager editorManager;
    private CurrencyManager currencyManager;
    private OpeningManager openingManager;
    private KeyManager keyManager;
    private CrateManager crateManager;
    private MenuManager menuManager;

    private HologramHandler hologramHandler;
    private CrateLogger crateLogger;

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Crates", new String[]{"excellentcrates", "ecrates", "crates", "crate", "case", "cases"})
                .setConfigClass(Config.class)
                .setLangClass(Lang.class)
                .setPermissionsClass(Perms.class);
    }

    @Override
    public void enable() {
        Keys.load(this);

        this.getLangManager().loadEntries(EditorLang.class);
        this.registerCommands();
        this.setupHologramHandler();

        this.crateLogger = new CrateLogger(this);

        Creator creator = new Creator(this);
        creator.createDefaults();

        this.dataHandler = new DataHandler(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        this.currencyManager = new CurrencyManager(this);
        this.currencyManager.setup();

        this.openingManager = new OpeningManager(this);
        this.openingManager.setup();

        this.editorManager = new EditorManager(this);
        this.editorManager.setup();

        this.keyManager = new KeyManager(this);
        this.keyManager.setup();

        this.crateManager = new CrateManager(this);
        this.crateManager.setup();

        this.menuManager = new MenuManager(this);
        this.menuManager.setup();

        if (Plugins.hasPlaceholderAPI()) {
            PlaceholderHook.setup(this);
        }
    }

    private void setupHologramHandler() {
        HologramType type = Config.getHologramType();
        if (type == HologramType.INTERNAL) {
            if (Plugins.isInstalled(HookId.PROTOCOL_LIB)) {
                if (Version.isBehind(Version.V1_19_R3)) {
                    this.error("Your server version (" + Version.getCurrent().getLocalized() + ") is not supported by internal holograms handler. Change holograms handler in config.yml.");
                } else if (!Plugins.isLoaded(HookId.PROTOCOL_LIB)) {
                    this.error(HookId.PROTOCOL_LIB + " is not working correctly.");
                    this.error("Download the latest build at:");
                    this.error("https://ci.dmulloy2.net/job/ProtocolLib/");
                } else {
                    this.hologramHandler = new HologramInternalHandler(this);
                }
            }
        } else if (type == HologramType.DECENT_HOLOGRAMS) {
            if (Plugins.isInstalled(HookId.DECENT_HOLOGRAMS)) {
                this.hologramHandler = new HologramDecentHandler(this);
            }
        }

        if (this.hasHolograms()) {
            this.hologramHandler.setup();
            this.info("Using " + StringUtil.capitalizeUnderscored(type.name().toLowerCase()) + " hologram handler.");
        } else {
            this.info("No plugins are available to handle crate holograms.");
        }
    }

    @Override
    public void disable() {
        if (this.editorManager != null) this.editorManager.shutdown();
        if (this.openingManager != null) this.openingManager.shutdown();
        if (this.keyManager != null) this.keyManager.shutdown();
        if (this.crateManager != null) this.crateManager.shutdown();
        if (this.menuManager != null) this.menuManager.shutdown();
        if (this.hologramHandler != null) this.hologramHandler.shutdown();
        if (this.currencyManager != null) this.currencyManager.shutdown();

        if (Plugins.hasPlaceholderAPI()) {
            PlaceholderHook.shutdown();
        }
    }

    private void registerCommands() {
        NightPluginCommand baseCommand = this.getBaseCommand();
        baseCommand.addChildren(new EditorCommand(this));
        baseCommand.addChildren(new DropCommand(this));
        baseCommand.addChildren(new DropKeyCommand(this));
        baseCommand.addChildren(new OpenCommand(this));
        baseCommand.addChildren(new OpenForCommand(this));
        baseCommand.addChildren(new GiveCommand(this));
        baseCommand.addChildren(new KeyCommand(this));
        baseCommand.addChildren(new MenuCommand(this));
        baseCommand.addChildren(new PreviewCommand(this));
        baseCommand.addChildren(new ResetCooldownCommand(this));
        baseCommand.addChildren(new ResetLimitCommand(this));
        baseCommand.addChildren(new ReloadSubCommand(this, Perms.COMMAND_RELOAD));
    }

    public boolean hasHolograms() {
        return this.hologramHandler != null;
    }

    @NotNull
    public CrateLogger getCrateLogger() {
        return crateLogger;
    }

    @Override
    @NotNull
    public DataHandler getData() {
        return this.dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @NotNull
    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    @NotNull
    public OpeningManager getOpeningManager() {
        return openingManager;
    }

    @NotNull
    public EditorManager getEditorManager() {
        return editorManager;
    }

    @NotNull
    public KeyManager getKeyManager() {
        return keyManager;
    }

    @NotNull
    public CrateManager getCrateManager() {
        return this.crateManager;
    }

    @NotNull
    public MenuManager getMenuManager() {
        return menuManager;
    }

    @Nullable
    public HologramHandler getHologramHandler() {
        return hologramHandler;
    }
}
