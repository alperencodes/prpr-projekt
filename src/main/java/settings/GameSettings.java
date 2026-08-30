package settings;

public class GameSettings {
    public static final float DEFAULT_SONG_VOLUME = 0.8f;
    public static final boolean DEFAULT_SHOW_FPS = false;

    private float songVolume;
    private boolean showFps;

    public GameSettings() {
        resetToDefaults();
    }

    public float getSongVolume() {
        return songVolume;
    }

    public void setSongVolume(float songVolume) {
        this.songVolume = Float.isFinite(songVolume)
                ? Math.clamp(songVolume, 0.0f, 1.0f)
                : DEFAULT_SONG_VOLUME;
    }

    public boolean isShowFps() {
        return showFps;
    }

    public void setShowFps(boolean showFps) {
        this.showFps = showFps;
    }

    public void resetToDefaults() {
        songVolume = DEFAULT_SONG_VOLUME;
        showFps = DEFAULT_SHOW_FPS;
    }
}
