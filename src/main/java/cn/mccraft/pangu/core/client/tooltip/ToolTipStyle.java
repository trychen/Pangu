package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.client.gui.Style;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

import static cn.mccraft.pangu.core.client.PGClient.PG_TOOLTIPS_TEXTURE;

public class ToolTipStyle extends Style {
    private static final Map<String, ToolTipStyle> name2Style = Maps.newHashMap();

    public static final ToolTipStyle NONE = of("NONE", null, 0, 0, 0, 0, 0, 0xFFFFFF, false);
    public static final ToolTipStyle NORMAL = of("NORMAL", PG_TOOLTIPS_TEXTURE, 0, 0, 200, 15, 3, 0xFFFFFF, false);
    public static final ToolTipStyle TRANSPARENT = of("TRANSPARENT", PG_TOOLTIPS_TEXTURE, 0, 15, 200, 15, 3, 0xFFFFFF, false);

    private final String name;

    protected ToolTipStyle(String name, ResourceLocation texture, int x, int y, int width, int height, int textOffset, int fontColor, boolean fontShadow) {
        super(texture, x, y, width, height, textOffset, fontColor, 0, 0, fontShadow);
        this.name = name;
    }

    public static ToolTipStyle of(String name, ResourceLocation texture, int x, int y, int width, int height, int textOffset, int fontColor, boolean fontShadow) {
        ToolTipStyle toolTipStyle = new ToolTipStyle(name, texture, x, y, width, height, textOffset, fontColor, fontShadow);
        name2Style.put(name, toolTipStyle);
        return toolTipStyle;
    }

    public String getName() {
        return name;
    }

    @Nonnull
    public static ToolTipStyle valueOf(String name) {
        ToolTipStyle style = name2Style.get(name);
        return style == null ? NORMAL : style;
    }
}
