package cn.mccraft.pangu.core.client.ui.meta;

import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.util.ResourceLocation;

@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
@Builder
public class Style {
    private final ResourceLocation texture;
    private final int x, y, width, height, textOffset, fontColor, hoverFontColor, disabledFontColor;
    private final boolean fontShadow;
}
