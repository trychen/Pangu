package cn.mccraft.pangu.core.item;

import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author trychen
 * @since 1.0.0.5
 */
public class LoreHelper {
    /**
     * 添加可以Shift显示的Lore
     * @param lores addInformation 的arg3 的列表
     * @param def 未按下shift时显示的内容
     * @param shifts 按下shift后显示的内容
     */
    public static void shiftLore(List<String> lores, String[] def, String[] shifts){
        lores.addAll(Arrays.asList(def));
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            for (String s : shifts)
                lores.add(WHITE + s);
        else 
            lores.add(WHITE + I18n.format("gui.inventory.shiftfordetail"));
    }

    /**
     * 添加可以Shift显示的Lore
     * @param lores addInformation 的arg3 的列表
     * @param itemName 未按下shift时显示的内容
     */
    public static void shiftLoreWithI18n(List<String> lores, String itemName){
        if (I18n.hasKey(itemName + ".lore"))
            lores.add(I18n.format(itemName + ".lore"));
        else {
            int i = 1;
            while (I18n.hasKey(itemName + ".lore." + i)){
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
        } else if (I18n.hasKey(itemName + ".hidelore") || I18n.hasKey(itemName + ".hidelore.1")){
            lores.add("");
            lores.add(I18n.format("gui.inventory.shiftfordetail"));
        }
    }
}
