package su.nightexpress.excellentcrates;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.Version;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.command.list.ReloadSubCommand;
import su.nexmedia.engine.utils.EngineUtils;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.currency.CurrencyManager;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.command.*;
import su.nightexpress.excellentcrates.command.key.KeyMainCommand;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.data.DataHandler;
import su.nightexpress.excellentcrates.data.UserManager;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.editor.EditorLocales;
import su.nightexpress.excellentcrates.editor.EditorMainMenu;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.hologram.impl.HologramDecentHandler;
import su.nightexpress.excellentcrates.hologram.impl.HologramDisplaysHandler;
import su.nightexpress.excellentcrates.hologram.impl.HologramInternalHandler;
import su.nightexpress.excellentcrates.hooks.impl.PlaceholderHook;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.excellentcrates.menu.MenuManager;
import su.nightexpress.excellentcrates.hologram.HologramType;

public class ExcellentCratesPlugin extends NexPlugin<ExcellentCratesPlugin> implements UserDataHolder<ExcellentCratesPlugin, CrateUser> {

    private DataHandler dataHandler;
    private UserManager userManager;

    private CurrencyManager currencyManager;
    private KeyManager     keyManager;
    private CrateManager   crateManager;
    private MenuManager    menuManager;

    private EditorMainMenu editor;
    private HologramHandler hologramHandler;

    @Override
    @NotNull
    protected ExcellentCratesPlugin getSelf() {
        return this;
    }

    @Override
    public void enable() {
        this.setupHologramHandler();

        this.currencyManager = new CurrencyManager(this);
        this.currencyManager.setup();

        this.keyManager = new KeyManager(this);
        this.keyManager.setup();

        this.crateManager = new CrateManager(this);
        this.crateManager.setup();

        this.menuManager = new MenuManager(this);
        this.menuManager.setup();
    }

    private void setupHologramHandler() {
        HologramType type = Config.CRATE_HOLOGRAM_HANDLER.get();
        if (type == HologramType.INTERNAL) {
            if (EngineUtils.hasPlugin(HookId.PROTOCOL_LIB) && Version.isAtLeast(Version.V1_19_R3)) {
                this.hologramHandler = new HologramInternalHandler(this);
            }
        }
        else if (type == HologramType.HOLOGRAPHIC_DISPLAYS) {
            if (EngineUtils.hasPlugin(HookId.HOLOGRAPHIC_DISPLAYS)) {
                this.hologramHandler = new HologramDisplaysHandler(this);
            }
        }
        else if (type == HologramType.DECENT_HOLOGRAMS) {
            if (EngineUtils.hasPlugin(HookId.DECENT_HOLOGRAMS)) {
                this.hologramHandler = new HologramDecentHandler(this);
            }
        }

        if (this.hologramHandler != null) {
            this.info("Using " + StringUtil.capitalizeUnderscored(type.name().toLowerCase()) + " hologram handler.");
        }
        else {
            this.warn("No hologram handler is available. Do your server met all the requirements?");
        }
    }

    @Override
    public void disable() {
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
        if (this.keyManager != null) {
            this.keyManager.shutdown();
            this.keyManager = null;
        }
        if (this.crateManager != null) {
            this.crateManager.shutdown();
            this.crateManager = null;
        }
        if (this.menuManager != null) {
            this.menuManager.shutdown();
            this.menuManager = null;
        }
        if (this.hologramHandler != null) {
            this.hologramHandler.shutdown();
            this.hologramHandler = null;
        }
        if (this.currencyManager != null) {
            this.currencyManager.shutdown();
        }
        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderHook.shutdown();
        }
    }

    @Override
    public void loadConfig() {
        this.getConfig().initializeOptions(Config.class);
    }

    @Override
    public void loadLang() {
        this.getLangManager().loadMissing(Lang.class);
        this.getLangManager().loadEditor(EditorLocales.class);
        this.getLang().saveChanges();
    }

    @Override
    public void registerHooks() {
        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderHook.setup();
        }
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<ExcellentCratesPlugin> mainCommand) {
        mainCommand.addChildren(new EditorCommand(this));
        mainCommand.addChildren(new DropCommand(this));
        mainCommand.addChildren(new DropKeyCommand(this));
        mainCommand.addChildren(new OpenCommand(this));
        mainCommand.addChildren(new GiveCommand(this));
        mainCommand.addChildren(new KeyMainCommand(this));
        mainCommand.addChildren(new MenuCommand(this));
        mainCommand.addChildren(new PreviewCommand(this));
        mainCommand.addChildren(new ResetCooldownCommand(this));
        mainCommand.addChildren(new ResetLimitCommand(this));
        mainCommand.addChildren(new ReloadSubCommand<>(this, Perms.COMMAND_RELOAD));
    }

    @Override
    public void registerPermissions() {
        this.registerPermissions(Perms.class);
    }

    @Override
    public boolean setupDataHandlers() {
        this.dataHandler = DataHandler.getInstance(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();
        return true;
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

    @NotNull
    public EditorMainMenu getEditor() {
        if (this.editor == null) {
            this.editor = new EditorMainMenu(this);
        }
        return this.editor;
    }
}
