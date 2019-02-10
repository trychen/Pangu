package cn.mccraft.pangu.core.util.font;

public interface FontProvider {
    int getStringWidth(String text);

    int drawString(String text, float x, float y, int color, boolean shadow);

    default int drawString(String text, float x, float y, int color, boolean shadow, boolean centered) {
        float half = centered ? getStringWidth(text) / 2 : 0;
        return drawString(text, x - half, y, color, shadow);
    }

    default int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    default int drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        float half = getStringWidth(text) / 2;
        return drawString(text, x - half, y, color, shadow);
    }
}
