package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.util.debug.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.client.PGClient.PG_TOOLTIPS_TEXTURE;

@SideOnly(Side.CLIENT)
@AutoWired(registerCommonEventBus = true)
public enum ToolTipRenderer {
    INSTANCE;

    protected String text = "";
    protected int time;
    protected ToolTipStyle style = ToolTipStyle.NONE;

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            if (time > 0) {
                GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                if (gui == null) return;
                int scaledWidth = event.getResolution().getScaledWidth();
                int scaledHeight = event.getResolution().getScaledHeight();

                FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
                TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

                int textWidth = fontRenderer.getStringWidth(this.text);
                int extendWidth = 10;
                int width = textWidth + extendWidth;

                int x = -textWidth / 2;

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (scaledWidth / 2), (float) (scaledHeight - 62), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

                fontRenderer.drawString(this.text, - textWidth / 2, style.getTextOffset(), style.getFontColor());

                if (style != null && style != ToolTipStyle.NONE) {
                    textureManager.bindTexture(PG_TOOLTIPS_TEXTURE);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                    drawTexturedModalRect(
                            x - extendWidth / 2, 0,
                            style.getX(), style.getY(),
                            width / 2, style.getHeight());

                    drawTexturedModalRect(
                            x + textWidth / 2, 0,
                            style.getWidth() - width / 2, style.getY(),
                            width / 2, style.getHeight());
                }

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (time > 0) {
            time--;
        }
    }

    /**
     * Display ToolTip, will replace the previous one if exists.
     * ToolTip text width (from {@code FontRenderer.getStringWidth(String text)})
     * cannot be longer than 387. If text width is longer than 387, the beyond part
     * will be cut.
     */
    public void set(ToolTip toolTip) {
        Message.actionBar(toolTip.getText());
        text = fixStringWidth(toolTip.getText());
        style = toolTip.getStyle();
        time = 60;
    }

    @SubscribeEvent
    public void render(ServerChatEvent event) {
        PanguCore.getNetwork().sendTo(new ToolTip(event.getMessage(), ToolTipStyle.TRANSPARENT), event.getPlayer());
    }

    /**
     * Cut string into a displayable size
     */
    public String fixStringWidth(String text) {
        return fixStringWidth(text, 1);
    }
    /**
     * Cut string into a displayable size
     */
    public String fixStringWidth(String text, int level) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        if (fontRenderer.getStringWidth(text) > 387) {
            if (text.length() > 129)
                return fixStringWidth(text.substring(0, 129));
            else
                return fixStringWidth(text.substring(0, text.length() - level), level + 1);
        }
        return text;
    }

    public static void drawTexturedModalRect(int para1, int para2, int para3, int para4, int para5, int para6) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        int zLevel = -1;

        buffer.pos((double) para1, (double) (para2 + para6), (double) zLevel).tex((double) ((float) (para3) * 0.00390625F), (double) ((float) (para4 + para6) * 0.00390625F)).endVertex();
        buffer.pos((double) (para1 + para5), (double) (para2 + para6), (double) zLevel).tex((double) ((float) (para3 + para5) * 0.00390625F), (double) ((float) (para4 + para6) * 0.00390625F)).endVertex();
        buffer.pos((double) (para1 + para5), (double) para2, (double) zLevel).tex((double) ((float) (para3 + para5) * 0.00390625F), (double) ((float) (para4) * 0.00390625F)).endVertex();
        buffer.pos((double) para1, (double) para2, (double) zLevel).tex((double) ((float) (para3) * 0.00390625F), (double) ((float) (para4) * 0.00390625F)).endVertex();

        tessellator.draw();
    }
}
