package su.nightexpress.excellentcrates.reward.broadcast.message;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.wrap.NightSound;

@NullMarked
public class BroadcastMessage {

    private final List<String> text;
    private final NightSound   sound;
    private final boolean      playSound;

    public BroadcastMessage(List<String> text, NightSound sound, boolean playSound) {
        this.text = List.copyOf(text);
        this.sound = sound;
        this.playSound = playSound;
    }

    public List<String> getText() {
        return text;
    }

    public NightSound getSound() {
        return sound;
    }

    public boolean isPlaySound() {
        return playSound;
    }
}
