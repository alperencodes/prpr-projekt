package ui;

import processing.core.PApplet;

public class Button {
    private float x;
    private float y;
    private float width;
    private float height;

    private int backgroundColor;
    private int textColor;
    private int borderColor;

    private String text;
    private int textSize;
    private float roundness;

    public Button() {}

    public Button(String text, float x, float y, float width, float height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = 0;
        this.textColor = 255;
        this.borderColor = 255;
        this.textSize = 20;
    }

    public void draw(PApplet app) {
        app.fill(backgroundColor);
        app.stroke(borderColor);
        app.rect(x, y, width, height, roundness);

        app.fill(textColor);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.text(text, x + width / 2, y + height / 2);
        app.textSize(textSize);
    }

    public boolean contains(float x, float y) {
        return x >= this.x
                && x <= this.x + width
                && y >= this.y
                && y <= this.y + height;
    }

    public void setRoundness(float roundness) {
        this.roundness = roundness;
    }
}
