package cn.mccraft.pangu.core.client.toast;

import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

@SideOnly(Side.CLIENT)
public class PanguToast implements IToast {
    @Getter
    @Setter
    protected ToastData info;
    protected IToast.Visibility visibility = IToast.Visibility.SHOW;
    protected long firstDrawTime;
    protected boolean newDisplay;
    protected long lastDelta;
    protected float displayedProgress;

    @Getter
    @Setter
    protected float currentProgress = 0.5f;

    public PanguToast(ToastData info) {
        this.info = info;
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }

        Rect.startDrawing();

        drawIcon(toastGui, delta);

        drawText();

        drawProgress(delta);

        return info.getDuration() <= 0 || delta - this.firstDrawTime < info.getDuration() ? visibility : Visibility.HIDE;
    }

    protected void drawIcon(GuiToast toastGui, long delta) {
        info.getStyle().draw();
        if (info.getItemStacks() != null && !info.getItemStacks().isEmpty()) {
            RenderHelper.enableGUIStandardItemLighting();
            toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, this.info.getItemStacks().get((int) (delta * (long) this.info.getItemStacks().size() / 5000L % (long) this.info.getItemStacks().size())), 8, 8);
        } else if (info.getImageIcon() != null) {
            Rect.bind(info.getImageIcon());
            Rect.drawFullTexTextured(6, 6, 20, 20);
        } else {
            info.getIcon().draw(6, 6);
        }
    }

    protected void drawText() {
        int textStart = info.getStyle() == ToastStyle.PRIMARY_WARNING ? 18 : 30;
        if (StringUtils.isEmpty(info.getSubtitle())) {
            DefaultFontProvider.INSTANCE.drawString(info.getTitle(), textStart, 10, info.getStyle().getTitleColor(), false);
        } else {
            DefaultFontProvider.INSTANCE.drawString(info.getTitle(), textStart, 6F, info.getStyle().getTitleColor(), false);
            DefaultFontProvider.INSTANCE.drawString(info.getSubtitle(), textStart, 16.5F, info.getStyle().getSubtitleColor(), false);
        }
    }

    protected void drawProgress(long delta) {
        if (info.isProgress()) {
            if (info.isCustomProgress()) {
                Gui.drawRect(3, 28, 157, 29, -1);
                float f = (float) MathHelper.clampedLerp((double)this.displayedProgress, (double)this.currentProgress, (double)((float)(delta - this.lastDelta) / 100.0F));
                int i;

                if (this.currentProgress >= this.displayedProgress) {
                    i = -16755456;
                } else {
                    i = -11206656;
                }

                Gui.drawRect(3, 28, (int)(3.0F + 154.0F * f), 29, i);
                this.displayedProgress = f;
                this.lastDelta = delta;
            } else {
                Rect.draw(3, 28, 157, 29, -1);
                Rect.draw(3, 28, (int) (3.0F + 154.0F * Math.min((float) (delta - firstDrawTime) / info.getDuration(), 1F)), 29, 0xff66a6ff);
            }
        }
    }

    @Override
    public Object getType() {
        return StringUtils.isEmpty(info.getKey()) ? IToast.NO_TOKEN : info.getKey();
    }

    public void hide() {
        this.visibility = IToast.Visibility.HIDE;
    }
}
