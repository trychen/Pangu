package cn.mccraft.pangu.core.client.ui.meta;

import cn.mccraft.pangu.core.client.ui.Component;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;


public enum PositionAlignment {
    @SerializedName("center")
    CENTER,
    @SerializedName("top")
    TOP,
    @SerializedName("bottom")
    BOTTOM,
    @SerializedName("left")
    LEFT,
    @SerializedName("right")
    RIGHT,
    @SerializedName("top_left")
    TOP_LEFT,
    @SerializedName("bottom_left")
    BOTTOM_LEFT,
    @SerializedName("top_right")
    TOP_RIGHT,
    @SerializedName("bottom_right")
    BOTTOM_RIGHT;

    public static PositionAlignment of(String raw) {
        for (PositionAlignment value : values()) {
            if (value.name().equalsIgnoreCase(raw)) return value;
        }
        return null;
    }

    public void locate(Component component, float screenX, float screenY, float x, float y) {
        if (this == CENTER) {
            component.setCenteredPosition(screenX + x, screenY + y);
        } else if (this == TOP_LEFT) {
            component.setPosition(screenX + x, screenY + y);
        } else if (this == TOP_RIGHT) {
            component.setPosition(screenX + x - component.getWidth(), screenY + y);
        } else if (this == TOP) {
            component.setCenteredPosition(screenX + x, screenY + y + component.getHeight() / 2);
        } else if (this == BOTTOM) {
            component.setCenteredPosition(screenX + x, screenY + y - component.getHeight() / 2);
        } else if (this == LEFT) {
            component.setCenteredPosition(screenX + x + component.getWidth() / 2, screenY + y);
        } else if (this == RIGHT) {
            component.setCenteredPosition(screenX + x - component.getWidth() / 2, screenY + y);
        } else if (this == BOTTOM_LEFT) {
            component.setPosition(screenX + x, screenY + y - component.getHeight());
        } else if (this == BOTTOM_RIGHT) {
            component.setPosition(screenX + x - component.getWidth(), screenY + y - component.getHeight());
        }
    }

    public float[] offsetTo(float screenWidth, float screenHeight, float x, float y, float width, float height) {
        float[] result = new float[2];
        if (this == CENTER) {
            result[0] = (screenWidth + width) * 0.5F + x;
            result[1] = (screenHeight + height) * 0.5F + y;
        } else if (this == TOP_LEFT) {
            result[0] = x;
            result[1] = y;
        } else if (this == TOP_RIGHT) {
            result[0] = screenWidth - x - width;
            result[1] = y;
        } else if (this == TOP) {
            result[0] = (screenWidth - width) * 0.5F + x;
            result[1] = y;
        } else if (this == BOTTOM) {
            result[0] = (screenWidth - width) * 0.5F + x;
            result[1] = screenHeight - y - height;
        } else if (this == LEFT) {
            result[0] = x;
            result[1] = (screenHeight + height) * 0.5F + y;
        } else if (this == RIGHT) {
            result[0] = screenWidth - x - width;
            result[1] = (screenHeight + height) * 0.5F + y;
        } else if (this == BOTTOM_LEFT) {
            result[0] = x;
            result[1] = screenHeight - y - height;
        } else if (this == BOTTOM_RIGHT) {
            result[0] = screenWidth - x - width;
            result[1] = screenHeight - y - height;
        }
        return result;
    }

    public float getFocusX(float full) {
        if (this == CENTER) {
            return full / 2;
        } else if (this == TOP_LEFT) {
            return 0;
        } else if (this == TOP_RIGHT) {
            return full;
        } else if (this == TOP) {
            return full / 2;
        } else if (this == BOTTOM) {
            return full / 2;
        } else if (this == LEFT) {
            return 0;
        } else if (this == RIGHT) {
            return full;
        } else if (this == BOTTOM_LEFT) {
            return 0;
        } else if (this == BOTTOM_RIGHT) {
            return full;
        }
        return 0;
    }

    public float getFocusY(float full) {
        if (this == CENTER) {
            return full / 2;
        } else if (this == TOP_LEFT) {
            return 0;
        } else if (this == TOP_RIGHT) {
            return 0;
        } else if (this == TOP) {
            return 0;
        } else if (this == BOTTOM) {
            return full;
        } else if (this == LEFT) {
            return full / 2;
        } else if (this == RIGHT) {
            return full / 2;
        } else if (this == BOTTOM_LEFT) {
            return full;
        } else if (this == BOTTOM_RIGHT) {
            return full;
        }
        return 0;
    }
}
