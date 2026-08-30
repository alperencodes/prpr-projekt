package settings;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class GameSettingsStore {
    private static final String SONG_VOLUME_KEY = "songVolume";
    private static final String SHOW_FPS_KEY = "showFps";

    private final Path settingsPath;

    public GameSettingsStore(Path settingsPath) {
        this.settingsPath = settingsPath;
    }

    public GameSettings load() {
        GameSettings settings = new GameSettings();
        if (!Files.exists(settingsPath)) {
            return settings;
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(settingsPath)) {
            properties.load(reader);
            loadSongVolume(properties, settings);
            loadShowFps(properties, settings);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Could not load settings; using defaults: " + exception.getMessage());
            settings.resetToDefaults();
        }
        return settings;
    }

    public void save(GameSettings settings) {
        Properties properties = new Properties();
        properties.setProperty(SONG_VOLUME_KEY, Float.toString(settings.getSongVolume()));
        properties.setProperty(SHOW_FPS_KEY, Boolean.toString(settings.isShowFps()));

        try {
            Path parent = settingsPath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(settingsPath)) {
                properties.store(writer, "Kaizen settings");
            }
        } catch (IOException exception) {
            System.err.println("Could not save settings: " + exception.getMessage());
        }
    }

    private void loadSongVolume(Properties properties, GameSettings settings) {
        String value = properties.getProperty(SONG_VOLUME_KEY);
        if (value != null) {
            settings.setSongVolume(Float.parseFloat(value));
        }
    }

    private void loadShowFps(Properties properties, GameSettings settings) {
        String value = properties.getProperty(SHOW_FPS_KEY);
        if (value == null) {
            return;
        }
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Invalid showFps value: " + value);
        }
        settings.setShowFps(Boolean.parseBoolean(value));
    }
}
