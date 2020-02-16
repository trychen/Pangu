package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.Games;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Listener {
    @SubscribeEvent
    public static void onHUD(RenderGameOverlayEvent.Pre event) {
        if (Games.currentScreen() instanceof Screen && !((Screen) Games.currentScreen()).isDrawHUD()) {
            event.setCanceled(true);
        }
    }
}
