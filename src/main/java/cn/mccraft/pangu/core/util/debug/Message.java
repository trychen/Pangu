package cn.mccraft.pangu.core.util.debug;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * display message in game
 */
@DevOnly
@SideOnly(Side.CLIENT)
public interface Message {
    Minecraft minecraft =Minecraft.getMinecraft();

    /**
     * add chat message as game info
     */
    static void chat(String message){
        chat(ChatType.CHAT, message);
    }


    /**
     * add chat message as game info
     */
    static void chat(Object message){
        chat(String.valueOf(message));
    }

    /**
     * add chat message as some kind of type
     */
    static void chat(ChatType type, String message){
        minecraft.ingameGUI.addChatMessage(type, new TextComponentString(message));
    }

    /**
     * set overlay message with no color animate
     */
    static void actionBar(String message){
        minecraft.ingameGUI.setOverlayMessage(message, false);
    }

    /**
     * set overlay message with no color animate
     */
    static void actionBar(Object message){
        actionBar(String.valueOf(message));
    }

    /**
     * display a title
     */
    static void subtitle(String title, String subTitle){
        subtitle(title, subTitle, 20, 20 * 5, 20);
    }
    /**
     * display a title
     */
    static void subtitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut){
        minecraft.ingameGUI.displayTitle(title, subTitle, timeFadeIn, displayTime, timeFadeOut);
    }
}
