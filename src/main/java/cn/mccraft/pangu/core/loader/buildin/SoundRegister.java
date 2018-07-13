package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegSound;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

/**
 * Register to register SoundEvent with RegSound
 *
 * @author trychen
 * @since 1.0.0.4
 */
@AutoWired(registerCommonEventBus = true)
public class SoundRegister extends StoredElementRegister<SoundEvent, RegSound> {
    @Override
    public void registerField(Field field, SoundEvent soundEvent, RegSound regSound, String domain) {
        String location = regSound.value();
        soundEvent.setRegistryName(location.isEmpty() ? soundEvent.getSoundName() : PanguResLoc.of(domain, location));
        super.registerField(field, soundEvent, regSound, domain);
    }

    /**
     * forge build-in event holder.
     * the implementation of {@link RegSound}
     */
    @SubscribeEvent
    public void registerSound(RegistryEvent.Register<SoundEvent> event) {
        items.forEach(element -> {
            try {
                // start register
                event.getRegistry().register(element.getInstance());
            } catch (Exception ex) {
                PanguCore.getLogger().error("Unable to register " + element.getField().toGenericString(), ex);
            }
        });
        PanguCore.getLogger().info("Processed " + items.size() + " RegSound annotations");
    }
}