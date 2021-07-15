package cn.mccraft.pangu.core.util.font;

import cn.mccraft.pangu.core.client.ui.meta.Alignment;

public interface FontProvider {
    FontProvider DEFAULT = DefaultFontProvider.INSTANCE;

    int getStringWidth(String text);

    int drawString(String text, float x, float y, int color, boolean shadow);

    String trimStringToWidth(String text, int width, boolean reverse);

    int getFontHeight();

    default int drawString(String text, float x, float y, int color, boolean shadow, boolean centered) {
        float half = centered ? getStringWidth(text) / 2F : 0;
        return drawString(text, x - half, y, color, shadow);
    }

    default int drawString(String text, float x, float y) {
        return drawString(text, x, y, 0xFF000000, false);
    }

    default int drawString(String text, float x, float y, int color) {
        return drawString(text, x, y, color, false);
    }

    default int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    default int drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        float half = getStringWidth(text) / 2F;
        return drawString(text, x - half, y, color, shadow);
    }

    default int drawCenteredString(String text, float x, float y, int color) {
        return this.drawCenteredString(text, x, y, color, false);
    }

    default int drawCenteredString(String text, float x, float y) {
        return this.drawCenteredString(text, x, y, 0xFF000000, false);
    }

    default int drawEndingString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x - getStringWidth(text), y, color, shadow);
    }

    default int drawEndingString(String text, float x, float y, int color) {
        return this.drawEndingString(text, x, y, color, false);
    }

    default int drawEndingString(String text, float x, float y) {
        return this.drawEndingString(text, x, y, 0xFF000000, false);
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
