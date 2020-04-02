package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.ui.builtin.*;
import cn.mccraft.pangu.core.client.ui.meta.Line;
import cn.mccraft.pangu.core.client.ui.stack.HStack;
import cn.mccraft.pangu.core.client.ui.stack.VStack;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UI Helper
 */
public interface UI {
    static TextButton button(String text) {
        return new TextButton(text);
    }

    static Label text(String text) {
        return new Label(text);
    }
    static SelectionBox selection(String text) {
        return new SelectionBox(text);
    }

    static TextField input(float width, float height) {
        return new TextField(width, height);
    }

    static Button ofYesButton() {
        return button(I18n.format("gui.yes")).setStyle(TextButton.PRIMARY);
    }

    static Button ofNoButton() {
        return button(I18n.format("gui.no")).setStyle(TextButton.WHITE);
    }

    static ScrollingList ofStringList(int width, int height, List<Object> datas) {
        return new StringScrollingList(width, height, datas.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    static List<String> resizeStringList(List<String> lines, int maxTextLength) {
        return resizeStringList(DefaultFontProvider.INSTANCE, lines, maxTextLength);
    }

    static Stack horizontal(Screen screen, Component... components) {
        return new HStack(screen).addComponents(components);
    }

    static Stack vertical(Screen screen, Component... components) {
        return new VStack(screen).addComponents(components);
    }

    static List<String> resizeStringList(FontProvider font, List<String> lines, int maxTextLength) {
        if (maxTextLength <= 0) return lines;
        List<ITextComponent> ret = new ArrayList<>();
        for (String line : lines) {
            if (line == null) {
                ret.add(new TextComponentString(""));
                continue;
            }

            ret.addAll(splitText(new TextComponentString(line), maxTextLength, font, false, true));
        }
        return ret.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList());
    }

    static String removeTextColorsIfConfigured(String text, boolean forceColor) {
        return !forceColor && !Minecraft.getMinecraft().gameSettings.chatColours ? TextFormatting.getTextWithoutFormattingCodes(text) : text;
    }

    static List<ITextComponent> splitText(ITextComponent textComponent, int maxTextLength, FontProvider font, boolean p_178908_3_, boolean forceTextColor) {
//        List<ITextComponent> result = Lists.newArrayList(textComponent);
//        for (int i = 0; i < result.size(); i++) {
//            ITextComponent current = result.get(i);
//            String plain = current.getUnformattedComponentText();
//
//            if (plain.contains("\n")) {
//
//            }
//        }

        int i = 0;
        ITextComponent itextcomponent = new TextComponentString("");
        List<ITextComponent> list = Lists.newArrayList();
        List<ITextComponent> list1 = Lists.newArrayList(textComponent);

        for (int j = 0; j < list1.size(); ++j) {
            ITextComponent itextcomponent1 = list1.get(j);
            String s = itextcomponent1.getUnformattedComponentText();
            boolean flag = false;

            if (s.contains("\n")) {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                ITextComponent itextcomponent2 = new TextComponentString(s1);
                itextcomponent2.setStyle(itextcomponent1.getStyle().createShallowCopy());
                list1.add(j + 1, itextcomponent2);
                flag = true;
            }

            String s4 = removeTextColorsIfConfigured(itextcomponent1.getStyle().getFormattingCode() + s, forceTextColor);
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = font.getStringWidth(s5);
            TextComponentString textcomponentstring = new TextComponentString(s5);
            textcomponentstring.setStyle(itextcomponent1.getStyle().createShallowCopy());

            if (i + i1 > maxTextLength) {
                String s2 = font.trimStringToWidth(s4, maxTextLength - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && !s3.isEmpty()) {
                    int l = s2.lastIndexOf(32);

                    if (l >= 0 && font.getStringWidth(s4.substring(0, l)) > 0) {
                        s2 = s4.substring(0, l);

                        if (p_178908_3_) {
                            ++l;
                        }

                        s3 = s4.substring(l);
                    } else if (i > 0 && !s4.contains(" ")) {
                        s2 = "";
                        s3 = s4;
                    }

                    s3 = FontRenderer.getFormatFromString(s2) + s3; //Forge: Fix chat formatting not surviving line wrapping.

                    TextComponentString textcomponentstring1 = new TextComponentString(s3);
                    textcomponentstring1.setStyle(itextcomponent1.getStyle().createShallowCopy());
                    list1.add(j + 1, textcomponentstring1);
                }

                i1 = font.getStringWidth(s2);
                textcomponentstring = new TextComponentString(s2);
                textcomponentstring.setStyle(itextcomponent1.getStyle().createShallowCopy());
                flag = true;
            }

            if (i + i1 <= maxTextLength) {
                i += i1;
                itextcomponent.appendSibling(textcomponentstring);
            } else {
                flag = true;
            }

            if (flag) {
                list.add(itextcomponent);
                i = 0;
                itextcomponent = new TextComponentString("");
            }
        }

        list.add(itextcomponent);
        return list;
    }
}
