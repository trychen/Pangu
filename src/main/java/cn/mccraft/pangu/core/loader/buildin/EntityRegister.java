package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationRegister;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegEntity;
import cn.mccraft.pangu.core.util.ModFinder;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author trychen
 * @since 1.0.1
 */
@AutoWired(registerCommonEventBus = true)
public class EntityRegister implements AnnotationRegister<RegEntity, Entity> {
    private Map<String, Integer> increaseEntityID = new HashMap<>();
    private Set<EntityEntry> registerEntityEntries = new HashSet<>();

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

        EntityEntryBuilder<Entity> builder = EntityEntryBuilder.create()
                .entity(clazz)
                .id(resourceLocation, id)
                .name(annotation.value())
                .tracker(annotation.trackingRange(), annotation.updateFrequency(), annotation.sendsVelocityUpdates());

        if (annotation.addEgg()) {
            builder.egg(annotation.eggPrimaryColor(), annotation.eggSecondaryColor());
        }

        registerEntityEntries.add(builder.build());
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(registerEntityEntries.toArray(new EntityEntry[0]));
    }
}
