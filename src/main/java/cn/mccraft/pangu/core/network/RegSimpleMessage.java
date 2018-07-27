package cn.mccraft.pangu.core.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public @interface RegSimpleMessage {
    Class<? extends IMessage> message();
}
