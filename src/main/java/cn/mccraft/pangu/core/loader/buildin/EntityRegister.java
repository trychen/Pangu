package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.BaseRegister;
import cn.mccraft.pangu.core.loader.RegisteringItem;
import cn.mccraft.pangu.core.loader.annotation.RegEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

/**
 * @since 1.0.1
 * @author trychen
 */
public class EntityRegister extends BaseRegister<EntityEntry, RegEntity> {
    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> e) {
        for (RegisteringItem<EntityEntry, RegEntity> item : itemSet) try {
            ResourceLocation resourceLocation = new ResourceLocation(item.getItem().getName());
            item.getItem().setRegistryName(resourceLocation);

            if (item.getAnnotation().addEgg()) {
                item.getItem().setEgg(new EntityList.EntityEggInfo(resourceLocation, item.getAnnotation().eggPrimaryColor(), item.getAnnotation().eggSecondaryColor()));
            }
            e.getRegistry().register(item.getItem());
        } catch (Exception ex) {
            PanguCore.getLogger().error("Unable to register " + item.getField().toGenericString(), ex);
        }
        PanguCore.getLogger().info("Processed " + itemSet.size() + " @RegEntity annotations");
    }
}
