package su.nightexpress.excellentcrates.api.animation;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AnimationInstance {

    boolean isRunning();

    void start();

    boolean tick();

    void stop();
}
