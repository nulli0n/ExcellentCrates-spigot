package su.nightexpress.excellentcrates.api.crate.pipeline.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineComponent;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface AnimationProfileComponent extends PipelineComponent {

    AdaptedKey getProfileKey();

    void setProfileKey(AdaptedKey profileKey);
}
