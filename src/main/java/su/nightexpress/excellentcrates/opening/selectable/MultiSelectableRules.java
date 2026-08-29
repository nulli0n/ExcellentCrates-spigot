package su.nightexpress.excellentcrates.opening.multiselectable;

public final class MultiSelectableRules {

    private MultiSelectableRules() {
    }

    public static int maxSelections(int remainingPaidOpenings, int rewardCount, int cooldownRemaining) {
        int paidLimit = 1 + Math.max(0, remainingPaidOpenings);
        int rewardLimit = Math.max(1, rewardCount);
        int cooldownLimit = Math.max(1, cooldownRemaining);
        return Math.max(1, Math.min(paidLimit, Math.min(rewardLimit, cooldownLimit)));
    }

    public static int extraCosts(int selectedAmount) {
        return Math.max(0, selectedAmount - 1);
    }

    public static int openingCount(int selectedAmount) {
        return Math.max(1, selectedAmount);
    }
}
