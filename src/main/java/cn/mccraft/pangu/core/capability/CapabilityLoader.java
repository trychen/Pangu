package cn.mccraft.pangu.core.capability;

import cn.mccraft.pangu.core.capability.color.CapabilityColor;
import cn.mccraft.pangu.core.capability.color.ColorStats;
import cn.mccraft.pangu.core.capability.food.CapabilityFood;
import cn.mccraft.pangu.core.capability.food.FoodStats;
import cn.mccraft.pangu.core.loader.Load;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityLoader {
    @Load
    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(FoodStats.class, new CapabilityFood.Storage(), CapabilityFood.Implementation::new);
        CapabilityManager.INSTANCE.register(ColorStats.class, new CapabilityColor.Storage(), CapabilityColor.Implementation::new);
    }
}
