package cn.mccraft.pangu.core.client.toast;

import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ToastStyle {
    DARK(0, -256, -1),
    WHITE(1, -11534256, -16777216),
    PRIMARY_WARNING(2, -256, -1),
    WHITE_RECTANGLE(3, -11534256, -16777216);

    @Getter
    private final int index, titleColor, subtitleColor;

    ToastStyle(int index, int titleColor, int subtitleColor) {
        this.index = index;
        this.titleColor = titleColor;
        this.subtitleColor = subtitleColor;
    }

    @SideOnly(Side.CLIENT)
    public void draw() {
        Rect.bind(IToast.TEXTURE_TOASTS);
        Rect.drawTextured(0, 0, 0, this.index * 32, 160, 32);
    }
}
