package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.UI;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Accessors(chain = true)
public class MultiLineText extends Component {
    @Getter
    protected List<String> content;

    @Getter
    @Setter
    protected int color = 0xFFFFFF;

    @Getter
    @Setter
    protected boolean dropShadow = false;


    @Getter
    @Setter
    protected int padding = 3;

    @Getter
    @Setter
    protected FontProvider font;

    public MultiLineText(float width, List<String> content, FontProvider font) {
        setSize(width, 0);
        setFont(font);
        setContent(content);
        setSize(width, this.content.size() * (font.getFontHeight() + getPadding()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        for (int i = 0; i < getContent().size(); i++) {
            getFont().drawString(getContent().get(i), getX(), getY() + i * (getFont().getFontHeight() + getPadding()), getColor(), false);
        }
    }

    public MultiLineText setContent(List<String> content) {
        this.content = UI.resizeStringList(font, content, (int) width);
        return this;
    }
}
