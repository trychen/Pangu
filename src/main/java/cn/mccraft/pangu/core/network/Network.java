package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.ModFinder;
import cn.mccraft.pangu.core.util.ReflectUtils;
import lombok.val;
import lombok.var;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.NoSuchElementException;

/**
 * @since 1.0.0
 * @author trychen
 */
public interface Network {
    int BRIDGE_SERVER_MESSAGE = 0;
    int BRIDGE_CLIENT_MESSAGE = 1;
    int BRIDGE_SERVER_MULTIPART_MESSAGE = 2;
    int BRIDGE_CLIENT_MULTIPART_MESSAGE = 3;

    int BRIDGE_SERVER_COMPRESS_MESSAGE = 4;
    int BRIDGE_CLIENT_COMPRESS_MESSAGE = 5;

    int BRIDGE_SERVER_COMPRESS_MULTIPART_MESSAGE = 6;
    int BRIDGE_CLIENT_COMPRESS_MULTIPART_MESSAGE = 7;

    static SimpleNetworkWrapper getNetworkWrapper(Class<?> clazz) {
        // finding mod instance
        val container = ModFinder.getModContainer(clazz);
        if (!container.isPresent()) {
            PanguCore.getLogger().error("Unable to find a mod to get SimpleNetworkWrapper for class " + clazz.toGenericString(), new NoSuchElementException());
            return null;
        }

        Object mod = container.get().getMod();

        // finding SimpleNetworkWrapper from mod
        var channel = ReflectUtils.getField(mod.getClass(), mod, "network", SimpleNetworkWrapper.class, true);
        if (channel == null) channel = ReflectUtils.getField(mod.getClass(), mod, "NETWORK", SimpleNetworkWrapper.class, true);
        if (channel == null) channel = ReflectUtils.invokeMethod(mod, "getNetwork", SimpleNetworkWrapper.class, true);

        if (channel == null) {
            PanguCore.getLogger().error("Unable to find a SimpleNetworkWrapper for mod's class " + clazz.toGenericString(), new NoSuchFieldException());
            return null;
        }
        return channel;
    }
}
