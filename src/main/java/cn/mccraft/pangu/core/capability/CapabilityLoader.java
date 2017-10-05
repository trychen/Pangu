package cn.mccraft.pangu.core.capability;

import cn.mccraft.pangu.core.loader.Load;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityLoader {
    @Load
    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(FoodStats.class, new CapabilityFood.Storage(), CapabilityFood.Implementation.class);
    }
}
