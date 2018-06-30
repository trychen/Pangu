package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.BaseRegister;
import cn.mccraft.pangu.core.loader.RegisteringItem;
import cn.mccraft.pangu.core.loader.annotation.RegSound;
import cn.mccraft.pangu.core.util.resource.PanguResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Register to register SoundEvent with RegSound
 *
 * @author trychen
 * @since 1.0.0.4
 */
public class SoundRegister extends BaseRegister<SoundEvent, RegSound> {
    /**
     * forge build-in event holder.
     * the implementation of {@link RegSound}
     */
    @SubscribeEvent
    public void registerSound(RegistryEvent.Register<SoundEvent> event) {
        for (RegisteringItem<SoundEvent, RegSound> registeringItem : itemSet) try {
            SoundEvent soundEvent = registeringItem.getItem();
            RegSound regSound = registeringItem.getAnnotation();

            String location = regSound.value();
            ResourceLocation registryName;
            if (location.isEmpty()) {
                registryName = soundEvent.getSoundName();
            } else {
                registryName = PanguResourceLocation.of(location);
            }

            // start register
            event.getRegistry().register(
                    // set registry name
                    soundEvent.setRegistryName(registryName)
            );
        } catch (Exception ex) {
            PanguCore.getLogger().error("Unable to register " + registeringItem.getField().toGenericString(), ex);
        }
        PanguCore.getLogger().info("Processed " + itemSet.size() + " RegSound annotations");
    }
}