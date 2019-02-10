package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Accessors(chain = true)
public class SelectionBox extends Button {
    @Getter
    @Setter
    protected String text;

    @Getter
    @Setter
    protected int color;

    @Getter
    @Setter
    protected boolean isSelected = false;

    @Getter
    @Setter
    protected SelectEvent selectEvent;

    public SelectionBox(String text) {
        super(DefaultFontProvider.INSTANCE.getStringWidth(text) + 10, 8);
        this.text = text;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        DefaultFontProvider.INSTANCE.drawString(text, getX(), getY(), color, false);
        float boxStartX = getX() + DefaultFontProvider.INSTANCE.getStringWidth(text) + 2F;
        if (isSelected()) Rect.draw(boxStartX + 1.2F, getY() + 1.5F + 1.2F, boxStartX - 1.5F - 1.2F + height, getY() - 1.2F + height, 0xFFcea119);

        Rect.drawFrame(boxStartX, getY() + 1.5F, boxStartX - 1.5F + height, getY() + height, 0.6F, color);

        drawComponentBox();
    }

    @Override
    public void onClick(int mouseButton, int mouseX, int mouseY) {
        setSelected(!isSelected());
        if (selectEvent != null) setSelected(selectEvent.onClick(isSelected(), mouseButton, mouseX, mouseY));
    }


    @FunctionalInterface
    public interface SelectEvent {
        boolean onClick(boolean isSelected, int mouseButton, int mouseX, int mouseY);
    }
}
