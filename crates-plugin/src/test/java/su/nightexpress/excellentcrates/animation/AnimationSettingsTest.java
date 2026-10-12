package su.nightexpress.excellentcrates.animation;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.CsvSource;

import su.nightexpress.excellentcrates.animation.style.csgo.CSGOAnimationSettings;
import su.nightexpress.excellentcrates.animation.style.csgo.CSGOAnimationProvider;
import su.nightexpress.excellentcrates.animation.style.csgo.codec.CSGOAnimationSettingsCodec;
import su.nightexpress.excellentcrates.animation.style.simpleroll.SimpleRollSettings;
import su.nightexpress.excellentcrates.animation.style.simpleroll.SimpleRollProvider;
import su.nightexpress.excellentcrates.animation.style.simpleroll.codec.SimpleRollSettingsCodec;
import su.nightexpress.excellentcrates.animation.visibility.AnimationVisibility;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.CodecRegistry;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnimationSettingsTest {

    @TempDir
    Path directory;

    @BeforeAll
    static void initializeCodecs() {
        ConfigCodecs.init(new CodecRegistry());
        ConfigCodecs.register(SimpleRollSettings.class, SimpleRollSettingsCodec.INSTANCE);
        ConfigCodecs.register(CSGOAnimationSettings.class, CSGOAnimationSettingsCodec.INSTANCE);
    }

    @Test
    void oldProfilesKeepTheirValuesAndDefaultToPublic() throws Exception {
        FileConfig simple = FileConfig.load(this.directory.resolve("simple.yml"));
        simple.set("settings.name", "Custom Simple");
        simple.set("settings.roll_amount", 7);
        simple.set("settings.roll_interval", 3);
        simple.set("settings.finish_delay", 18);
        SimpleRollSettings simpleSettings = SimpleRollSettingsCodec.INSTANCE.read(simple, "settings");
        assertEquals(AnimationVisibility.PUBLIC, simpleSettings.getVisibility());
        assertEquals("Custom Simple", simpleSettings.getName());
        assertEquals(7, simpleSettings.getRollAmount());
        assertEquals(3, simpleSettings.getRollInterval());
        assertEquals(18, simpleSettings.getFinishDelay());
        assertEquals("PUBLIC", simple.getString("settings.visibility"));

        FileConfig csgo = FileConfig.load(this.directory.resolve("csgo.yml"));
        csgo.set("settings.name", "Custom CSGO");
        csgo.set("settings.total_rolls", 21);
        csgo.set("settings.duration_ticks", 73L);
        csgo.set("settings.finish_tick_delay", 31L);
        assertEquals(new CSGOAnimationSettings("Custom CSGO", 21, 73L, 31L),
            CSGOAnimationSettingsCodec.INSTANCE.read(csgo, "settings"));
        assertEquals("PUBLIC", csgo.getString("settings.visibility"));
    }

    @ParameterizedTest
    @EnumSource(AnimationVisibility.class)
    void visibilityAndCustomSettingsSurviveSerializationAndReload(AnimationVisibility visibility) throws Exception {
        Path simplePath = this.directory.resolve("simple.yml");
        FileConfig simple = FileConfig.load(simplePath);
        SimpleRollSettingsCodec.INSTANCE.write(simple, "settings",
            new SimpleRollSettings("Custom", 9, 4, 23, visibility));
        simple.saveChanges();
        SimpleRollSettings simpleReloaded = SimpleRollSettingsCodec.INSTANCE.read(FileConfig.load(simplePath), "settings");
        assertEquals(visibility, simpleReloaded.getVisibility());
        assertEquals("Custom", simpleReloaded.getName());
        assertEquals(9, simpleReloaded.getRollAmount());
        assertEquals(4, simpleReloaded.getRollInterval());
        assertEquals(23, simpleReloaded.getFinishDelay());

        Path csgoPath = this.directory.resolve("csgo.yml");
        FileConfig csgo = FileConfig.load(csgoPath);
        CSGOAnimationSettings settings = new CSGOAnimationSettings("Custom", 19, 80L, 35L, visibility);
        CSGOAnimationSettingsCodec.INSTANCE.write(csgo, "settings", settings);
        csgo.saveChanges();
        assertEquals(settings, CSGOAnimationSettingsCodec.INSTANCE.read(FileConfig.load(csgoPath), "settings"));
    }

    @Test
    void defaultAndLegacyConstructorsRemainPublic() {
        assertEquals(AnimationVisibility.PUBLIC, SimpleRollSettings.defaultSettings().getVisibility());
        assertEquals(AnimationVisibility.PUBLIC, CSGOAnimationSettings.defaultSettings().visibility());
        assertEquals(AnimationVisibility.PUBLIC, new SimpleRollSettings("Old", 1, 2, 3).getVisibility());
        assertEquals(AnimationVisibility.PUBLIC, new CSGOAnimationSettings("Old", 1, 2, 3).visibility());
    }

    @Test
    void generatedProfilesIncludePublicVisibility() {
        FileConfig simple = FileConfig.load(this.directory.resolve("simple-default.yml"));
        new SimpleRollProvider(mock(org.bukkit.plugin.Plugin.class)).writeDefaultProfile(null, simple);
        assertEquals("PUBLIC", simple.getString("settings.visibility"));
        assertEquals(12, simple.getInt("settings.roll_amount"));
        FileConfig csgo = FileConfig.load(this.directory.resolve("csgo-default.yml"));
        new CSGOAnimationProvider(mock(org.bukkit.plugin.Plugin.class)).writeDefaultProfile(null, csgo);
        assertEquals("PUBLIC", csgo.getString("settings.visibility"));
        assertEquals(16, csgo.getInt("settings.total_rolls"));
    }

    @ParameterizedTest
    @CsvSource({"private, PRIVATE", "invalid, PUBLIC"})
    void bothProfilesUseTheEstablishedEnumParsingAndFallback(String configured, AnimationVisibility expected) throws Exception {
        FileConfig config = FileConfig.load(this.directory.resolve("visibility.yml"));
        config.set("settings.visibility", configured);
        assertEquals(expected, SimpleRollSettingsCodec.INSTANCE.read(config, "settings").getVisibility());
        config.set("settings.visibility", configured);
        assertEquals(expected, CSGOAnimationSettingsCodec.INSTANCE.read(config, "settings").visibility());
    }
}
