package cn.mccraft.pangu.core.client.ui.meta;

import cn.mccraft.pangu.core.util.render.Rect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import net.minecraft.util.ResourceLocation;

@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
@Builder
public class Icon {
    private final ResourceLocation texture;
    private final int u, v;

    @Builder.Default
    private final int width = 11, height = 11;

    public void draw(float x, float y) {
        Rect.startDrawing();
        Rect.bind(texture);
        Rect.drawTextured(
                x, y,
                u, v,
                width, height);
    }

    public void drawCentered(float x, float y) {
        draw(x - width / 2, y - height / 2);
    }
}
