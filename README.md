**ExcellentCrates** it is a modern, feature-rich, modular crate & lootbox plugin with an intuitive in-game GUI editor and smooth animations. Easily set up custom rewards, batch openings, portable crates, and fine-tuned limits without touching config files.

## Features

### IN-GAME EDITOR

Create your own crates, keys, and rewards using an **intuitive GUI editor**! All features are split into separate menus/categories, every action has its own button, and modern dialog popups handle text input and options!

![](https://nightexpressdev.com/excellentcrates/img/overview/editor.webp)

### MODULARITY & EXTENSIBILITY

Almost every feature is **fully optional**! Disable whatever you don’t like or need, and it won’t bother you again!

Thanks to our modular architecture, developers can hook their custom content into almost any plugin system - from animations to custom crate components!

### KEYS

Create infinite keys for your crates! You can even assign **multiple keys** to the exact same crate!

And with our GUI editor, turning existing in-game items into new keys takes literally a couple of clicks!

![](https://nightexpressdev.com/excellentcrates/img/overview/keys.webp)

### REWARDS

Create unlimited rewards for your crates, complete with **commands and custom items** support! Set up global and per-player **limits and cooldowns**.

Broadcast to the entire server when someone lands a legendary or special drop - with customizable chat messages based on reward rarity!

![](https://nightexpressdev.com/excellentcrates/img/overview/rewards.webp)

### ANIMATIONS

Treat players to cool opening animations and reward rollouts, making the whole process way more fun and engaging!

Create custom animation profiles for registered types, tweaking their parameters and visuals. A full **developer API** is available to create and register your own custom animations.

![](https://nightexpressdev.com/excellentcrates/img/overview/animations.webp)

Built-in animation profiles support `settings.visibility`:

- `PUBLIC` (default) shows the animation to nearby players, with the existing sounds and effects.
- `PRIVATE` shows the animation entities and reward names only to the player opening the crate. Multiple players can open the same crate at the same time and see their own animation.

Edit the profile selected for your crate under `plugins/ExcellentCrates/objects/animations/`, then reload ExcellentCrates or restart the server. Existing profiles without this setting continue using `PUBLIC`; newly generated profiles include it.

Visibility uses the existing enum configuration parser: values are case-insensitive, and unrecognized values fall back to `PUBLIC`.

Simple Roll (`simple_roll/<profile>.yml`, such as `simple_roll/default.yml`):

```yaml
settings:
  name: "Simple Roll (Default)"
  roll_amount: 12
  roll_interval: 5
  finish_delay: 40
  visibility: PRIVATE
```

CS:GO (`csgo_animation/<profile>.yml`, such as `csgo_animation/default.yml`):

```yaml
settings:
  name: "CSGO (Default)"
  total_rolls: 16
  duration_ticks: 50
  finish_tick_delay: 20
  visibility: PRIVATE
```

In `PRIVATE` mode, Simple Roll sends its sounds only to the opener and replaces the world firework with an opener-only firework particle burst. The burst creates no firework entity or explosion; its appearance depends on the player's particle settings. CS:GO's items, text and winner glow all follow the selected visibility; this animation has no separate sounds or fireworks. Reward announcements, logs, crate holograms and idle crate effects keep their existing behavior.

Client visibility still needs verification on a running server; see the [multiplayer test checklist](docs/animation-visibility-testing.md).

### BLOCKS, EFFECTS AND HOLOGRAMS

Place crates anywhere in one click - just put the crate block where you want it! Configure **holograms, particle effects**, and custom **click actions** for each crate block!

**Built-in automatic holograms** for your crates! Just type in your text and adjust the offset if needed - everything else is ready to go!

Bring your crates to life with particle effects and unique shapes! Almost all vanilla particle types are supported!

![](https://nightexpressdev.com/excellentcrates/img/overview/blocks.webp)

### BULK OPENING

Got a dozen keys on hand, but opening them one by one is too slow or boring? Open them all at once with **bulk opening**, picking the exact amount you want to unlock!

A smart GUI will show all available options. If there are none, well, there's nothing to choose from, hehe (the GUI won't even show up).

![](https://nightexpressdev.com/excellentcrates/img/overview/batch.webp)

### FAST OPENING

Tired of watching opening animations? Don't want to pick keys or amounts every single time? Just hold **Shift-click** - and the plugin will do everything instantly!

### PAYMENT / KEY SELECTION

If a crate accepts different keys or payment methods, players get to choose how to pay.

The GUI displays all available payment methods, showing player balances and the exact number of crates they can afford.

![](https://nightexpressdev.com/excellentcrates/img/overview/cost_selection.webp)

### PORTABLE CRATES

Turn crates into **portable lootboxes** - give them straight to player inventories or drop them in the world as pickup items!

### PREVIEW

_"Cool crate! What's inside?"_

Toggle reward preview individually per crate and pick its visual style!

Control exactly what shows up in reward icons: lore, rarity, drop chances, cooldowns, limits, and more - with full **placeholder support**!

Create custom preview profiles for registered types, tweaking their look and settings!

![](https://nightexpressdev.com/excellentcrates/img/overview/preview.webp)

### OPENING HISTORY

Track what players open, which keys they use, locations, and dropped loot - with console output and a dedicated **log file**!

## Getting Started

Please view the [documentation](https://nightexpressdev.com/excellentcrates) for installation instructions

## Releases

Please see the [changelog](https://nightexpressdev.com/excellentcrates/releases) for more details about a given release.

## Donate

If you like my work or enjoy using my plugins, feel free to [Buy me a coffee](https://ko-fi.com/nightexpress) :) Thank you! 🧡
