package su.nightexpress.excellentcrates.editor;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.hooks.HookId;

import java.util.ArrayList;
import java.util.List;

public enum CrateEditorType implements EditorButtonType {

    EDITOR_CRATES(Material.ENDER_CHEST, "Crate Editor",
        EditorButtonType.info("Create and configure your crates here!"),
        EditorButtonType.click("Left-Click to &fNavigate")),
    EDITOR_KEYS(Material.TRIPWIRE_HOOK, "Key Editor",
        EditorButtonType.info("Create and configure crate keys here!"),
        EditorButtonType.click("Left-Click to &fNavigate")),

    CRATE_OBJECT(Material.CHEST, "&e" + Placeholders.CRATE_NAME + " &7(ID: &f" + Placeholders.CRATE_ID + "&7)",
        EditorButtonType.click("Left-Click to &fEdit\nShift-Right to &fDelete &7(No Undo)")),
    CRATE_CREATE(Material.ANVIL, "Create a Crate",
        EditorButtonType.info("Creates a new crate."),
        EditorButtonType.click("Left-Click to &fCreate")),
    CRATE_CHANGE_NAME(Material.NAME_TAG, "Crate Display Name",
        EditorButtonType.current(Placeholders.CRATE_NAME),
        EditorButtonType.info("Sets the crate display name. This name is used in messages, GUIs, holograms, etc."),
        EditorButtonType.click("Left-Click to &fChange")),
    CRATE_CHANGE_PERMISSION(Material.REDSTONE, "Permission Requirement",
        EditorButtonType.current("Enabled: &f" + Placeholders.CRATE_PERMISSION_REQUIRED + "\nNode: &f" + Placeholders.CRATE_PERMISSION),
        EditorButtonType.info("Sets whether permission is required to open this crate."),
        EditorButtonType.click("Left-Click to &fToggle")),
    CRATE_CHANGE_COOLDOWN(Material.CLOCK, "Open Cooldown",
        EditorButtonType.current(Placeholders.CRATE_OPENING_COOLDOWN),
        EditorButtonType.info("Sets the cooldown for opening this crate again."),
        EditorButtonType.note("When set a negative value, crate can be opened only once."),
        EditorButtonType.click("Left-Click to &fChange\nRight-Click to &fDisable\n[Q] Key to &fOne-Timed")),
    CRATE_CHANGE_CONFIG(Material.PAINTING, "GUI Layout",
        EditorButtonType.current("Preview: &f" + Placeholders.CRATE_PREVIEW_CONFIG + "\nOpening: &f" + Placeholders.CRATE_ANIMATION_CONFIG),
        EditorButtonType.info("Sets the configuration used for crate Preview and Opening GUIs."),
        EditorButtonType.note("You can create/edit crate Previews in " + Config.DIR_PREVIEWS + " folder."),
        EditorButtonType.note("You can create/edit crate Openings in " + Config.DIR_OPENINGS + " folder."),
        EditorButtonType.click("Left-Click to &fChange Opening\nRight-Click to &fDisable Opening\nShift-Left to &fChange Preview\nShift-Right to &fDisable Preview")),
    CRATE_CHANGE_CONFIG_OPENING,
    CRATE_CHANGE_CONFIG_PREVIEW,
    CRATE_CHANGE_KEYS(Material.TRIPWIRE_HOOK, "Attached Keys",
        EditorButtonType.current(Placeholders.CRATE_KEY_IDS),
        EditorButtonType.info("Sets a list of crate keys, that will open this crate."),
        EditorButtonType.note("If no keys are attached, crate can be opened without any key(s)."),
        EditorButtonType.warn("Make sure to provide correct key identifiers!"),
        EditorButtonType.click("Left-Click to &fAdd Key\nRight-Click to &fClear List")),
    CRATE_CHANGE_CITIZENS(Material.PLAYER_HEAD, "Attached Citizens NPCs",
        EditorButtonType.current("NPC Ids:\n" + Placeholders.CRATE_ATTACHED_CITIZENS),
        EditorButtonType.info("Sets a list of NPC Ids that will open and preview this crate on clicks."),
        EditorButtonType.warn("You must have Citizens plugin installed!"),
        EditorButtonType.click("Left-Click to &fAdd NPC\nRight-Click to &fClear List")),
    CRATE_CHANGE_OPEN_COST(Material.GOLD_NUGGET, "Open Cost",
        EditorButtonType.current("Money: &f$" + Placeholders.CRATE_OPENING_COST_MONEY + "\nExp Levels: &f" + Placeholders.CRATE_OPENING_COST_EXP),
        EditorButtonType.info("Sets how many money/exp player will have to pay to open this crate."),
        EditorButtonType.warn("You must have Vault + Economy plugins installed for the Money cost to work!"),
        EditorButtonType.click("Left-Click to &fChange Money\nRight-Click to &fChange Exp\nShift-Left to &fDisable Money\nShift-Right to &fDisable Exp")),
    CRATE_CHANGE_OPEN_COST_MONEY,
    CRATE_CHANGE_OPEN_COST_EXP,
    CRATE_CHANGE_ITEM(Material.ENDER_CHEST, "Crate Item",
        EditorButtonType.current(Placeholders.CRATE_ITEM_NAME),
        EditorButtonType.info("Sets the crate item, that will be used when you give this crate to players, or adding it to crate menus."),
        EditorButtonType.note("Apply custom item name, lore and other settings before you put it here for best result."),
        EditorButtonType.click("Drag & Drop to &fReplace\nRight-Click to &fGet Item")),

