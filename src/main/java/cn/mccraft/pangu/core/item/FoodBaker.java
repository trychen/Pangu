package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PanguCore.MODID)
public class FoodBaker {
    @SubscribeEvent
    public static void onBakeFood(ModelBakeEvent event) {
        // TODO add food model
    }
}
