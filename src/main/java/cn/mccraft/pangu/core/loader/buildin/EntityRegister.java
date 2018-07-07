package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegEntity;
import cn.mccraft.pangu.core.util.resource.PanguResourceLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

/**
 * @since 1.0.1
 * @author trychen
 */
@AutoWired(registerCommonEventBus = true)
public class EntityRegister extends StoredElementRegister<EntityEntry, RegEntity> {
    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> e) {
        items.forEach(element -> {
            try {
                ResourceLocation resourceLocation = PanguResourceLocation.of(element.getInstance().getName());
                element.getInstance().setRegistryName(resourceLocation);

                if (element.getAnnotation().addEgg()) {
                    element.getInstance().setEgg(new EntityList.EntityEggInfo(resourceLocation, element.getAnnotation().eggPrimaryColor(), element.getAnnotation().eggSecondaryColor()));
                }

                e.getRegistry().register(element.getInstance());
            } catch (Exception ex) {
                PanguCore.getLogger().error("Unable to register " + element.getField().toGenericString(), ex);
            }
        });
        PanguCore.getLogger().info("Processed " + items.size() + " @RegEntity annotations");
    }
}