    CRATE_CHANGE_BLOCK_LOCATION(Material.CHEST, "Block Locations & Pushback",
        EditorButtonType.current(Placeholders.CRATE_BLOCK_LOCATIONS + "\nPushback Enabled: &f" + Placeholders.CRATE_BLOCK_PUSHBACK_ENABLED),
        EditorButtonType.info("Sets a list of block locations to which this crate will be attached. When clicking on that blocks, it will preview or open the crate.\nAlso sets if the crate should push players back when they can't open the crate."),
        EditorButtonType.click("Left-Click to &fAssign Block\nRight-Click to &fClear List\n[Q] Key to &fToggle Pushback")),
    CRATE_CHANGE_BLOCK_HOLOGRAM(Material.ARMOR_STAND, "Block Hologram",
        EditorButtonType.current("Enabled: &f" + Placeholders.CRATE_BLOCK_HOLOGRAM_ENABLED + "\nY Offset: &f" + Placeholders.CRATE_BLOCK_HOLOGRAM_OFFSET_Y + "\nText:\n" + Placeholders.CRATE_BLOCK_HOLOGRAM_TEXT),
        EditorButtonType.info("Sets the hologram text to appear above the crate block(s)."),
        EditorButtonType.warn("You must have holograms plugin installed:\n- " + HookId.HOLOGRAPHIC_DISPLAYS + "\n- " + HookId.DECENT_HOLOGRAMS),
        EditorButtonType.click("Left-Click to &fAdd Line\nRight-Click to &fClear Text\nShift-Left to &fToggle Hologram\nShift-Right to &fChange Y Offset")),
    CRATE_CHANGE_BLOCK_HOLOGRAM_TEXT,
    CRATE_CHANGE_BLOCK_HOLOGRAM_OFFSET_Y,
    CRATE_CHANGE_BLOCK_EFFECT(Material.BLAZE_POWDER, "Block Effect",
        EditorButtonType.current("Model: &f" + Placeholders.CRATE_BLOCK_EFFECT_MODEL + "\nParticle: &f" + Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_NAME + "\nData: &f" + Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_DATA),
        EditorButtonType.info("Sets effect to play around the crate blocks infinitely."),
        EditorButtonType.note("Data format for colored particles: 'R,G,B' (255,0,0)"),
        EditorButtonType.note("Data format for item/block particles: MATERIAL_NAME (SAND)"),
        EditorButtonType.click("[Q] Key to &fToggle Model\nLeft-Click to &fChange Particle\nRight-Click to &fChange Data")),
    CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_NAME,
    CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_DATA,
    CRATE_CHANGE_REWARDS(Material.EMERALD, "Crate Rewards",
        EditorButtonType.info("Here you can create and manage rewards for the crate."),
        EditorButtonType.click("Left-Click to &fNavigate")),

