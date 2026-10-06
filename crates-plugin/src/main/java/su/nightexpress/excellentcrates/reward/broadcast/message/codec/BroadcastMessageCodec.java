package su.nightexpress.excellentcrates.reward.broadcast.message.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.broadcast.message.BroadcastMessage;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class BroadcastMessageCodec implements ConfigCodec<BroadcastMessage> {

    public static final BroadcastMessageCodec INSTANCE = new BroadcastMessageCodec();

    @Override
    public BroadcastMessage read(FileConfig config, String path) throws CodecReadException {
        List<String> text = config.getOrSet(path + ".text", ConfigCodecs.STRING_LIST, List.of());
        NightSound sound = config.getOrSet(path + ".sound", ConfigCodecs.NIGHT_SOUND, null);
        boolean playSound = config.getOrSet(path + ".play_sound", ConfigCodecs.BOOLEAN, false);

        return new BroadcastMessage(text, sound, playSound);
    }

    @Override
    public void write(FileConfig config, String path, BroadcastMessage value) {
        config.set(path + ".text", value.getText());
        config.set(path + ".sound", value.getSound());
        config.set(path + ".play_sound", value.isPlaySound());
    }
}
