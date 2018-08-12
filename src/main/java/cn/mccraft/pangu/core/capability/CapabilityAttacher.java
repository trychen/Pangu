package cn.mccraft.pangu.core.capability;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.capability.color.CapabilityColor;
import cn.mccraft.pangu.core.capability.food.CapabilityFood;
import cn.mccraft.pangu.core.item.PGFood;
import cn.mccraft.pangu.core.item.silk.PGItemSilk;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PanguCore.ID)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void attackItemStack(AttachCapabilitiesEvent<ItemStack> attachEvent) {
        if (attachEvent.getObject().getItem() instanceof PGFood)
            attachEvent.addCapability(new ResourceLocation(PanguCore.ID, "food_stats"), new CapabilityFood.Provider());
        if (attachEvent.getObject().getItem() instanceof PGItemSilk)
            attachEvent.addCapability(PanguResLoc.of("color_stats"), new CapabilityColor.Provider());
    }
}
