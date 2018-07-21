package cn.mccraft.pangu.core.client.gui;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class PanguToast implements IToast {
    public static final Object PG_INFO_TOAST_TOKEN = new Object();

    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;

    public PanguToast(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    @Nonnull
    public Visibility draw(@Nonnull GuiToast toast, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }

        toast.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toast.drawTexturedModalRect(0, 0, 0, 64, 160, 32);

        if (this.subtitle == null) {
            toast.getMinecraft().fontRenderer.drawString(this.title, 18, 12, -256);
        } else {
            toast.getMinecraft().fontRenderer.drawString(this.title, 18, 7, -256);
            toast.getMinecraft().fontRenderer.drawString(this.subtitle, 18, 18, -1);
        }

        return delta - this.firstDrawTime < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }

    @Override
    @Nonnull
    public Object getType() {
        return PG_INFO_TOAST_TOKEN;
    }
}
