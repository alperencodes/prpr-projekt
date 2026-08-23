package app.screen;

import controlP5.ControlP5;
import controlP5.Group;

public abstract class AbstractScreen implements Screen {
    protected final Group group;

    protected AbstractScreen(ControlP5 cp5, String groupName) {
        group = cp5.addGroup(groupName).setLabel("");
    }

    @Override
    public void show() {
        group.show();
    }

    @Override
    public void hide() {
        group.hide();
    }

    @Override
    public void draw() {
        // Most screens currently only contain ControlP5 components.
     }
}
