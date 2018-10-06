package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.IRegister;
import cn.mccraft.pangu.core.loader.annotation.RegEntity;
import cn.mccraft.pangu.core.util.ModFinder;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author trychen
 * @since 1.0.1
 */
@AutoWired
public class EntityRegister implements IRegister<RegEntity, Entity> {
    private Map<String, Integer> increaseEntityID = new HashMap<>();

    @Override
    public void registerClass(Class<? extends Entity> clazz, RegEntity annotation, String domain) {
        final ModContainer modContainer = ModFinder.getModContainer(clazz).orElseGet(() -> {
            PanguCore.getLogger().error("Unable to get mod container to register entity for class " + clazz.toGenericString());
            return ModFinder.getModContainer("pangu").get();
        });

        final int id = increaseEntityID.getOrDefault(modContainer.getModId(), -1) + 1;

        increaseEntityID.put(
                modContainer.getModId(),
                id
        );

        final ResourceLocation resourceLocation = PanguResLoc.of(modContainer.getModId(), annotation.value());

        EntityRegistry.registerModEntity(
                resourceLocation,
                clazz,
                annotation.value(),
                id,
                modContainer.getMod(),
                annotation.trackingRange(),
                annotation.updateFrequency(),
                annotation.sendsVelocityUpdates());

        if (annotation.addEgg()) {
            EntityRegistry.registerEgg(resourceLocation, annotation.eggPrimaryColor(), annotation.eggSecondaryColor());
        }
    }
}
