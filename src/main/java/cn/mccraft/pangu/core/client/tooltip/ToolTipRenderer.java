package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.loader.AutoWired;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@AutoWired(registerCommonEventBus = true)
public class ToolTipRenderer {
    protected String text = "";
    protected int time;

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            if (time > 0) {

            }
        }
    }
}
