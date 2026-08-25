package app.screen;

import controlP5.ControlP5;
import controlP5.Group;

// this class serves as a base for different screens in the application,
// providing common functionality for showing and hiding UI elements.
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
    public void draw() {}
}
