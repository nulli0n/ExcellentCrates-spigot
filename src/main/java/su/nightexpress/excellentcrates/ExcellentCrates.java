package su.nightexpress.excellentcrates;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.api.editor.AbstractEditorHandler;
import su.nexmedia.engine.api.editor.EditorHolder;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.hooks.external.citizens.CitizensHook;
import su.nightexpress.excellentcrates.animation.AnimationManager;
import su.nightexpress.excellentcrates.api.hook.HologramHandler;
import su.nightexpress.excellentcrates.command.*;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.data.CrateUser;
import su.nightexpress.excellentcrates.data.CrateUserData;
import su.nightexpress.excellentcrates.data.UserManager;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorHub;
import su.nightexpress.excellentcrates.editor.CrateEditorType;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.hooks.external.CrateCitizensListener;
import su.nightexpress.excellentcrates.hooks.external.PlaceholderHook;
import su.nightexpress.excellentcrates.hooks.hologram.HologramHandlerDecent;
import su.nightexpress.excellentcrates.hooks.hologram.HologramHandlerHD;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.excellentcrates.menu.MenuManager;

import java.sql.SQLException;

public class ExcellentCrates extends NexPlugin<ExcellentCrates> implements UserDataHolder<ExcellentCrates, CrateUser>, EditorHolder<ExcellentCrates, CrateEditorType> {

	private static ExcellentCrates instance;
	
	private Config config;
	private Lang lang;
	
	private CrateUserData dataHandler;
	private UserManager userManager;

	private CrateEditorHandler editorHandler;
	private CrateEditorHub editorHub;

	private KeyManager       keyManager;
	private AnimationManager animationManager;
    private CrateManager     crateManager;
    private MenuManager      menuManager;

    private HologramHandler hologramHandler;

    public static ExcellentCrates getInstance() {
    	return instance;
    }
    
	public ExcellentCrates() {
	    instance = this;
	}
	
	@Override
	public void enable() {
		this.animationManager = new AnimationManager(this);
		this.animationManager.setup();
		
		this.keyManager = new KeyManager(this);
		this.keyManager.setup();
		
	    this.crateManager = new CrateManager(this);
	    this.crateManager.setup();
	    
	    this.menuManager = new MenuManager(this);
	    this.menuManager.setup();

	    this.editorHandler = new CrateEditorHandler(this);
	    this.editorHandler.setup();
	}

	@Override
	public void disable() {
    	if (this.editorHandler != null) {
    		this.editorHandler.shutdown();
    		this.editorHandler = null;
		}
    	if (this.editorHub != null) {
    		this.editorHub.clear();
    		this.editorHub = null;
		}
		if (this.animationManager != null) {
			this.animationManager.shutdown();
			this.animationManager = null;
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
	}

	@Override
	public boolean useNewConfigFields() {
		return true;
	}

	@Override
	public void setConfig() {
		this.config = new Config(this);
		this.config.setup();
		
		this.lang = new Lang(this);
		this.lang.setup();
	}

	@Override
	public boolean setupDataHandlers() {
		try {
			this.dataHandler = CrateUserData.getInstance(this);
			this.dataHandler.setup();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		this.userManager = new UserManager(this);
		this.userManager.setup();
		
		return true;
	}

	@Override
	@NotNull
	public Config cfg() {
		return this.config;
	}
	
	@Override
	@NotNull
	public Lang lang() {
		return this.lang;
	}

	@Override
	public void registerHooks() {
    	if (Hooks.hasPlugin(HookId.HOLOGRAPHIC_DISPLAYS)) {
			this.hologramHandler = this.registerHook(HookId.HOLOGRAPHIC_DISPLAYS, HologramHandlerHD.class);
		}
    	if (Hooks.hasPlugin(HookId.DECENT_HOLOGRAMS)) {
    		this.hologramHandler = this.registerHook(HookId.DECENT_HOLOGRAMS, HologramHandlerDecent.class);
		}
		if (Hooks.hasPlaceholderAPI()) {
			this.registerHook(Hooks.PLACEHOLDER_API, PlaceholderHook.class);
		}
		CitizensHook citizensHook = this.getCitizens();
		if (citizensHook != null) {
			citizensHook.addListener(this, new CrateCitizensListener(this));
		}
	}

	@Override
	public void registerCommands(@NotNull GeneralCommand<ExcellentCrates> mainCommand) {
		mainCommand.addChildren(new DropCommand(this));
		mainCommand.addChildren(new ForceOpenCommand(this));
		mainCommand.addChildren(new GiveCommand(this));
		mainCommand.addChildren(new GivekeyCommand(this));
		mainCommand.addChildren(new KeysCommand(this));
		mainCommand.addChildren(new MenuCommand(this));
		mainCommand.addChildren(new PreviewCommand(this));
		mainCommand.addChildren(new ResetCooldownCommand(this));
		mainCommand.addChildren(new ResetLimitCommand(this));
		mainCommand.addChildren(new TakeKeyCommand(this));
	}

	@Override
	@NotNull
	public CrateUserData getData() {
		return this.dataHandler;
	}

	@NotNull
	@Override
	public UserManager getUserManager() {
		return userManager;
	}

	@NotNull
	public AnimationManager getAnimationManager() {
		return animationManager;
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

	@Override
	@NotNull
	public CrateEditorHub getEditor() {
    	if (this.editorHub == null) {
			JYML cfg = JYML.loadOrExtract(this, "/editor/main.yml");
    		this.editorHub = new CrateEditorHub(this, cfg);
		}
		return this.editorHub;
	}

	@Override
	@NotNull
	public AbstractEditorHandler<ExcellentCrates, CrateEditorType> getEditorHandlerNew() {
		return this.editorHandler;
	}
}
