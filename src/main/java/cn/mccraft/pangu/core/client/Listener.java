package cn.mccraft.pangu.core.client;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by trychen on 17/9/10.
 */
public class Listener {
    @SubscribeEvent
    public void start(GuiScreenEvent event){
        if (event.getGui() instanceof GuiChest){

        }
    }
}
