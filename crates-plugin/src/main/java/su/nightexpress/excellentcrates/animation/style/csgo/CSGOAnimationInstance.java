package su.nightexpress.excellentcrates.animation.style.csgo;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.core.animation.BaseAnimationInstance;
import su.nightexpress.nightcore.bridge.entity.WrappedTextDisplay;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Randomizer;

@NullMarked
public class CSGOAnimationInstance extends BaseAnimationInstance {

    // TODO In Settings
    private static final int   VISIBLE_ITEMS_RADIUS = 3; // 3 items left and right + 1 in the center
    private static final float ITEM_SPACING         = 0.6f;    // spacing between items
    private static final float MIN_SCALE            = 0.2f;       // size of the edge items (20%)
    private static final float MAX_SCALE            = 0.8f;       // size of the center item (100%)

    private final Location displayLocation;

    private final int  targetIndex;
    private final long durationTicks;
    private final long finishTickDelay;

    private final RouletteRenderer renderer;

    private int currentTick;

    public CSGOAnimationInstance(AnimationContext context, Runnable onComplete, CSGOAnimationSettings settings) {
        super(context, onComplete);
        this.displayLocation = this.adjustLocation(context.location());

        this.targetIndex = settings.totalRolls();
        this.durationTicks = settings.durationTicks();
        this.finishTickDelay = settings.finishTickDelay();

        this.renderer = new RouletteRenderer(this.displayLocation);
    }

    private Location adjustLocation(Location originLocation) {
        Block block = originLocation.getBlock();
        double offset = 0.5;
        double height = block.getBoundingBox().getHeight() + offset;
        return LocationUtil.setCenter2D(block.getLocation()).add(0, height, 0);
    }

    @Override
    protected void onStart() {
        this.currentTick = 0;
    }

    @Override
    protected void onStop() {
        // Cleanup renderer and any active nodes
        this.renderer.cleanup();
    }

    @Override
    protected boolean onTick() {
        currentTick++;

        if (currentTick < durationTicks) {
            // Calculate the current progress of the roulette animation
            double t = (double) currentTick / durationTicks;
            double progress = easeOutCubic(t) * targetIndex;

            renderer.render(progress);
            return true; // Animation continues
        }
        else if (currentTick == durationTicks) {
            // The roulette stopped at this tick.
            // Render the exact final position and highlight the winner.
            renderer.render(targetIndex);
            renderer.highlightWinner(targetIndex);
            return true;
        }
        else {
            // Waiting for the delay before fully completing
            return currentTick <= (durationTicks + finishTickDelay);
        }
    }

    /**
     * Mathematical function for smooth deceleration.
     * Makes the roulette spin quickly at the beginning and slow down smoothly at the end.
     */
    private double easeOutCubic(double t) {
        return 1.0 - Math.pow(1.0 - t, 3.0);
    }

    private Reward getRewardForIndex(int index) {
        if (index == targetIndex) {
            return this.reward.getGranted();
        }
        return Randomizer.pick(this.availableRewards);
    }

    /**
     * Responsible for the lifecycle of entities and their spatial transformation.
     */
    private class RouletteRenderer {

        private final Location                   centerLoc;
        private final Map<Integer, RouletteNode> activeNodes = new HashMap<>();

        public RouletteRenderer(Location centerLoc) {
            this.centerLoc = centerLoc;
        }

        public void render(double progress) {
            int minIndex = (int) Math.floor(progress - VISIBLE_ITEMS_RADIUS);
            int maxIndex = (int) Math.ceil(progress + VISIBLE_ITEMS_RADIUS);

            // Clear nodes that are out of the visible range
            activeNodes.entrySet().removeIf(entry -> {
                int index = entry.getKey();
                if (index < minIndex || index > maxIndex) {
                    entry.getValue().remove();
                    return true;
                }
                return false;
            });

            // Spawn and update nodes within the visible range
            for (int i = minIndex; i <= maxIndex; i++) {
                final int currentIndex = i;
                RouletteNode node = activeNodes.computeIfAbsent(i, idx -> spawnNode(currentIndex));
                updateTransform(node, currentIndex, progress);
            }
        }

        private RouletteNode spawnNode(int index) {
            Reward reward = getRewardForIndex(index);
            ItemStack itemStack = rewardPreview.createPreviewItem(reward);
            String rewardName = rewardPreview.getDisplayInfo(reward).name();

            ItemDisplay item = centerLoc.getWorld().spawn(centerLoc, ItemDisplay.class, ent -> {
                ent.setItemStack(itemStack);
                ent.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
                ent.setBillboard(ItemDisplay.Billboard.CENTER);
                ent.setInterpolationDelay(0);
                ent.setInterpolationDuration(1);
            });

            TextDisplay text = centerLoc.getWorld().spawn(centerLoc, TextDisplay.class, ent -> {
                WrappedTextDisplay wrapped = WrappedTextDisplay.wrap(ent);
                wrapped.setText(rewardName);
                ent.setBillboard(TextDisplay.Billboard.CENTER);
                ent.setAlignment(TextDisplay.TextAlignment.CENTER);
                //ent.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                ent.setSeeThrough(false);
                ent.setInterpolationDelay(0);
                ent.setInterpolationDuration(1);
            });

            return new RouletteNode(item, text);
        }

        private void updateTransform(RouletteNode node, int index, double progress) {
            float offset = (float) (index - progress);

            float normalizedDist = Math.abs(offset) / VISIBLE_ITEMS_RADIUS;
            float scaleFactor = (float) Math.pow(Math.max(0, 1.0f - normalizedDist), 2.5);
            float currentScale = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * scaleFactor;

            float sign = Math.signum(offset);
            float visualOffset = sign * (float) Math.pow(Math.abs(offset), 0.75);
            float translationX = visualOffset * ITEM_SPACING * -1.0f;

            // Item transformation
            Transformation itemTransform = new Transformation(
                new Vector3f(translationX, 0, 0),
                new AxisAngle4f(),
                new Vector3f(currentScale, currentScale, currentScale),
                new AxisAngle4f()
            );
            node.item.setTransformation(itemTransform);
            node.item.setInterpolationDelay(0);

            // Text transformation (shifted above the item)
            // The Y coefficient depends on the item's scale so that the text doesn't "detach" from the icon
            float translationY = 0.6f * currentScale;

            Transformation textTransform = new Transformation(
                new Vector3f(translationX, translationY, 0),
                new AxisAngle4f(),
                new Vector3f(currentScale, currentScale, currentScale),
                new AxisAngle4f()
            );
            node.text.setTransformation(textTransform);
            node.text.setInterpolationDelay(0);
        }

        public void highlightWinner(int index) {
            RouletteNode winnerNode = activeNodes.get(index);
            if (winnerNode == null) return;

            Color glowColor = rewardPreview.getColor(reward.getGranted());

            winnerNode.item.setGlowing(true);
            winnerNode.item.setGlowColorOverride(glowColor);
            winnerNode.text.setDefaultBackground(true);
        }

        public void cleanup() {
            activeNodes.values().forEach(RouletteNode::remove);
            activeNodes.clear();
        }
    }

    private record RouletteNode(ItemDisplay item, TextDisplay text) {

        public void remove() {
            if (item != null) item.remove();
            if (text != null) text.remove();
        }
    }
}