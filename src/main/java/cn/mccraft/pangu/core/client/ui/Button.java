package cn.mccraft.pangu.core.client.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Accessors(chain = true)
public abstract class Button extends Component {
    private boolean isPlayPressSound = true;

    @Getter
    @Setter
    private ClickEvent clickEvent;

    public Button(int width, int height) {
        super();
        setSize(width, height);
    }

    public void onClick(int mouseButton, int mouseX, int mouseY) {
        if (clickEvent != null) clickEvent.onClick(mouseButton, mouseX, mouseY);
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        if (isDisabled()) return;
        if (actionOnReleased(mouseX, mouseY)) return;
        if (isPlayPressSound) playPressSound();
        this.onClick(mouseButton, mouseX, mouseY);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        if (isDisabled()) return;
        if (!actionOnReleased(mouseX, mouseY)) return;
        if (isPlayPressSound) playPressSound();
        this.onClick(0, mouseX, mouseY);
    }

    public boolean actionOnReleased(int mouseX, int mouseY) {
        return true;
    }

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
}
