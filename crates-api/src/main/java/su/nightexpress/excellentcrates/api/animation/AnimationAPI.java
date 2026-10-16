package su.nightexpress.excellentcrates.api.animation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.animation.session.AnimationSessionAPI;

@NullMarked
public interface AnimationAPI extends PluginAPI {

    AnimationRegistry getRegistry();

    AnimationSessionAPI getSessionAPI();
}
