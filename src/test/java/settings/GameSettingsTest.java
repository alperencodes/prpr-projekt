package settings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameSettingsTest {
    @Test
    void usesExplicitDefaults() {
        GameSettings settings = new GameSettings();

        assertEquals(0.3f, GameSettings.DEFAULT_SONG_VOLUME);
        assertEquals(GameSettings.DEFAULT_SONG_VOLUME, settings.getSongVolume());
        assertFalse(settings.isShowFps());
    }

    @Test
    void clampsSongVolume() {
        GameSettings settings = new GameSettings();

        settings.setSongVolume(-0.5f);
        assertEquals(0.0f, settings.getSongVolume());
        settings.setSongVolume(1.5f);
        assertEquals(1.0f, settings.getSongVolume());
        settings.setSongVolume(Float.NaN);
        assertEquals(GameSettings.DEFAULT_SONG_VOLUME, settings.getSongVolume());
    }

    @Test
    void resetRestoresDefaults() {
        GameSettings settings = new GameSettings();
        settings.setSongVolume(0.2f);
        settings.setShowFps(true);

        settings.resetToDefaults();

        assertEquals(GameSettings.DEFAULT_SONG_VOLUME, settings.getSongVolume());
        assertEquals(GameSettings.DEFAULT_SHOW_FPS, settings.isShowFps());
    }
}
