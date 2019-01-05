package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.ModFinder;
import cn.mccraft.pangu.core.util.ReflectUtils;
import lombok.val;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.NoSuchElementException;

/**
 * @since 1.0.0
 * @author trychen
 */
public interface Network {
    int TOOLTIP_MESSAGE_ID = 50;

    static SimpleNetworkWrapper getNetworkWrapper(Class<?> clazz) {
        // finding mod instance
        val container = ModFinder.getModContainer(clazz);
        if (!container.isPresent()) {
            PanguCore.getLogger().error("Unable to find a mod to get SimpleNetworkWrapper for class " + clazz.toGenericString(), new NoSuchElementException());
            return null;
        }

        Object mod = container.get().getMod();

        // finding SimpleNetworkWrapper from mod
        val channel = ReflectUtils.getField(mod.getClass(), mod, "network", SimpleNetworkWrapper.class, true);

        if (channel == null) {
            PanguCore.getLogger().error("Unable to find a SimpleNetworkWrapper for mod's class " + clazz.toGenericString(), new NoSuchFieldException());
            return null;
        }
        return channel;
    }
}
