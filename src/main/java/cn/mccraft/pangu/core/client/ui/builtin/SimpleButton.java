package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

public class SimpleButton extends Button {
    @Getter
    @Setter
    protected ResourceLocation texture;

    @Getter
    @Setter
    protected float textureWidth, textureHeight;

    public SimpleButton(ResourceLocation texture, float width, float height) {
        this(texture, width, height, width, height);
    }


    public SimpleButton(ResourceLocation texture, float width, float height, float textureWidth, float textureHeight) {
        super(width, height);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.startDrawing();
        Rect.bind(getTexture());
        Rect.textureFiltering();
        Rect.drawCustomSizeTextured(getX(), getY(), isHovered() ? textureWidth : 0, 0, textureWidth, textureHeight, getWidth(), getHeight());
    }
}
