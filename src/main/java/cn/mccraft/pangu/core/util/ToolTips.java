package cn.mccraft.pangu.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

import static com.mojang.realmsclient.gui.ChatFormatting.WHITE;

/**
 * @author trychen
 * @since 1.8.2
 */
public interface ToolTips {
    /**
     * 添加可以 Shift 显示的 Lore
     *
     * @param lores  addInformation 的arg3 的列表
     * @param def    未按下shift时显示的内容
     * @param shifts 按下shift后显示的内容
     */
    static void shiftLore(List<String> lores, String[] def, String[] shifts) {
        lores.addAll(Arrays.asList(def));

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            for (String s : shifts)
                lores.add(WHITE + s);
        else
            lores.add(WHITE + I18n.format("gui.inventory.shiftfordetail"));
    }

    /**
     * 添加可以 Shift 显示的 Lore
     *
     * @param lores    addInformation 的 arg3 的列表
     * @param itemName 未按下shift时显示的内容
     */
    @SuppressWarnings("Duplicates")
    static void shiftLoreWithI18n(List<String> lores, String itemName) {
        if (I18n.hasKey(itemName + ".lore"))
            lores.add(I18n.format(itemName + ".lore"));
        else {
            int i = 1;
            while (I18n.hasKey(itemName + ".lore." + i)) {
                lores.add(I18n.format(itemName + ".lore." + i));
                i++;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (I18n.hasKey(itemName + ".hidelore")) {
                lores.add("");
                lores.add(WHITE + I18n.format(itemName + ".hidelore"));
            } else {
                int i = 1;
                while (I18n.hasKey(itemName + ".hidelore." + i)) {
                    if (i == 1) lores.add("");
                    lores.add(WHITE + I18n.format(itemName + ".hidelore." + i));
                    i++;
                }
            }
        } else if (I18n.hasKey(itemName + ".hidelore") || I18n.hasKey(itemName + ".hidelore.1")) {
            lores.add("");
            lores.add(I18n.format("gui.inventory.shiftfordetail"));
        }
    }

    /**
     * Get item stack's tooltips
     */
    static List<String> get(ItemStack item) {
        List<String> list = item.getTooltip(Games.player(), Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

        for (int i = 0; i < list.size(); ++i) {
            list.set(i, (i == 0 ? item.getRarity().color : TextFormatting.GRAY) + list.get(i));
        }

        return list;
    }
}
