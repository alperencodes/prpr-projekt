package app;

public enum GameState {
    MENU("main menu"),
    GAME_CONFIG("game configuration"),
    PLAYING("clicking some buttons"),
    RESULTS("results"),
    TUTORIAL("tutorial"),
    HIGHSCORES("highscores"),
    SETTINGS("settings");

    private final String screenName;

    GameState(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenName() {
        return screenName;
    }
}
