package cn.mccraft.pangu.core.capability;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.item.PGFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PanguCore.MODID)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void attackItemStack(AttachCapabilitiesEvent<ItemStack> attachEvent) {
        if (attachEvent.getObject().getItem() instanceof PGFood)
            attachEvent.addCapability(new ResourceLocation(PanguCore.MODID, "food_stats"), new CapabilityFood.Provider());
    }
}
