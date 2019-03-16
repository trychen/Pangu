package cn.mccraft.pangu.core.client.ui;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;

@Accessors(chain = true)
public abstract class Button extends Component {
    @Setter
    @Getter
    private boolean isPlayPressSound = true;

    @Setter
    @Getter
    private boolean clickToClose = false;

    @Getter
    @Setter
    @Deprecated
    private ClickEvent clickEvent;

    public Button(int width, int height) {
        super();
        setSize(width, height);
    }

    public void onClick(int mouseButton, int mouseX, int mouseY) {
        if (isDisabled()) return;
        if (isPlayPressSound()) playPressSound();
        if (getButtonClickEvent() != null) getButtonClickEvent().accept(new ButtonClickEvent(this, mouseButton, mouseX, mouseY));
        if (getClickEvent() != null) getClickEvent().onClick(mouseButton, mouseX, mouseY);
        if (isClickToClose() && getScreen() != null) getScreen().closeScreen();
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        if (actionOnReleased(mouseX, mouseY)) return;
        this.onClick(mouseButton, mouseX, mouseY);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        if (!actionOnReleased(mouseX, mouseY)) return;
        this.onClick(0, mouseX, mouseY);
    }

    public boolean actionOnReleased(int mouseX, int mouseY) {
        return true;
    }

    /**
     * @return 0 = disabled, 1 = normal, 2 = hovered
     */
    public int getHoverState() {
        int i = 1;
        if (isDisabled()) i = 0;
        else if (isHovered()) i = 2;
        return i;
    }

    @FunctionalInterface
    public interface ClickEvent {
        void onClick(int mouseButton, int mouseX, int mouseY);
    }

    @Getter
    protected Consumer<ButtonClickEvent> buttonClickEvent;

    public void onButtonClick(Consumer<ButtonClickEvent> consumer) {
        buttonClickEvent = consumer;
    }

    @Data
    public static class ButtonClickEvent {
        @NonNull
        private Button button;

        @NonNull
        private int mouseButton, mouseX, mouseY;
    }
}
