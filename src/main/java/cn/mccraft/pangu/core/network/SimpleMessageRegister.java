package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.util.ModFinder;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface SimpleMessageRegister {
    @AnnotationInjector.StaticInvoke
    static void bind(AnnotationStream<RegSimpleMessage> stream) {
        stream.typeStream().forEach(clazz -> {
            try {
                // get meta
                RegSimpleMessage anno = clazz.getAnnotation(RegSimpleMessage.class);

                // check class
                if (!IMessageHandler.class.isAssignableFrom(clazz)) {
                    PanguCore.getLogger().error("You can only use @RegSimpleMessage to an IMessageHandler class, but given " + clazz.toGenericString(), new IllegalArgumentException());
                    return;
                }
                SimpleNetworkWrapper channel = getNetworkWrapper(clazz);
                if (channel == null) return;

                // init handler
                //noinspection unchecked
                IMessageHandler messageHandler = (IMessageHandler) InstanceHolder.getOrNewInstance(clazz);

                //noinspection unchecked
                channel.registerMessage(messageHandler, anno.message(), anno.id(), anno.side());

                PanguCore.getLogger().info("Registered SimpleMessage handler for class " + clazz.toGenericString());
            } catch (Exception e) {
                PanguCore.getLogger().error("Unexpected error while registering SimpleMessage for class" + clazz.toGenericString(), e);
            }
        });
    }

    static SimpleNetworkWrapper getNetworkWrapper(Class<?> clazz) {
        // finding mod instance
        Optional<ModContainer> container = ModFinder.getModContainer(clazz);
        if (!container.isPresent()) {
            PanguCore.getLogger().error("Unable to find a mod to get SimpleNetworkWrapper for class " + clazz.toGenericString(), new NoSuchElementException());
            return null;
        }

        Object mod = container.get().getMod();

        // finding SimpleNetworkWrapper from mod
        SimpleNetworkWrapper channel = ReflectUtils.getField(mod.getClass(), mod, "network", SimpleNetworkWrapper.class, true);

        if (channel == null) {
            PanguCore.getLogger().error("Unable to find a SimpleNetworkWrapper for mod's class " + clazz.toGenericString(), new NoSuchFieldException());
            return null;
        }
        return channel;
    }
}
