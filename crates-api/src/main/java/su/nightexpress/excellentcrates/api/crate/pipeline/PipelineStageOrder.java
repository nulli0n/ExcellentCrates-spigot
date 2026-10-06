package su.nightexpress.excellentcrates.api.crate.pipeline;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PipelineStageOrder {

    public static final int BUILD_BATCH = 100;

    public static final int PRE_ROLL_ANIMATION         = 500;
    public static final int PRE_ROLL_CRATE_QUOTA       = 1000;
    public static final int PRE_ROLL_REWARD_VALIDATION = 1500;
    public static final int PRE_ROLL_COST_SELECTION    = 2000;
    public static final int PRE_ROLL_BATCH_SELECTION   = 5000;

    public static final int ROLL_REWARDS   = 100;
    public static final int ROLL_SELECTION = 200;

    public static final int MODIFICATION_REWARD_LIMITS = 100;

    public static final int VALIDATE_COST = 100;
    public static final int VALIDATE_ITEM = 200;

    public static final int PRE_PROCESS_COST_TAKE   = 100;
    public static final int PRE_PROCESS_CRATE_QUOTA = 150;
    public static final int PRE_PROCESS_ITEM_TAKE   = 200;
    public static final int PRE_PROCESS_HOLOGRAMS   = 100;

    private PipelineStageOrder() {
    }
}
