package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public MultiLineText(int width, List<String> content) {
        setSize(width, content.size() * 12);
        setContent(content);
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        for (int i = 0; i < content.size(); i++) {
            DefaultFontProvider.INSTANCE.drawString(content.get(i), getX(), getY() + i * 12, color, false);
        }
    }

    public MultiLineText setContent(List<String> content) {
        this.content = resizeContent(content, (int) width);
        return this;
    }

    public static List<String> resizeContent(List<String> lines, int maxTextLength) {
        List<ITextComponent> ret = new ArrayList<>();
        for (String line : lines) {
            if (line == null) {
                ret.add(null);
                continue;
            }

            ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
            if (maxTextLength >= 0) {
                ret.addAll(GuiUtilRenderComponents.splitText(chat, maxTextLength, Minecraft.getMinecraft().fontRenderer, false, true));
            }
        }
        return ret.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList());
    }
}
