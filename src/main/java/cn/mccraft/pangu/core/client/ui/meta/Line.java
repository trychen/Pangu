package cn.mccraft.pangu.core.client.ui.meta;


import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Data
@Accessors(chain = true, fluent = true)
@RequiredArgsConstructor(staticName = "of")
public class Line {
    @NonNull
    protected String text;

    protected FontProvider font = DefaultFontProvider.INSTANCE;

    protected boolean dropShadow;

    protected int color = 0xFFFFFF;

    protected Alignment alignment = Alignment.LEADING;

    @SideOnly(Side.CLIENT)
    public void draw(float x, float y, float parentWidth) {
        font.drawString(text, x, y, parentWidth, color, dropShadow, alignment);
    }
}