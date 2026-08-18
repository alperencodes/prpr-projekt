import processing.core.PApplet;

public class Game extends PApplet {
    @Override
    public void settings() {
        size(640, 360);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        background(67);
    }

    public static void main(String[] args) {
        PApplet.main(Game.class, args);
    }
}
