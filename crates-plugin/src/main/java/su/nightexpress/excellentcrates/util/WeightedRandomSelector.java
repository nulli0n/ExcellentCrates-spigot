package su.nightexpress.excellentcrates.util;

import java.util.Collection;
import java.util.function.ToDoubleFunction;
import java.util.random.RandomGenerator;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class WeightedRandomSelector {

    private static final double ZERO_WEIGHT = 0D;

    private final RandomGenerator random;

    public WeightedRandomSelector(RandomGenerator random) {
        this.random = random;
    }

    /**
     * Selects a random element from the collection based on its weight.
     *
     * @param items           The collection of items to choose from.
     * @param weightExtractor A function defining how to extract the double weight from an item.
     * @param <T>             The type of elements in the collection.
     * @return The randomly selected item.
     */
    public <T> T selectItem(Collection<T> items, ToDoubleFunction<T> weightExtractor) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be null or empty.");
        }

        double totalWeight = this.calculateTotalWeight(items, weightExtractor);

        if (totalWeight <= ZERO_WEIGHT) {
            throw new IllegalArgumentException("Total weight of all items must be strictly positive.");
        }

        double randomValue = random.nextDouble(totalWeight);
        double currentWeightSum = 0D;

        for (T item : items) {
            double weight = weightExtractor.applyAsDouble(item);
            currentWeightSum += weight;

            if (randomValue < currentWeightSum) {
                return item;
            }
        }

        // Fallback for rare floating-point rounding inaccuracies at the extreme boundary
        return items.iterator().next();
    }

    private <T> double calculateTotalWeight(Collection<T> items, ToDoubleFunction<T> weightExtractor) {
        double total = 0D;
        for (T item : items) {
            double weight = weightExtractor.applyAsDouble(item);
            if (weight < ZERO_WEIGHT) {
                throw new IllegalArgumentException("Item weights must be non-negative. Found: " + weight);
            }
            total += weight;
        }
        return total;
    }
}