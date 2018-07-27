package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.util.ModFinder;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.NoSuchElementException;

public interface SimpleMessageRegister {
    @AnnotationInjector.StaticInvoke
    static void bind(AnnotationStream<RegSimpleMessage> stream) {
        stream.typeStream().forEach(clazz -> {
            try {
                // get meta
                RegSimpleMessage anno = clazz.getAnnotation(RegSimpleMessage.class);

                // check class
                if (!clazz.isAssignableFrom(IMessageHandler.class)) {
                    PanguCore.getLogger().error("You can only use @RegSimpleMessage to an IMessageHandler class, but given " + clazz.toGenericString(), new IllegalArgumentException());
                    return;
                }

                // finding SimpleNetworkWrapper from mod
                SimpleNetworkWrapper channel = ReflectUtils.getField(ModFinder.getModContainer(clazz).get().getMod(), "network", SimpleNetworkWrapper.class);
                if (channel == null) {
                    PanguCore.getLogger().error("Unable to find a SimpleNetworkWrapper for mod's class " + clazz.toGenericString(), new NoSuchFieldException());
                    return;
                }

                // init handler
                IMessageHandler messageHandler = (IMessageHandler) InstanceHolder.getOrNewInstance(clazz);

                channel.registerMessage(messageHandler, anno.message(), anno.id(), anno.side());

                PanguCore.getLogger().info("Registered SimpleMessage handler for class " + clazz.toGenericString());
            } catch (NoSuchElementException e) {
                PanguCore.getLogger().error("Unable to find a mod to get SimpleNetworkWrapper for class " + clazz.toGenericString(), e);
            } catch (Exception e) {
                PanguCore.getLogger().error("Unexpected error while registering SimpleMessage for class" + clazz.toGenericString(), e);
            }
        });
    }
}
