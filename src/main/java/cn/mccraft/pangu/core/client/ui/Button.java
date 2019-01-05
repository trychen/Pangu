package cn.mccraft.pangu.core.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Button extends Component {
    private boolean isPlayPressSound = true;

    public Button(int width, int height) {
        super();
        setSize(width, height);
    }

    public abstract void onClick(int mouseButton, int mouseX, int mouseY);

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        if (actionOnReleased(mouseX, mouseY)) return;
        if (isPlayPressSound) playPressSound();
        this.onClick(mouseButton, mouseX, mouseY);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        if (!actionOnReleased(mouseX, mouseY)) return;
        if (isPlayPressSound) playPressSound();
        this.onClick(0, mouseX, mouseY);
    }

    public void playPressSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
}