    REWARD_OBJECT(Material.EMERALD, "&e" + Placeholders.REWARD_NAME + " &7(ID: &f" + Placeholders.REWARD_ID + "&7)",
        EditorButtonType.info("Chance: &f" + Placeholders.REWARD_CHANCE + "%"),
        EditorButtonType.click("Left-Click to &fEdit\n[Q] Key to &fDelete &7(No Undo)\nShift-Left to &fMove Forward\nShift-Right to &fMove Back")),
    REWARD_CREATE(Material.ANVIL, "Create Reward",
        EditorButtonType.info("Creates a new reward for the crate."),
        EditorButtonType.click("Left-Click to &fCreate\nDrag & Drop to &fQuick Create")),
    REWARD_SORT(Material.COMPARATOR, "Sort Rewards",
        EditorButtonType.info("Automatically sort your rewards in a certain order."),
        EditorButtonType.click("Left-Click to &fSort by Chance\nRight-Click to &fSort by Type\nShift-Left to &fSort by Name")),
    REWARD_CHANGE_NAME(Material.NAME_TAG, "Reward Display Name",
        EditorButtonType.current(Placeholders.REWARD_NAME),
        EditorButtonType.info("Sets the reward display name. This name is used in messages mostly."),
        EditorButtonType.warn("This is NOT the reward preview item name!"),
        EditorButtonType.click("Left-Click to &fChange\nRight-Click to &fSync with Preview Name")),
    REWARD_CHANGE_PREVIEW(Material.ITEM_FRAME, "Preview Item",
        EditorButtonType.current(Placeholders.REWARD_PREVIEW_NAME),
        EditorButtonType.info("Sets the preview item for this reward. This item will be displayed in crate preview and opening GUIs to display rewards."),
        EditorButtonType.note("Apply custom name, lore and other settings to the item before put it here for best result."),
        EditorButtonType.click("Drag & Drop to &fReplace\nRight-Click to &fGet Item")),
    REWARD_CHANGE_CHANCE(Material.COMPARATOR, "Chance",
        EditorButtonType.current(Placeholders.REWARD_CHANCE + "%"),
        EditorButtonType.info("Sets the chance for this reward to be rolled."),
        EditorButtonType.note("Reward chances works as 'weight' actually, so they don't have to be up to 100%."),
        EditorButtonType.click("Left-Click to &fChange")),
    REWARD_CHANGE_COMMANDS(Material.COMMAND_BLOCK, "Reward Commands",
        EditorButtonType.current(Placeholders.REWARD_COMMANDS),
        EditorButtonType.info("A list of commands that will be executed when this reward is given to the player."),
        EditorButtonType.note("Look in chat for command prefixes when adding a command."),
        EditorButtonType.click("Left-Click to &fAdd Command\nRight-Click to &fClear List")),
    REWARD_CHANGE_ITEMS(Material.CHEST_MINECART, "Reward Items",
        EditorButtonType.info("A list of items that will be added to the player's inventory when this reward is given."),
        EditorButtonType.note("Simply close the inventory to save & return."),
        EditorButtonType.click("Left-Click to &fNavigate")),
    REWARD_CHANGE_BROADCAST(Material.ENDER_EYE, "Broadcast",
        EditorButtonType.current("Enabled: &f" + Placeholders.REWARD_BROADCAST),
        EditorButtonType.info("Sets whether a win message will be broadcasted to all online players for this reward."),
        EditorButtonType.click("Left-Click to &fToggle")),
    REWARD_CHANGE_WIN_LIMITS(Material.REPEATER, "Win Limits",
        EditorButtonType.current("Amount: &f" + Placeholders.REWARD_WIN_LIMIT_AMOUNT + "\nCooldown: &f" + Placeholders.REWARD_WIN_LIMIT_COOLDOWN),
        EditorButtonType.info("Sets how many times and how often this reward can be rolled out to the player."),
        EditorButtonType.note("Set amount to -1 for unlimit."),
        EditorButtonType.note("Set cooldown to -1 for a one-time reward."),
        EditorButtonType.warn("Limit is per player, not a global one."),
        EditorButtonType.click("Left-Click to &fChange Amount\nRight-Click to &fChange Cooldown\n[Q] Key to &fDisable")),
    REWARD_CHANGE_WIN_LIMITS_AMOUNT,
    REWARD_CHANGE_WIN_LIMITS_COOLDOWN,
    REWARD_CHANGE_IGNORED_FOR_PERMISSIONS(Material.DAYLIGHT_DETECTOR, "Ignored Player Permissions",
        EditorButtonType.current(Placeholders.REWARD_IGNORED_FOR_PERMISSIONS),
        EditorButtonType.info("A list of permissions to prevent giving this reward away if player has any of them."),
        EditorButtonType.click("Left-Click to &fAdd Permission\nRight-Click to &fClear List")),

