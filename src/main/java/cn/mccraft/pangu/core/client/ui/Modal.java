package cn.mccraft.pangu.core.client.ui;

import javax.annotation.Nonnull;

public class Modal extends Container {
    public Modal(Screen screen) {
        super(screen.width, screen.height);
        setScreen(screen);
    }

    public void init() {
    }

    public void close() {
        getScreen().setModal(null);
    }
}
