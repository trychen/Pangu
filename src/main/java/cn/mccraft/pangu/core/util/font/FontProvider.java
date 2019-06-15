package cn.mccraft.pangu.core.util.font;

import cn.mccraft.pangu.core.client.ui.meta.Alignment;

public interface FontProvider {
    int getStringWidth(String text);

    int drawString(String text, float x, float y, int color, boolean shadow);

    String trimStringToWidth(String text, int width, boolean reverse);

    int getFontHeight();

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

    default int drawString(String text, float x, float y, float parentWidth, int color, boolean shadow, Alignment alignment) {
        if (alignment == Alignment.LEADING) {
            return drawString(text, x, y, color, shadow);
        } else if (alignment == Alignment.CENTER) {
            return drawCenteredString(text, x + parentWidth / 2, y, color, shadow);
        } else if (alignment == Alignment.ENDING) {
            return drawString(text, x + parentWidth - getStringWidth(text), y, color, shadow);
        }
        return -1;
    }
}
