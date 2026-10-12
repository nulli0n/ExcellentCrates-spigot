# Opening animation visibility: multiplayer verification

Run this checklist on the project's supported Paper server with NightCore and the built ExcellentCrates JAR. Use at least two Minecraft clients (A and B), preferably a third observer C. Automated tests use API doubles and do not establish client rendering, tracking, sound delivery or packet ordering.

## Setup

- Place a physical crate with at least two visually distinct rewards and sufficient keys. Keep its existing costs, reward announcements, holograms and effects configured.
- Select a Simple Roll profile. Repeat the entire checklist for a CS:GO profile.
- Test `settings.visibility: PUBLIC`, then `PRIVATE`, reloading ExcellentCrates or restarting after editing the selected profile.
- Record the Paper, NightCore, plugin and client versions, profile settings and results. All manual checks below are pending until executed on a server.

## Four required combinations

| Animation | Visibility | Expected audience |
| --- | --- | --- |
| Simple Roll | PUBLIC | A and B see each other's floating item and reward name, with the existing sounds and firework. |
| Simple Roll | PRIVATE | Each opener sees only their own item and name, hears only their own animation sounds and receives only their own particle celebration. |
| CS:GO | PUBLIC | A and B see both roulettes, including text, moving/scaling entries and winner highlighting. |
| CS:GO | PRIVATE | Each opener sees only their own complete roulette, text and winner highlighting. |

For each combination:

- [ ] A opens while B watches at close range; verify the expected audience for entities, names, sounds and effects from the very first frame through the final reward.
- [ ] A and B open the **same physical crate simultaneously**. Both openings must succeed. In PRIVATE, neither client sees any part of the other's animation, including brief flashes. Verify this even when both rolls temporarily show the same item.
- [ ] Repeat with staggered start times and with three or more simultaneous openers.
- [ ] Open different crates simultaneously, then use different profiles (including PUBLIC and PRIVATE together) at nearby crates.
- [ ] C joins or walks into tracking range mid-animation. PRIVATE animations remain absent; PUBLIC animations appear normally.
- [ ] A and an observer leave tracking range and return before completion (extend profile timings if needed). A sees their own PRIVATE animation on return; the observer never sees it.
- [ ] For CS:GO, watch entries created late in the roll: items and text must share visibility, movement, scaling, interpolation and winner glow must remain correct.
- [ ] Confirm the displayed final reward matches the normal selected/granted reward for that animation, and keys, costs, probabilities, limits, cooldowns and reward delivery behave as before.
- [ ] Attempt a second opening from A during A's active animation. The existing single-player session restriction remains enforced, while B can still open the crate.

## Lifecycle and regression

Run these in both modes for both animations:

- [ ] Let an animation complete; no temporary items or display entities remain.
- [ ] Disconnect the opener during a roll and during the finish delay; verify cleanup from another client and from the server's entity inspection tools.
- [ ] Change worlds during a roll; verify cleanup in the original world and no additional sequential animation starts there.
- [ ] Disable/shut down the plugin with active animations, including a batch with multiple sequential rewards. All temporary entities disappear and callbacks do not create more animation entities.
- [ ] Open a batch normally: each reward animates in sequence, with correct visibility and one final pipeline completion. Interrupt a batch; rewards and pipeline completion still follow the plugin's existing grant semantics.
- [ ] Use fast opening (Shift-click): no animation entities or animation-specific effects appear, and rewards are granted normally.
- [ ] Remove `visibility` from existing customized profiles and reload: PUBLIC behavior and custom timing/name settings remain intact. Toggle modes and reload; new sessions use the updated profile.
- [ ] Confirm reward announcements, logs, previews, crate holograms, idle effects and physical crate placement are unchanged.

Startup failures, partial entity initialization, entity ownership/cleanup and callback/session guards also have automated coverage. Test infrastructure failures on a development server only if you have a safe way to inject them; do not damage a production world to test error handling.

## Automated checks

Use Java 25 and Gradle (the repository supplies wrapper properties but no wrapper launch scripts):

```text
gradle :crates-plugin:compileJava --console=plain
gradle :crates-plugin:test --console=plain
gradle build --console=plain
```

Tests cover profile defaults and YAML reloads, default profile generation, pre-insertion entity visibility, opener-only show calls, dynamic roulette entries, Simple Roll timing and sound/effect routing, winner highlighting, cleanup, failed startup, simultaneous player sessions and sequential/interrupted/shutdown completion behavior. They do not simulate Minecraft clients or verify world entity tracking. The complete plugin is `crates-plugin/build/libs/ExcellentCrates-7.1.0.jar`; the `-thin.jar` is not the distributable plugin.
