package cn.mccraft.pangu.core.util.debug;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.util.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;

import static org.lwjgl.input.Keyboard.*;

@DevOnly
@SideOnly(Side.CLIENT)
public final class GlRotateDebugger {
    private GlRotateDebugger(){
    }

    /**
     * storage the offset data
     */
    public static float offsetX, offsetY, offsetZ;

    /**
     * if enable this debugger tools
     */
    public static boolean enable(){
//        return Environment.INSTANCE.isActiving();
        return false;
    }

    /**
     * active when key down
     * @param event
     */
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!enable()) return;
        float oldX = offsetX, oldY = offsetY, oldZ = offsetZ;

        // base setting
        if (isKeyDown(KEY_NUMPAD0)) reset();
        else if (isKeyDown(KEY_NUMPAD5)) sendOffsetMessage();

        // offset x
        else if (isKeyDown(KEY_NUMPAD6)) offsetX++;
        else if (isKeyDown(KEY_NUMPAD4)) offsetX--;

        // offset y
        else if (isKeyDown(KEY_NUMPAD8)) offsetY++;
        else if (isKeyDown(KEY_NUMPAD2)) offsetY--;

        // offset z
        else if (isKeyDown(KEY_NUMPAD9)) offsetZ++;
        else if (isKeyDown(KEY_NUMPAD1)) offsetZ--;

        if (oldX != offsetX || oldY != offsetY || oldZ != offsetZ) sendOffsetMessage();
    }

    /**
     *
     * @param angle rotate angle
     * @param x rotate x
     * @param y rotate y
     * @param z rotate z
     */
    public static void rotate(float angle, float x, float y, float z) {
        GlStateManager.translate(offsetX, offsetY, offsetZ);
        RenderUtils.drawCube(1, 1, 1);
        GlStateManager.rotate(angle, x, y, z);
        GlStateManager.translate(-offsetX, -offsetY, -offsetZ);
    }

    /**
     * display the offset message in action bar and console
     */
    public static void sendOffsetMessage(){
        String message = offsetMessage();
        PanguCore.getLogger().info(message);
        Message.actionBar(message);
    }

    private static final DecimalFormat dnf = new DecimalFormat ("##0.0");

    /**
     * get the offset message
     * @return string like "[Debugger] x: 0, y: 1, z: 0"
     */
    public static String offsetMessage(){
        return String.format("[Debugger] x: %s, y: %s, z: %s", dnf.format(offsetX), dnf.format(offsetY), dnf.format(offsetZ));
    }

    /**
     * set the offset to zero
     */
    public static void reset(){
        offsetX = 0;
        offsetY = 0;
        offsetZ = 0;
    }
}
