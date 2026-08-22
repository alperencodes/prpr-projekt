package settings;

import java.awt.*;

public class DisplayManager {
    private final int refreshRate;
    private final boolean isRateKnown;

    public DisplayManager() {
        DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDisplayMode();

        this.refreshRate = mode.getRefreshRate();
        this.isRateKnown = (this.refreshRate != DisplayMode.REFRESH_RATE_UNKNOWN);
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public boolean isRateKnown() {
        return isRateKnown;
    }
}
