package cn.mccraft.pangu.core.util.font;

public interface FontProvider {
    int getStringWidth(String text);
    int drawString(String text, float x, float y, int color, boolean shadow);
    int drawStringWithShadow(String text, float x, float y, int color);
    int drawCenteredString(String text, float x, float y, int color, boolean shadow);
}
