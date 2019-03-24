package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.ui.builtin.ScrollingList;
import cn.mccraft.pangu.core.client.ui.builtin.StringScrollingList;
import cn.mccraft.pangu.core.client.ui.builtin.TextButton;
import cn.mccraft.pangu.core.client.ui.meta.Line;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UI Helper
 */
public interface UI {
    static Button ofYesButton() {
        return new TextButton(I18n.format("gui.yes")).setStyle(TextButton.PRIMARY);
    }

    static Button ofNoButton() {
        return new TextButton(I18n.format("gui.no")).setStyle(TextButton.WHITE);
    }

    static ScrollingList ofStringList(int width, int height, List<Object> datas) {
        return new StringScrollingList(width, height, datas.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    static List<String> resizeStringList(List<String> lines, int maxTextLength) {
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
