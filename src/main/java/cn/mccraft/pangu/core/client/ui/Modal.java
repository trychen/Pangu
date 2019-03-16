package cn.mccraft.pangu.core.client.ui;

public class Modal extends Container {
    public Modal(Screen screen) {
        super(screen.width, screen.height);
        setScreen(screen);
    }

    public void closeModal() {
        getScreen().setModal(null);
    }
}
