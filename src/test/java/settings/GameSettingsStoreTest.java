package settings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSettingsStoreTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void missingFileUsesDefaults() {
        GameSettings settings = new GameSettingsStore(temporaryDirectory.resolve("missing.properties")).load();

        assertEquals(GameSettings.DEFAULT_SONG_VOLUME, settings.getSongVolume());
        assertEquals(GameSettings.DEFAULT_SHOW_FPS, settings.isShowFps());
    }

    @Test
    void savedSettingsSurviveLoad() {
        GameSettingsStore store = new GameSettingsStore(temporaryDirectory.resolve("nested/settings.properties"));
        GameSettings settings = new GameSettings();
        settings.setSongVolume(0.35f);
        settings.setShowFps(true);

        store.save(settings);
        GameSettings loaded = store.load();

        assertEquals(0.35f, loaded.getSongVolume());
        assertTrue(loaded.isShowFps());
    }

    @Test
    void resetDefaultsCanBePersisted() {
        GameSettingsStore store = new GameSettingsStore(temporaryDirectory.resolve("settings.properties"));
        GameSettings settings = new GameSettings();
        settings.setSongVolume(0.1f);
        settings.setShowFps(true);
        settings.resetToDefaults();

        store.save(settings);
        GameSettings loaded = store.load();

        assertEquals(GameSettings.DEFAULT_SONG_VOLUME, loaded.getSongVolume());
        assertFalse(loaded.isShowFps());
    }
}