    KEY_OBJECT(Material.TRIPWIRE_HOOK, "&e" + Placeholders.KEY_NAME + " &7(ID: &f" + Placeholders.KEY_ID + "&7)",
        EditorButtonType.note("Is Virtual: &f" + Placeholders.KEY_VIRTUAL),
        EditorButtonType.click("Left-Click to &fEdit\nShift-Right to &fDelete &7(No Undo)")),
    KEY_CREATE(Material.ANVIL, "Create Key",
        EditorButtonType.info("Creates a new crate key."),
        EditorButtonType.click("Left-Click to &fCreate")),
    KEY_CHANGE_NAME(Material.NAME_TAG, "Key Display Name",
        EditorButtonType.current(Placeholders.KEY_NAME),
        EditorButtonType.info("Sets the key display name. This name is used in messages mostly."),
        EditorButtonType.warn("This option does not affect display name of the key item!"),
        EditorButtonType.click("Left-Click to &fChange")),
    KEY_CHANGE_ITEM(Material.TRIPWIRE_HOOK, "Key Item",
        EditorButtonType.current(Placeholders.KEY_ITEM_NAME),
        EditorButtonType.info("Sets the key item, that will be used when key is given to player's inventory."),
        EditorButtonType.note("Apply custom name, lore and other settings before put item here for best result."),
        EditorButtonType.click("Drag & Drop to &fReplace\nRight-Click to &fGet Item")),
    KEY_CHANGE_VIRTUAL(Material.ENDER_PEARL, "Virtual Key",
        EditorButtonType.current("Is Virtual: &f" + Placeholders.KEY_VIRTUAL),
        EditorButtonType.info("Sets whether this key is a virtual key. Virtual keys can not be given as inventory items and are stored in the database."),
        EditorButtonType.click("Left-Click to &fToggle")),
    ;

    private final Material     material;
    private       String       name;
    private       List<String> lore;

     CrateEditorType() {
        this(Material.AIR, "", "");
    }

    CrateEditorType(@NotNull Material material, @NotNull String name, @NotNull String... lores) {
        this.material = material;
        this.setName(name);
        this.setLore(EditorButtonType.fineLore(lores));
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return material;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = StringUtil.color(name);
    }

    @NotNull
    public List<String> getLore() {
        return lore;
    }

    public void setLore(@NotNull List<String> lore) {
        this.lore = StringUtil.color(new ArrayList<>(lore));
    }
}
